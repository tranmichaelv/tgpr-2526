package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.view.OrderDetailsView;

public class OrderDetailsController extends Controller<OrderDetailsView> {
    @Override
    public OrderDetailsView getView() {
        return new OrderDetailsView(this);
    }
}
