package tgpr.restaurant.view;

import tgpr.restaurant.controller.OrderDeleteController;

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
