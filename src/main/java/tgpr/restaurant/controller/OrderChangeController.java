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
        if (order.getTakeAway()) return java.util.List.of();

        var date = order.getOrderDate();
        int serviceId = order.getServiceId();
        Integer currentOrderId = order.getId();
        Integer currentReservationId = order.getReservationId();

        var busyByOrders = Order.getAll().stream()
                .filter(o -> o.getId() != currentOrderId)
                .filter(o -> date.equals(o.getOrderDate()) && serviceId == o.getServiceId())
                .map(Order::getTableId)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toSet());

        var busyByReservations = Reservation.getAll().stream()
                .filter(r -> r.getId() != (currentReservationId == null ? -1 : currentReservationId))
                .filter(r -> date.equals(r.getReservationDate()) && serviceId == r.getServiceId())
                .map(Reservation::getTableId)
                .collect(Collectors.toSet());

        var busy = new java.util.HashSet<Integer>();
        busy.addAll(busyByOrders);
        busy.addAll(busyByReservations);

        return Table.getAll().stream()
                .filter(t -> !busy.contains(t.getId()))
                .sorted((a,b) -> Integer.compare(a.getCapacity(), b.getCapacity()))
                .collect(Collectors.toList());
    }

    public List<Service> getAvailableServices() {
        var wd = Order.toWeekDay(order.getOrderDate());     // jour de la commande
        return Service.getAll().stream()
                .filter(s -> s.getWeekDay() == wd)
                .sorted((a,b) -> a.getStartTime().compareTo(b.getStartTime()))
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