package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.view.OrderDetailsView;

public class OrderDetailsController extends Controller<OrderDetailsView> {
    private final Order order;

    public OrderDetailsController(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public OrderDetailsView getView() {
        return new OrderDetailsView(this);
    }
}
