package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.view.OrderPayView;

public class OrderPayController extends Controller<OrderPayView> {

    @Override
    public OrderPayView getView() {
        return new OrderPayView(this);
    }
}

