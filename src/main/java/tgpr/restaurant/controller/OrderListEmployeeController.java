package tgpr.restaurant.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.framework.Controller;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.model.User;
import tgpr.restaurant.view.OrderListEmployeeView;

public class OrderListEmployeeController extends Controller<OrderListEmployeeView> {

    /*private final Order order;
    private final OrderListEmployeeView view;*/

    /*public OrderListEmployeeController(Order order){
        this.order = order;
        this.view = new OrderListEmployeeView(this,order);
    }*/

    public User getUser() {
        return User.getByKey(1);
    }
    @Override
    public OrderListEmployeeView getView() {
        return new OrderListEmployeeView(this, getUser());
    }


}
