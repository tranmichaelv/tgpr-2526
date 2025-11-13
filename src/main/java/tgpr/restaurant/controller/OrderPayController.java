package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.model.Order.PaymentMethod;
import tgpr.restaurant.view.OrderPayView;

public class OrderPayController extends Controller<OrderPayView> {
    private final Order order;

    public OrderPayController(Order order) {
        this.order = order;
    }
    public Order getOrder() {
        return order;
    }
//    @Override
//    public OrderPayView getView() {
//        return new OrderPayView(this);
//    }

    @Override
    public OrderPayView getView() {
        return new OrderPayView(this);
    }

    // met à jour le mode de paiement dans la BD
    public void confirmPayment(PaymentMethod method) {
        if (order == null || method == null) return;
        order.setPaymentMethod(method);
        order.save();
    }
}

