package tgpr.restaurant.view;

import tgpr.restaurant.controller.OrderDeleteController;

public class OrderDeleteView extends OrderAddOrDelete<OrderDeleteController> {

    public OrderDeleteView(OrderDeleteController controller) {
        super(controller,controller.getOrder() ,"Annulation", "Voulez-vous annuler cette commande ?", "Yes");
    }

    // méthode qui crée la commande
    @Override
    protected void onYes() {
        controller.deleteOrder();
        close();
    }
}

