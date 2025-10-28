package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.view.OrderDeleteView;

public class OrderDeleteController extends Controller<OrderDeleteView> {

    @Override
    public OrderDeleteView getView() {
        return new OrderDeleteView(this);
    }
}
