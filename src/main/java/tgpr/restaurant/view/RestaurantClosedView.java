package tgpr.restaurant.view;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.restaurant.model.ClosingPeriod;

import java.util.List;

public class RestaurantClosedView extends DialogWindow {
    private final Button back = new Button("Retour",this::back);

    public RestaurantClosedView(ClosingPeriod closingPeriod) {
        super("Restaurant fermé");

        setHints(List.of(Hint.CENTERED, Hint.MODAL));

        Panel root = Panel.verticalPanel(1);
        root.addComponent(new Label("Impossible de commander, nous sommes fermés entre le "
                + closingPeriod.getStartDate() + " et le " + closingPeriod.getEndDate()));
        setComponent(root);

        root.addComponent(createButton());
        back.takeFocus();
    }

    private void back(){
        close();
    }

    private Panel createButton(){
        Panel panel = new Panel();

        panel.addComponent(back);
        return panel;
    }
}
