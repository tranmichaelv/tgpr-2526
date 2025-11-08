package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.view.OrderDeleteView;

public class OrderDeleteController extends Controller<OrderDeleteView> {

    private final Order order;

    public OrderDeleteController(Order order) {
        this.order = order;
    }
    @Override
    public OrderDeleteView getView() {
        return new OrderDeleteView(this);
    }

    public void deleteOrder(){
        if(order != null){
            order.delete();
        }
    }
}