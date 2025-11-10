package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.graphics.ThemeDefinition;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import tgpr.restaurant.controller.ReservationController;
import tgpr.restaurant.model.Security;
import tgpr.restaurant.model.Time;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ReservationView extends BasicWindow {
    public static final String[] DAYS = new String[]{"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
    private static final int MONTH_LABEL_WIDTH = 20;
    private final ReservationController controller;
    private Label monthLabel;
    private Panel calendarGrid;
    private TextBox nbGuests;
    private ComboBox<String> midiSoir;
    private Label tableLabel;
    private LocalDate selectedDate;
    private Button confirm;
    private Button targetDate = null ;

    public ReservationView(ReservationController controller) {

        this.controller = controller;


        setTitle("Réserver une table");
        setHints(List.of(Hint.CENTERED));

        Panel root = Panel.verticalPanel(1);

        setComponent(root);

        root.addComponent(new EmptySpace(new TerminalSize(0,1))); // upper padding

        createHeader().addTo(root);
        createGridLayout().addTo(root);

        // The following Listeners will refresh the reservation whenever the service or the number of people is changed
        midiSoir.addListener((newIndex,PrevIndex,byUser)->{
            buildCalendarGrid();
            tableLabel.setText("(non attribuée)");
        });

        nbGuests.setTextChangeListener((newText, changedByUserInteraction) -> {
            buildCalendarGrid();
            tableLabel.setText("(non attribuée)");
        });

        createCalendarHeader().addTo(root);
        root.addComponent(createCalendarGridPanel());
        root.addComponent(buttonsAtBottom());

        root.addComponent(new EmptySpace(new TerminalSize(0,1)));
    }

    private Panel createHeader() {
        Panel createHeader = new Panel();
        for (Label label : headerIfGuest()) {
            label.addTo(createHeader);
        }
        return createHeader;// to-do-list for headerIfGuest : conditions (model)
    }

    private List<Label> headerIfGuest() { // List containing two labels composing the header (login reminder)
        List<Label> headerIfGuest = new ArrayList<>();
        headerIfGuest.add(new Label("Réservation en tant qu'invité"));
        headerIfGuest.add(new Label("Vous pourrez confirmer votre réservation après vous être connecté"));
        headerIfGuest.getFirst().setForegroundColor(TextColor.ANSI.BLACK_BRIGHT); // changing color
        return headerIfGuest;
    }

    private Panel createGridLayout() {
        Panel createGridLayout = new Panel();
        createGridLayout.setLayoutManager(new GridLayout(2)); // two columns

        // 1st row
        createGridLayout.addComponent(new Label("Nombre de personnes: "));
        nbGuests = new TextBox();
        nbGuests.setValidationPattern(Pattern.compile("[1-9][0-9]*")); // only integers allowed
        createGridLayout.addComponent(nbGuests);

        createGridLayout.addComponent(new EmptySpace(), GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.CENTER, true, false, 2, 1)); // to skip row

        // 2nd row
        createGridLayout.addComponent(new Label("Service: "));
        midiSoir = new ComboBox<>();
        midiSoir.addItem("midi");
        midiSoir.addItem("soir");
        midiSoir.setSelectedItem("midi");
        createGridLayout.addComponent(midiSoir);

        createGridLayout.addComponent(new EmptySpace(), GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.CENTER, true, false, 2, 1));

        // 3rd row
        createGridLayout.addComponent(new Label("Table assignée: "));
        tableLabel = new Label("(non attribuée)");
        createGridLayout.addComponent(tableLabel);
        return createGridLayout;
    }

    // Calendar's header showing prec and next buttons, alongside the month + year (adapted with DateTimeFormatter)
    private Panel createCalendarHeader() {
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

    // formatting Month received from controller
    private String getFormattedMonth() {
        try {
            return controller.getCurrentMonth().atDay(1)
                    .format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH));
        } catch (Exception e) {
            showError("Erreur lors du formatage du mois.");
            return "Mois inconnu";
        }

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

            //
            Button dayButton = new Button(String.valueOf(date.getDayOfMonth()));

            // listener allowing us to color the day we've chosen in green (if table has been found)
            dayButton.addListener(btn ->{
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

//            if (current date is before today OR current day of week is monday OR (current day of week is sunday and service is "soir")) {
            if (date.isBefore(simulatedToday) || date.getDayOfWeek() == DayOfWeek.MONDAY || (date.getDayOfWeek() == DayOfWeek.SUNDAY && midiSoir.getSelectedItem().equalsIgnoreCase("soir"))) {
                dayButton.setEnabled(false);
                calendarGrid.addComponent(dayButton
                        .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER))
                        .setPreferredSize(daySize)); // without border
            } else {
                calendarGrid.addComponent(dayButton
                        .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER))
                        .setPreferredSize(daySize)
                        .withBorder(Borders.singleLine())); // with border

            }
        }
    }

    // method refresh to scroll calendar
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

    // buttons "Annuler" and "Confirmer la réservation". Will be reworked later (to match model's logic)
    private Panel buttonsAtBottom() {
        Panel buttons = new Panel(new LinearLayout(Direction.HORIZONTAL)).center();
        if (!Security.isLogged()) {
            Button cancel = new Button("Annuler");
            // if guest, "annuler" button will navigate back to homepage
            cancel.addListener(button -> {
                controller.retour();
            });
            confirm = new Button("Confirmer la réservation", () -> {
                int numberOfPeople = Integer.parseInt(nbGuests.getText());
                String service = midiSoir.getSelectedItem();
                String tableInfo = tableLabel.getText();

                ReservationConfirmationView dialog = new ReservationConfirmationView(selectedDate, numberOfPeople, service, tableInfo);
                dialog.showDialog(getTextGUI());
            });
            confirm.setEnabled(false);
            buttons.addComponent(cancel);
            buttons.addComponent(confirm);
        }
        return buttons;
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

    private void retour() {controller.retour();}
}