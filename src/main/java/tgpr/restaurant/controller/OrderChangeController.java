package tgpr.restaurant.controller;

import tgpr.restaurant.model.Order;
import tgpr.restaurant.view.OrderChangeView;

public class OrderChangeController {

    private final Order order;
    public OrderChangeController(Order order) {
        this.order = order;
    }

    public OrderChangeView getView() {
        return new OrderChangeView(this);
    }
}
