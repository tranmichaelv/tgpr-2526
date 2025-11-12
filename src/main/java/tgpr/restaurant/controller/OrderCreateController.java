package tgpr.restaurant.controller;

import tgpr.restaurant.model.Order;
import tgpr.restaurant.model.Service;
import tgpr.restaurant.model.Time;
import tgpr.restaurant.model.User;
import tgpr.restaurant.view.OrderCreateView;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class OrderCreateController extends OrderChangeController {
    private boolean draftJustCreated = false;
    public OrderCreateController(Order order) {
        super(order); // on réutilise toute la logique (clients, services, totaux…)
        ensureOrderHasId();
    }

    public void ensureOrderHasId() {
        Order o = getOrder();
        if (o.getId() != 0) return;

        //  Toujours une date pour éviter NPE dans toWeekDay()
        if (o.getOrderDate() == null) {
            try { o.setOrderDate(LocalDate.now()); } catch (Throwable ignore) {}
            if (o.getOrderDate() == null) o.setOrderDate(LocalDate.now());
        }

        //
        if (o.getEffectivePeopleCount() == 0) o.setEffectivePeopleCount(1);

        if ( o.getClientId() == 0) {
            List<User> clients = User.getAll().stream()
                    .filter(u -> u.getRole() == User.Role.Client)
                    .collect(Collectors.toList());
            if (!clients.isEmpty()) o.setClientId(clients.get(0).getId());
        }
        if (o.getServiceId() == 0) {
            var wd = Order.toWeekDay(o.getOrderDate());
            List<Service> svcs = Service.getAll().stream()
                    .filter(s -> s.getWeekDay() == wd)
                    .sorted((a,b) -> a.getStartTime().compareTo(b.getStartTime()))
                    .collect(Collectors.toList());
            if (!svcs.isEmpty()) o.setServiceId(svcs.get(0).getId());
        }

        //  Sauver le brouillon -> crée l’ID
        o.save();
        draftJustCreated = true;
    }

    // Si l’utilisateur annule, on supprime le brouillon (cascade sur order_items)
    public void cancelDraftIfAny() {
        if (draftJustCreated) {
            getOrder().delete();
            draftJustCreated = false;
        }
    }

    @Override
    public OrderCreateView getView() {
        return new OrderCreateView(this); // retourne la vue de création
    }
}