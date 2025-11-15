package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.model.Order.PaymentMethod;
import tgpr.restaurant.model.Security;
import tgpr.restaurant.model.User;
import tgpr.restaurant.view.OrderPayView;

public class OrderPayController extends Controller<OrderPayView> {
    private final Order order;

    public OrderPayController(Order order) {
        this.order = order;
    }
    public Order getOrder() {
        return order;
    }
    @Override
    public OrderPayView getView() {
        return new OrderPayView(this);
    }

    // met à jour le mode de paiement dans la BD
    public void confirmPayment(PaymentMethod method) {
        if (order == null || method == null) return;
        order.setPaymentMethod(method);
        User logged = Security.getLoggedUser();

        //  Si c'est un employé ou le boss, on l'associe à la commande
        if (logged != null && (logged.getRole() == User.Role.Employee || logged.getRole() == User.Role.Boss)) {
            order.setEmployeeId(logged.getId());
        }
        order.save();
    }
}

