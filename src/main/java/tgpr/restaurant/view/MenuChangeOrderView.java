package tgpr.restaurant.view;

import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Spacing;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.restaurant.controller.MenuChangeOrderController;

public class MenuChangeOrderView extends DialogWindow {
    private final MenuChangeOrderController controller;
    private String menuName;
    private final int orderInMenu;

    public MenuChangeOrderView(MenuChangeOrderController controller, String menuName, int orderInMenu) {
        super("Changer l'ordre");
        this.controller = controller;
        this.menuName = menuName;
        this.orderInMenu = orderInMenu;

        Panel root = Panel.verticalPanel(1);
        root.addComponent(createBodyPanel());
        setComponent(root);
    }

    private Panel createBodyPanel(){
        Panel panel = Panel.verticalPanel(1,
                new Label("Nouvel ordre pour '" + menuName + "' :"),
                new TextBox(String.valueOf(orderInMenu))
        );
        return panel;
    }
}
