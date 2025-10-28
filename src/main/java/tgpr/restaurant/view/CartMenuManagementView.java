package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.restaurant.controller.CartMenuManagementController;

import java.util.List;

public class CartMenuManagementView extends DialogWindow {

    public CartMenuManagementView(CartMenuManagementController controller) {
        super("Gestion de la carte");

        setHints(List.of(Hint.CENTERED, Hint.MODAL));
        setCloseWindowWithEscape(true);

        Panel root = Panel.gridPanel(1, Margin.of(1,1,0,1), Spacing.of(1));
        setComponent(root);
        // pour rendre un label vert fluo
        root.addComponent(new Label("PRODUITS").addStyle(SGR.BORDERED).setForegroundColor(TextColor.ANSI.GREEN_BRIGHT));

        root.addComponent(controller.getProductCheckView());

        // pour rendre un label jaune fluo
        root.addComponent(new Label("MENU").addStyle(SGR.BORDERED).setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT));

        root.addComponent(controller.getMenuCheckView());

    }

}
