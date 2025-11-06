package tgpr.restaurant.controller;
import tgpr.framework.Controller;
import tgpr.restaurant.model.User;
import tgpr.restaurant.view.OrderListClientView;


public class OrderListClientController extends Controller<OrderListClientView> {

    // pour pouvoir avancer en attendant que je fasse le lien avec le login
    public User getUser(){
        return User.getByKey(1);
    }
    public OrderListClientView getReservationView() { return new OrderListClientView(this,getUser());}
}

