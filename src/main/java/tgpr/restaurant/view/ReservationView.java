package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import org.springframework.cglib.core.Local;
import tgpr.restaurant.controller.ReservationController;

import java.time.LocalDate;
import java.time.YearMonth;
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
    private final ReservationController controller;
    private YearMonth currentMonth = YearMonth.now(); // variable useful for the calendar

    public ReservationView(ReservationController controller) {

        this.controller = controller;

        setTitle("Réserver une table");
        setHints(List.of(Hint.CENTERED));

        Panel root = Panel.verticalPanel(1);

        setComponent(root);

        root.addComponent(new EmptySpace(new TerminalSize(0,1))); // upper padding

        createHeader(headerIfGuest()).addTo(root);
        createGridLayout().addTo(root);
        createCalendarHeader().addTo(root);
        root.addComponent(createStaticCalendarGrid());

        root.addComponent(new EmptySpace(new TerminalSize(0,1)));
    }

    private Panel createHeader(List<Label> headerIfGuest) {
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

        createGridLayout.addComponent(new EmptySpace());
        createGridLayout.addComponent(new EmptySpace()); // cheating out row skip. To refractor later

        // 2nd row
        createGridLayout.addComponent(new Label("Service: "));
        ComboBox<String> multipleOpt = new ComboBox<>();
        multipleOpt.addItem("midi");
        multipleOpt.addItem("soir");
        multipleOpt.setSelectedItem("midi");
        multipleOpt.addListener((selectedIndex, previousSelection, changedByUserInteraction) -> invalidate()); // to change options in ComboBox
        createGridLayout.addComponent(multipleOpt);

        createGridLayout.addComponent(new EmptySpace());
        createGridLayout.addComponent(new EmptySpace()); // cheating out row skip. To refractor later

        // 3rd row
        createGridLayout.addComponent(new Label("Table assignée: "));
        createGridLayout.addComponent(new Label("work in progress: tableId")); // getting the table(s) with an SQL query (model)
        return createGridLayout;
    }

    // Calendar's header showing prec and next buttons, alongside the month (adapted with DateTimeFormatter) and the year
    private Panel createCalendarHeader() {
        Panel createCalendarHeader = new Panel();
        createCalendarHeader.setLayoutManager(new LinearLayout(Direction.HORIZONTAL)).center();
        createCalendarHeader.addComponent(new Button("précedent"));
        Label thisMonthAndYear = new Label(currentMonth.getMonth() + " " + currentMonth.getYear()); // gets current month (uppercase String in English) and current year; example : OCTOBER 2025
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH); // switching from uppercase English to lowercase French (OCTOBER 2025 into octobre 2025)
        thisMonthAndYear.setText(currentMonth.atDay(1).format(formatter)); // formalizing new pattern
        createCalendarHeader.addComponent(thisMonthAndYear);
        createCalendarHeader.addComponent(new Button("suivant"));
        return createCalendarHeader;
    }

    // Static calendar grid (columns : 7)
    private Panel createStaticCalendarGrid() {
        Panel grid = new Panel(new GridLayout(7)).center();
        String[] days = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};

        // adding each day to the grid
        for (String day : days) {
            grid.addComponent(new Label(day));
        }

        List<LocalDate> calendarDays = controller.getCalendarDays(currentMonth);
        // adding each day to the grid with a foreach loop
        for (LocalDate date : calendarDays) {
            Label dayLabel = new Label(String.valueOf(date.getDayOfMonth()));
            // Coloring days that are not from the current month
            if (!date.getMonth().equals(currentMonth.getMonth())) {
                dayLabel.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
            }
            grid.addComponent(dayLabel);
        }
        return grid;
    }
}
