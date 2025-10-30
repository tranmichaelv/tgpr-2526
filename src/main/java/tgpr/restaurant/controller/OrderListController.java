package tgpr.restaurant.controller;

import com.googlecode.lanterna.gui2.BasicWindow;
import tgpr.restaurant.view.OrderCreateView;
import tgpr.restaurant.view.OrderListView;

public class OrderListController {

    public OrderListView getView() {
        return new OrderListView(this);
    }
}
