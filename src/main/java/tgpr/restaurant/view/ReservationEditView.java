package tgpr.restaurant.view;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;

import java.util.List;

public class ReservationEditView extends BasicWindow {

    public ReservationEditView() {
        setTitle("Editer une réservation");
        setHints(List.of(Hint.CENTERED));

        Panel root = Panel.verticalPanel(1);

        setComponent(root);
        root.addComponent(currentReservationInfo());


    }

    private Panel currentReservationInfo() {
        Panel currentReservation = new Panel();
        currentReservation.setLayoutManager(new GridLayout(2));
        currentReservation.addComponent(new Label("Table: "));

        return currentReservation;
    };
}
