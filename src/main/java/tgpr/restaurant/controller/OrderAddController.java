package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.view.OrderAddView;

public class OrderAddController extends Controller<OrderAddView> {

    private final Order order;

    public OrderAddController(Order order) {
        this.order = order;
    }


    @Override
    public OrderAddView getView() {
        return new  OrderAddView(this);
    }

    // méthode qui crée la commande
    public void createOrder(){
        if(order != null){
            order.save();
        }
    }
}
