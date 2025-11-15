package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.graphics.ThemeDefinition;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import tgpr.restaurant.controller.ReservationController;
import tgpr.restaurant.model.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ReservationView extends DialogWindow {
    public static final String[] DAYS = new String[]{"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
    private static final int MONTH_LABEL_WIDTH = 20;
    private final ReservationController controller;
    private Label monthLabel;
    private Panel calendarGrid;
    private TextBox nbPeople;
    private ComboBox<String> midiSoir;
    private Label tableLabel;
    private LocalDate selectedDate;
    private Button confirm;
    private Button targetDate;
    private List<User> customers;
    private ComboBox<String> clientBox;
    private User selectedClient;
    private String firstName;
    private String lastName;
    private List<User> clientList = new ArrayList<>();
    private User selectedCustomer;
    private int customerId;


    public ReservationView(ReservationController controller) {
        super("Réserver une table");
        this.controller = controller;

        setHints(List.of(Hint.CENTERED));

        Panel root = Panel.verticalPanel(1);

        setComponent(root);

        root.addComponent(new EmptySpace(new TerminalSize(0,1))); // upper padding

        // si l'utilisateur est un guest
        if (!Security.isLogged()) {
            createHeader().addTo(root);
        } else if (!Security.isClient()) {
            clientBox = new ComboBox<>();
            clientBox.setPreferredSize(new TerminalSize(30, 1));

            customers = User.getAll();
            for (User u : customers) {
                if (u.isClient()) {
                    clientBox.addItem(u.getLastName() + ", " + u.getFirstName());
                    clientList.add(u);
                }
            }
            if (!clientList.isEmpty()) {
                clientBox.setSelectedIndex(0);
                selectedCustomer = clientList.getFirst();
            }
            clientBox.addListener((selectedIndex, previousIndex, _) -> {
                if (selectedIndex >= 0) {
                    selectedCustomer = clientList.get(selectedIndex);
                }
            });
            clientBox.addTo(root);
        }
        createGridLayout().addTo(root);

        // The following Listeners will refreshOrders the reservation whenever the service or the number of people is changed
        midiSoir.addListener((_, _, _)->{
            buildCalendarGrid();
            tableLabel.setText("(non attribuée)");
        });

        nbPeople.setTextChangeListener((_, _) -> {
            buildCalendarGrid();
            tableLabel.setText("(non attribuée)");
        });

        createCalendarHeader().addTo(root);
        calendarGrid = createCalendarGridPanel();
        root.addComponent(calendarGrid);
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
        nbPeople = new TextBox();
        nbPeople.setValidationPattern(Pattern.compile("[1-9][0-9]?")); // only integers allowed, max 2 numbers
        createGridLayout.addComponent(nbPeople);

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
        monthLabel = new Label(centerText(getFormattedMonth()));
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
                // skipping empty cases (to reach the day of the week when the month starts)
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
                int numberOfPeople = nbPeople.getText().isEmpty() ? 0 : Integer.parseInt(nbPeople.getText());
                String serviceName = midiSoir.getSelectedItem();


                if (numberOfPeople > 0 && serviceName != null) {
                    // saving the day of the week and service ID in the following variables
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

            selectedDate = Time.getDateChosen().toLocalDate();
            LocalTime selectedHour = Time.getDateChosen().toLocalTime();
            LocalTime maxMidi = LocalTime.of(11,30);
            LocalTime maxSoir = LocalTime.of(18,30);
            // date colored in red if:
            /*
            a) a table corresponding to nbPeople or nbPeople + 1 exists
            b) all tables are occupied
            c) date is not before today and not today OR date is today before 11:30 a.m. for "midi" OR date is today before 18:30 p.m for "soir"
             */
            if (tablesExist
                    && (
                        !date.isBefore(selectedDate) && !date.isEqual(selectedDate)
                                || (date.isEqual(selectedDate) && selectedHour.isBefore(maxMidi) && midiSoir.getSelectedItem().equals("midi"))
                                || (date.isEqual(selectedDate) && selectedHour.isBefore(maxSoir) && midiSoir.getSelectedItem().equals("soir"))
                    )
                    && allTablesOccupied && (!Security.isLogged() || (Security.isClient()) || ((!Security.isClient()) && Security.getUser().getId() == controller.getCustomerId(Security.getUser().getLastName(), Security.getUser().getFirstName())))
            ) {
                dayButton.setTheme(new SimpleTheme(TextColor.ANSI.WHITE, TextColor.ANSI.RED));
                dayButton.setEnabled(false);
            }

            // listener allowing us to color the day we've chosen in green (if table has been found)
            dayButton.addListener(_ ->{
                selectedDate = date;
                boolean tableFound = controller.assignTable(nbPeople.getText(), midiSoir.getSelectedItem(), selectedDate);
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
                    date.isBefore(simulatedToday)
                            || date.getDayOfWeek() == DayOfWeek.MONDAY
                            || (date.getDayOfWeek() == DayOfWeek.SUNDAY && midiSoir.getSelectedItem().equalsIgnoreCase("soir"))
                            /* Check pour la date du jour
                            exemples :
                                1) à 13h30, je ne pourrai plus réserver un service du midi pour le jour courant
                                2) à 20h00, je ne pourrai plus réserver un service du soir pour le jour courant
                            */
                            || (date.equals(simulatedToday) && (
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
        monthLabel.setText(centerText(getFormattedMonth())); // updating month label
        buildCalendarGrid();
        invalidate();
    }

    // The centerText method below is used to fix monthLabel's width (in CalendarHeader method)
    private String centerText(String text) {
        int padding = Math.max(0, (MONTH_LABEL_WIDTH - text.length()) / 2);
        return " ".repeat(padding) + text + " ".repeat(MONTH_LABEL_WIDTH - padding - text.length());
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
            // if guest, "annuler" button will navigate back to homepage
            if (!Security.isLogged()) {
                Button cancel = new Button("Annuler");
                cancel.addListener(_ -> controller.retour());
                confirm = new Button("Confirmer la réservation", () -> {
                    int numberOfPeople = Integer.parseInt(nbPeople.getText());
                    String serviceName = midiSoir.getSelectedItem();
                    String tableInfo = tableLabel.getText();

                    String weekDay = selectedDate.getDayOfWeek()
                            .getDisplayName(TextStyle.FULL, Locale.FRENCH).toLowerCase();

                    Service service = Service.getByNameAndDay(serviceName, weekDay);
                    Table table = Reservation.findAvailableTable(numberOfPeople, selectedDate, service.getId());

                    if (table != null) {
                        Reservation r = new Reservation(
                                numberOfPeople,
                                -1, // Temporary clientId
                                table.getId(),
                                service.getId(),
                                true,
                                selectedDate
                        );
                        Security.setPendingReservation(r); // the chosen reservation is temporary saved with setPendingReservation in Security class
                    }
                    ReservationConfirmationView dialog = new ReservationConfirmationView(selectedDate, numberOfPeople, serviceName, tableInfo);
                    dialog.showDialog(getTextGUI());
                });
                confirm.setEnabled(false);
                buttons.addComponent(cancel);
                buttons.addComponent(confirm);
            } else {
                Button cancel = new Button("Annuler");
                cancel.addListener(_ -> close());
                confirm = new Button("Confirmer", () -> {
                    int numberOfPeople = Integer.parseInt(nbPeople.getText());
                    String serviceName = midiSoir.getSelectedItem();
                    String tableInfo = tableLabel.getText();

                    String weekDay = selectedDate.getDayOfWeek()
                            .getDisplayName(TextStyle.FULL, Locale.FRENCH).toLowerCase();

                    Service service = Service.getByNameAndDay(serviceName, weekDay);
                    Table table = Reservation.findAvailableTable(numberOfPeople, selectedDate, service.getId());

                    if (table != null) {
                        if (Security.isClient()) {
                            customerId = Security.getUser().getId();
                        } else {
                            customerId = selectedCustomer.getId();
                            }
                            Reservation r = new Reservation(
                                numberOfPeople,
                                // if security is client, we already have ID in security, otherwise we use selectedCustomer from Combobox
                                customerId,
                                table.getId(),
                                service.getId(),
                                true,
                                selectedDate
                        );
                        r.save();
                        close();
                    }

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