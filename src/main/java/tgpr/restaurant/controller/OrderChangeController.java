package tgpr.restaurant.controller;

import tgpr.restaurant.view.OrderChangeView;

public class OrderChangeController {

    public OrderChangeView getView() {
        return new OrderChangeView(this);
    }
}
