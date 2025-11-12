package tgpr.restaurant.controller;

import tgpr.restaurant.model.Order;
import tgpr.framework.Controller;
import tgpr.restaurant.view.OrderChangeView;


public class OrderChangeController extends Controller<OrderChangeView>{
    private final Order order;
    public OrderChangeController(Order order) {
        this.order = order;
    }

    @Override
    public OrderChangeView getView() {
        return new OrderChangeView(this);
    }

    public double getSousTotal() {
        return 15;
    }

    public double getReduction() {
        return 5;
    }

    public double getTotal() {
        return 10;
    }
}