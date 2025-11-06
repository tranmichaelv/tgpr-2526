package tgpr.restaurant.view;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.restaurant.controller.RestaurantClosedController;
import tgpr.restaurant.model.ClosingPeriod;

import java.util.List;

public class RestaurantClosedView extends BasicWindow {
    RestaurantClosedController controller;
    private Button back = new Button("Retour",this::back);
    public RestaurantClosedView(RestaurantClosedController controller, ClosingPeriod closingPeriod) {
        setTitle("Restaurant ferme");
        this.controller = controller;

        setHints(List.of(Hint.CENTERED, Hint.MODAL));

        Panel root = Panel.verticalPanel(1);
        root.addComponent(new Label("Impossible de commander, nous sommes fermees entre le "
                + closingPeriod.getStartDate() + " et le " + closingPeriod.getEndDate()));
        setComponent(root);

        root.addComponent(createButton());
        back.takeFocus();
    }

    private void back(){
        controller.back();
    }

    private Panel createButton(){
        Panel panel = new Panel();

        panel.addComponent(back);
        return panel;
    }
}
