package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderAddController;
import tgpr.restaurant.model.OrderAddOrDelete;

public class OrderAddView extends OrderAddOrDelete<OrderAddController> {

    public OrderAddView(OrderAddController controller) {
        super(controller, "Confirmation", "Voulez-vous créer cette commande ?", "Yes");
    }

    @Override
    protected void onYes() {

        // à completer -> doit ajouter la commande dans la BD
        close();
    }
}


