package tgpr.restaurant.view;

import tgpr.restaurant.controller.OrderAddController;

public class OrderAddView extends OrderAddOrDelete<OrderAddController> {

    public OrderAddView(OrderAddController controller) {
        super(controller,controller.getOrder() ,"Confirmation", "Voulez-vous créer cette commande ?", "Yes");
    }

    // méthode qui crée la commande
    @Override
    protected void onYes() {
      //  controller.createOrder();
        close();
    }
}


