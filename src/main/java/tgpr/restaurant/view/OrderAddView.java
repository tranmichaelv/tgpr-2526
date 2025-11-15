package tgpr.restaurant.view;

import tgpr.restaurant.controller.OrderAddController;

public class OrderAddView extends OrderAddOrDelete<OrderAddController> {

    public OrderAddView(OrderAddController controller) {
        super(controller,controller.getOrder() ,"Confirmation", "Voulez-vous créer cette commande ?", "Yes");
    }

    // bouton Oui
    @Override
    protected void onYes() {
        close();
    }
}


