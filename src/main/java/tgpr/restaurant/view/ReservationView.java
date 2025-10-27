package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.ReservationController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

 /*
        Gameplan (ref.: screenshots --> pages 4 and 5)
        PHASE 1
            a) realizing the 3 following fields:
                - nb. people
                - service
                - Table assigned
            * gridPanel to place elements in two columns
        PHASE 2
            a) Calendar (check if calendar tool exists in Lanterna's library)
                * implementing buttons "précédent" and "suivant"
        PHASE 3
            a) Buttons "Annuler" and "Confirmer la réservation"

        N.B.: planning may be updated after briefing with my colleague(s) involved in Reservation branch (Maddy)
*/


public class ReservationView extends BasicWindow {
    public static final String[] DAYS = new String[]{"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
    public static final LocalDate today = LocalDate.now();
    private final ReservationController controller;
    private Label monthLabel;
    private Panel calendarGrid;

    public ReservationView(ReservationController controller) {

        this.controller = controller;

        setTitle("Réserver une table");
        setHints(List.of(Hint.CENTERED));

        Panel root = Panel.verticalPanel(1);

        setComponent(root);

        root.addComponent(new EmptySpace(new TerminalSize(0,1))); // upper padding

        createHeader().addTo(root);
        createGridLayout().addTo(root);
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
        createGridLayout.addComponent(new TextBox());

        createGridLayout.addComponent(new EmptySpace(), GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.CENTER, true, false, 2, 1)); // to skip row

        // 2nd row
        createGridLayout.addComponent(new Label("Service: "));
        ComboBox<String> multipleOpt = new ComboBox<>();
        multipleOpt.addItem("midi");
        multipleOpt.addItem("soir");
        multipleOpt.setSelectedItem("midi");
        createGridLayout.addComponent(multipleOpt);

        createGridLayout.addComponent(new EmptySpace(), GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.CENTER, true, false, 2, 1));

        // 3rd row
        createGridLayout.addComponent(new Label("Table assignée: "));
        createGridLayout.addComponent(new Label("work in progress: tableId")); // will be re-worked after linking it with the model
        return createGridLayout;
    }

    // Calendar's header showing prec and next buttons, alongside the month (adapted with DateTimeFormatter) and the year
    private Panel createCalendarHeader() {
        Panel header = new Panel(new LinearLayout(Direction.HORIZONTAL)).center();

        Button prec = (new Button("précedent", () -> {
            controller.goToPreviousMonth();
            refreshCalendar();
        }));
        Button next = (new Button("suivant", () -> {
            controller.goToNextMonth();
            refreshCalendar();
        }));
        monthLabel = new Label(controller.getCurrentMonth().atDay(1).format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH)));
        // gets current month (uppercase, English) and current year (OCTOBER 2025), then converted (lowercase, French --> octobre 2025)

        header.addComponent(prec);
        header.addComponent(monthLabel);
        header.addComponent(next);

        return header;
    }


    private void buildCalendarGrid() {
        calendarGrid.removeAllComponents();

        // printing each day (lun, mar, ...)
        for (String day : DAYS) {
            calendarGrid.addComponent(new Label(day));
        }

        // printing each day from current and (if needed) previous month
        List<LocalDate> calendarDays = controller.getCalendarDays(controller.getCurrentMonth());
        for (LocalDate date : calendarDays) {
            Button dayButton = new Button(String.valueOf(date.getDayOfMonth()), () -> {
                System.out.println("Date selected: " + date); // en vue de la mise à jour dynamique de la table assignée
            });
//            if (current date is before today OR current day of week is monday) {
            if (date.isBefore(today) || date.getDayOfWeek() == DayOfWeek.MONDAY) {
                dayButton.setEnabled(false);
            }
            dayButton.setPreferredSize(new TerminalSize(6,1));
            calendarGrid.addComponent(dayButton);
        }

    }


    // method refresh to scroll calendar
    private void refreshCalendar() {
        monthLabel.setText(controller.getCurrentMonth().atDay(1).format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH)));
        buildCalendarGrid();
        invalidate();
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
        Button cancel = new Button("Annuler");
        Button confirm = new Button("Confirmer la réservation");
        buttons.addComponent(cancel);
        buttons.addComponent(confirm);
        return buttons;
    }
}