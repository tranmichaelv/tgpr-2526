package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.ReservationController;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
    private final String[] days = {"lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi", "dimanche"};
    public ReservationView(ReservationController controller) {

        this.controller = controller;

        setTitle("Réserver une table");
        setHints(List.of(Hint.CENTERED));

        Panel root = Panel.verticalPanel(1);

        setComponent(root);

        root.addComponent(new EmptySpace(new TerminalSize(0,1))); // upper padding

        createHeader(headerIfGuest()).addTo(root);
        createGridLayout().addTo(root);
        createCalendar().addTo(root);

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

    private Panel createCalendar() {
        Panel createCalendar = new Panel();
        createCalendar.setLayoutManager(new LinearLayout(Direction.HORIZONTAL)).center();
        createCalendar.addComponent(new Button("précedent"));
        createCalendar.addComponent(new Label("work in progress: calendar"));
        createCalendar.addComponent(new Button("suivant"));

        return createCalendar;
    }
}
