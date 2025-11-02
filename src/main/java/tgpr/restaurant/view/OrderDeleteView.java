package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderAddController;
import tgpr.restaurant.controller.OrderDeleteController;
import tgpr.restaurant.model.OrderAddOrDelete;

public class OrderDeleteView extends OrderAddOrDelete<OrderDeleteController> {

    public OrderDeleteView(OrderDeleteController controller) {
        super(controller,
                "Annulation",
                "Voulez-vous annuler cette commande ?",
                "Yes");
    }
    @Override
    protected void onYes() {

        // à compléter -> doit supprimer la commande dans la BD
        close();
    }
}
