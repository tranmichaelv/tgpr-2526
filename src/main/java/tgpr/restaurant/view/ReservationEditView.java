package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.graphics.ThemeDefinition;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import tgpr.restaurant.controller.ReservationEditController;
import tgpr.restaurant.model.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ReservationEditView extends BasicWindow {
    public static final String[] DAYS = new String[]{"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
    public static final LocalDate TODAY_DEMO = LocalDate.of(2025, 8, 20);
    private static final int MONTH_LABEL_WIDTH = 20;
    private final ReservationEditController controller;
    private Reservation reservation;
    private Label monthLabel;
    private Panel calendarGrid;
    private TextBox nbGuests;
    private ComboBox<String> midiSoir;
    private Label tableLabel;
    private LocalDate selectedDate;
    private Button confirm;
    private Button targetDate = null ;

    public ReservationEditView(ReservationEditController controller) {

        this.controller = controller;

        setTitle("Editer une réservation");
        setHints(List.of(Hint.CENTERED));
        Panel root = Panel.verticalPanel();
        setComponent(root);
    }

    // init method gathering all sub-panels
    public void init() {
        this.reservation = controller.getReservation();
        Panel root = (Panel) getComponent();
        root.addComponent(emptyRow());
        root.addComponent(header());
        root.addComponent(currentReservationInfo());
        root.addComponent(emptyRow());
        root.addComponent(inputByUser());
        root.addComponent(emptyRow());
        root.addComponent(calendarHeader());
        root.addComponent(emptyRow());
        root.addComponent(createCalendarGridPanel());
        root.addComponent(buttonsAtBottom());
    }

    private Panel emptyRow() {
        Panel empty = new Panel();
        empty.addComponent(new EmptySpace(new TerminalSize(0,1)));
        return empty;
    }

    private Panel header() {
        Panel header = new Panel(new LinearLayout());
        TextColor gray = new TextColor.RGB(80 ,80,80);
        header.addComponent(new Label("Réservation à modifier").setForegroundColor(gray).addStyle(SGR.BOLD));
        return header;
    }

    // Panel gathering information from the selected reservation
    private Panel currentReservationInfo() {
        Panel currentReservation = new Panel();
        currentReservation.setLayoutManager(new GridLayout(2));
        currentReservation.addComponent(new Label("Table: "));
        currentReservation.addComponent(new Label("Table " + reservation.getTable().getId() + " (" + reservation.getTable().getCapacity() + "p)"));
        currentReservation.addComponent(new Label("Date actuelle: "));
        currentReservation.addComponent(new Label(reservation.getReservationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        currentReservation.addComponent(new Label("Service: "));
        currentReservation.addComponent(new Label(reservation.getService().getName()));

        return currentReservation;
    };

    private Panel inputByUser() {
        Panel userInputGrid = new Panel();
        userInputGrid.setLayoutManager(new GridLayout(2)); // two columns

        // 1st row
        userInputGrid.addComponent(new Label("Nombre de personnes: "));
        nbGuests = new TextBox();
        nbGuests.setValidationPattern(Pattern.compile("[1-9][0-9]?")); // only integers allowed
        userInputGrid.addComponent(nbGuests);

        userInputGrid.addComponent(new EmptySpace(), GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.CENTER, true, false, 2, 1)); // to skip row

        // 2nd row
        userInputGrid.addComponent(new Label("Service: "));
        midiSoir = new ComboBox<>();
        midiSoir.addItem("midi");
        midiSoir.addItem("soir");
        midiSoir.setSelectedItem("midi");
        userInputGrid.addComponent(midiSoir);

        userInputGrid.addComponent(new EmptySpace(), GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.CENTER, true, false, 2, 1));

        // 3rd row
        userInputGrid.addComponent(new Label("Table assignée: "));
        tableLabel = new Label("(non attribuée)");
        userInputGrid.addComponent(tableLabel);
        return userInputGrid;
    }

    private Panel buttonsAtBottom() {
        Panel buttons = new Panel(new LinearLayout(Direction.HORIZONTAL)).center();
        Button cancel = new Button("Annuler", this::close);
        Button delete = new Button("Supprimer");
        if (Security.isBoss()) {
            reservation.delete();
            close();
        }
        confirm = new Button("Confirmer");
        confirm.setEnabled(false);
        buttons.addComponent(cancel);
        buttons.addComponent(delete);
        buttons.addComponent(confirm);
        return buttons;
    }

    // Calendar's header showing prec and next buttons, alongside the month + year (adapted with DateTimeFormatter)
    private Panel calendarHeader() {
        Panel calendarHeader = new Panel().center();
        calendarHeader.setLayoutManager(new GridLayout(3));

        Button prec = (new Button("précedent", () -> {
            controller.goToPreviousMonth();
            refreshCalendar();
        }));

        Button next = (new Button("suivant", () -> {
            controller.goToNextMonth();
            refreshCalendar();
        }));

        // fixing monthLabel's alignment
        monthLabel = new Label(centerText(getFormattedMonth(), MONTH_LABEL_WIDTH));
        monthLabel.setPreferredSize(new TerminalSize(MONTH_LABEL_WIDTH, 1));

        // adding all components to calendarHeader Panel
        calendarHeader.addComponent(prec, GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.CENTER));
        calendarHeader.addComponent(monthLabel,
                GridLayout.createLayoutData(
                        GridLayout.Alignment.CENTER,
                        GridLayout.Alignment.CENTER,
                        true, false)
        );
        calendarHeader.addComponent(next, GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER));

        return calendarHeader;
    }

    private String getFormattedMonth() {
        return controller.getCurrentMonth().atDay(1)
                .format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH));
    }

    // method building the calendar grid
    private void buildCalendarGrid() {
        calendarGrid.removeAllComponents();
        TerminalSize daySize = new TerminalSize(6, 1);

        // printing each day (lun, mar, ...)
        for (String day : DAYS) {
            Label dayLabel = new Label(day);
            dayLabel.setPreferredSize(daySize);
            dayLabel.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER));
            calendarGrid.addComponent(new Label(day).centerCenter());
        }


        // printing each day from current and (if needed) previous month
        List<LocalDate> calendarDays = controller.getCalendarDays(controller.getCurrentMonth());
        for (LocalDate date : calendarDays) {
            if (date == null) {
                // skipping empty cases (to reach the day of the week where the month starts)
                Label empty = new Label(" ");
                empty.setPreferredSize(daySize);
                calendarGrid.addComponent(empty);
                continue;
            }

            Button dayButton = new Button(String.valueOf(date.getDayOfMonth()));
            boolean tablesExist = false;
            boolean allTablesOccupied = false;

            // try-catch to color all taken tables in red (with a certain capacity and for a certain service)
            try {
                // saving nbPeople and service type in the following variables
                int numberOfPeople = nbGuests.getText().isEmpty() ? 0 : Integer.parseInt(nbGuests.getText());
                String serviceName = midiSoir.getSelectedItem();


                if (numberOfPeople > 0 && serviceName != null) {
                    // saving the day of the week and service Id in the following variables
                    String weekDay = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRENCH).toLowerCase();
                    Service service = Service.getByNameAndDay(serviceName, weekDay);

                    if (service != null) { // if a service id is found ...
                        tablesExist = Table.existsCompatibleTable(numberOfPeople);
                        if (tablesExist) { // ... and a table exists
                            Table available = Reservation.findAvailableTable(numberOfPeople, date, service.getId()); // saving an available table
                            allTablesOccupied = (available == null); // setting available as null
                        }
                    }
                }
            } catch (Exception ignored) {}

            // date colored in red if:
            /*
            a) a table corresponding to nbPeople or nbPeople + 1 exists
            b) all tables are occupied
            c) date is not before today
             */
            if (tablesExist && allTablesOccupied && !date.isBefore(Time.getDateChosen().toLocalDate())) {
                dayButton.setTheme(new SimpleTheme(TextColor.ANSI.WHITE, TextColor.ANSI.RED));
                dayButton.setEnabled(false);
            }

            // listener allowing us to color the day we've chosen in green (if table has been found)
            dayButton.addListener(_ ->{
                selectedDate = date;
                boolean tableFound = controller.assignTable(nbGuests.getText(), midiSoir.getSelectedItem(), selectedDate);
                if (tableFound) {
                    if (targetDate != null) {
                        targetDate.setTheme(null);
                    }

                    dayButton.setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.GREEN));
                    targetDate = dayButton;
                    setFocusedInteractable(confirm);
                }
            });


            dayButton.setRenderer(new Button.DefaultButtonRenderer() {
                @Override
                public void drawComponent(TextGUIGraphics graphics, Button button) {
                    // getting current theme color
                    Theme theme = button.getTheme();
                    ThemeDefinition def = theme.getDefinition(button.getClass());

                    // getting BLUE_BRIGHT color if button is focused
                    graphics.setBackgroundColor(button.isFocused() ? TextColor.ANSI.BLUE_BRIGHT : def.getInsensitive().getBackground());
                    graphics.setForegroundColor(def.getInsensitive().getForeground());

                    graphics.fill(' ');

                    String label = button.getLabel();
                    int x = (graphics.getSize().getColumns() - label.length()) / 2;
                    graphics.putString(x, 0, label);
                }
            });

            LocalDate simulatedToday = Time.getDateChosen().toLocalDate();
            LocalTime simulatedTodayHour = Time.getDateChosen().toLocalTime();

