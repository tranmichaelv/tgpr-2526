package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.view.OrderDetailsView;
import tgpr.restaurant.view.OrderListEmployeeView;

public class OrderDetailsController extends Controller<OrderDetailsView> {
    public OrderDetailsController(Order order) {
        super();
    }
    @Override
    public OrderDetailsView getView() {
        return new OrderDetailsView(this);
    }
}