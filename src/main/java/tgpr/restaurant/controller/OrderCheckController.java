package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.view.OrderCheckView;

public class OrderCheckController extends Controller<OrderCheckView> {

    @Override
    public OrderCheckView getView() {
        return new OrderCheckView(this);
    }

    public void openEditView() {
    }


    public void openRegisterPaymentView() {
    }

    public void openDetailsView() {
    }

    public void openCancelOrderView() {
    }
}