//            if (current date is before today OR current day of week is monday OR (current day of week is sunday and service is "soir")) {
            if (
                    date.isBefore(TODAY_DEMO)
                            || date.getDayOfWeek() == DayOfWeek.MONDAY
                            || (date.getDayOfWeek() == DayOfWeek.SUNDAY && midiSoir.getSelectedItem().equalsIgnoreCase("soir"))
                            /* Check pour la date du jour
                            exemples:
                                1) à 13h30, je ne pourrai plus réserver un service du midi pour le jour courant
                                2) à 20h00, je ne pourrai plus réserver un service du soir pour le jour courant
                            */
                            || (date.equals(TODAY_DEMO) && (
                            ((midiSoir.getSelectedItem().equalsIgnoreCase("midi") &&
                                    simulatedTodayHour.isAfter(LocalTime.of(11,30))))
                                    || (midiSoir.getSelectedItem().equalsIgnoreCase("soir") &&
                                    simulatedTodayHour.isAfter(LocalTime.of(18,30))
                            )
                    )
                    )
            ) {
                dayButton.setEnabled(false);
                calendarGrid.addComponent(dayButton
                        .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.END))
                        .setPreferredSize(new TerminalSize(6,2))); // without border
            } else {
                calendarGrid.addComponent(dayButton
                        .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.BEGINNING))
                        .setPreferredSize(new TerminalSize(6,1))
                        .withBorder(Borders.singleLine())); // with border

            }
        }

    }

    // method refreshOrders to scroll calendar
    private void refreshCalendar() {
        monthLabel.setText(centerText(getFormattedMonth(), MONTH_LABEL_WIDTH)); // updating month label
        buildCalendarGrid();
        invalidate();
    }

    // The centerText method below is used to fix monthLabel's width (in CalendarHeader method)
    private String centerText(String text, int width) {
        int padding = Math.max(0, (width - text.length()) / 2);
        return " ".repeat(padding) + text + " ".repeat(width - padding - text.length());
    }

    // calendar grid (columns : 7)
    private Panel createCalendarGridPanel() {
        calendarGrid = new Panel(new GridLayout(7)).center();
        buildCalendarGrid();
        return calendarGrid;
    }

    // method allowing dynamic update of tableLabel
    public void updateTableLabel(String text) {
        tableLabel.setText(text);
        confirm.setEnabled(!"(non attribuée)".equals(text) && !"(aucune table disponible)".equals(text));
        invalidate();
    }

    // Method to show errors
    public void showError(String message) {
        MessageDialog.showMessageDialog(getTextGUI(), "Erreur", message);
    }


}
