package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.view.OrderAddView;

public class OrderAddController extends Controller<OrderAddView> {


    @Override
    public OrderAddView getView() {
        return new  OrderAddView(this);
    }
}
