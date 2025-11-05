package tgpr.restaurant.view;

import tgpr.restaurant.controller.OrderAddController;

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


