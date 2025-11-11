package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.*;
import tgpr.restaurant.view.OrderChangeView;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;


public class OrderChangeController extends Controller<OrderChangeView>{
    private final Order order;

    public OrderChangeController(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public List<User> getClients(){
        return User.getAll().stream().filter(user -> user.getRole() == User.Role.Client).collect(Collectors.toList());

    }

    public List<Table> getAvailableTables() {
        // si à emporter, pas de table
        if (order.getTakeAway()) {
            return java.util.List.of();
        }

        int nb = order.getEffectivePeopleCount();
        var date = order.getOrderDate();
        int serviceId = order.getServiceId();
        // on doit autoriser la table déjà liée à cette commande
        Integer currentOrderId = order.getId();

        // Candidats qui respectent la règle nb = cap OU nb = cap - 1
        var candidates = Table.getAll().stream()
                .filter(t -> t.getCapacity() == nb || t.getCapacity() == nb + 1)
                .collect(Collectors.toList());

        //  Tables occupées par d'autres commandes le même jour/service
        var busyByOrders = Order.getAll().stream()
                // exclut la commande en cours d'édition
                .filter(o -> o.getId() != currentOrderId)
                .filter(o -> date.equals(o.getOrderDate()) && serviceId == o.getServiceId())
                .map(Order::getTableId)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toSet());

        //  Tables occupées par des réservations le même jour/service
        var busyByReservations = Reservation.getAll().stream()
                .filter(r -> date.equals(r.getReservationDate()) && serviceId == r.getServiceId())
                .map(Reservation::getTableId)
                .collect(Collectors.toSet());

        var busy = new java.util.HashSet<Integer>();
        busy.addAll(busyByOrders);
        busy.addAll(busyByReservations);

        //  Ne garder que les libres, triées par capacité
        return candidates.stream()
                .filter(t -> !busy.contains(t.getId()))
                .sorted((a, b) -> Integer.compare(a.getCapacity(), b.getCapacity()))
                .collect(Collectors.toList());
    }

    public List<Service> getAvailableServices(){
        // Services du bon jour
        var wd = Order.toWeekDay(order.getOrderDate());
        var sameDayServices = Service.getAll().stream()
                .filter(s -> s.getWeekDay() == wd)
                .sorted((a,b) -> a.getStartTime().compareTo(b.getStartTime()))
                .collect(Collectors.toList());

        // et seulement ceux pour lesquels il reste AU MOINS UNE table valable
        return sameDayServices.stream()
                .filter(s -> {
                    int previous = order.getServiceId();
                    try {
                        order.setServiceId(s.getId());
                        return !getAvailableTables().isEmpty();
                    } finally {
                        order.setServiceId(previous); // on remet la valeur initiale
                    }
                })
                .collect(Collectors.toList());
    }

    public double getSousTotal() {
        return order.getOrderItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
    }

    public double getReduction() {
        return order.getDiscount() != null ? order.getDiscount() : 0.0;
    }

    public double getTotal() {
        return getSousTotal() - getReduction();
    }
    @Override
    public OrderChangeView getView() {
        return new OrderChangeView(this);
    }
}