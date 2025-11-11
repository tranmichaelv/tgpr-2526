package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.*;
import tgpr.restaurant.view.OrderChangeView;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;


public class OrderChangeController extends Controller<OrderChangeView>{
    private final Order order;
    private final Order original;
    private  List<OrderItem> originalItems;

    public OrderChangeController(Order order) {
        this.order = order;
        this.original = order.copy();
        this.original.setId(order.getId());
        this.originalItems = order.getOrderItems().stream().map(oi -> {
            OrderItem x = new OrderItem();
            x.setOrderId(order.getId());
            x.setProductId(oi.getProductId());
            x.setQuantity(oi.getQuantity());
            x.setPrice(oi.getPrice());
            return x;
        }).collect(java.util.stream.Collectors.toList());
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
    // Remet la commande à son état d'origine
    public void resetToOriginal() {
        // Champs simples
        order.setOrderDate(original.getOrderDate());
        order.setClientId(original.getClientId());
        order.setTableId(original.getTableId());
        order.setTakeAway(original.getTakeAway());
        order.setOnline(original.getOnline());
        order.setServiceId(original.getServiceId());
        order.setEmployeeId(original.getEmployeeId());
        order.setReservationId(original.getReservationId());
        order.setEffectivePeopleCount(original.getEffectivePeopleCount());
        order.setDiscount(original.getDiscount());
        order.setPaymentMethod(original.getPaymentMethod());

        // Produits : purge puis réinsertion du snapshot
        for (OrderItem oi : order.getOrderItems()) {
            oi.delete();
        }
        for (OrderItem src : originalItems) {
            OrderItem ni = new OrderItem();
            ni.setOrderId(order.getId());
            ni.setProductId(src.getProductId());
            ni.setQuantity(src.getQuantity());
            ni.setPrice(src.getPrice());
            ni.save();
        }

        // Recharger depuis la BD pour repartir propre (ordre + items recalculés)
        order.reload();
    }
    @Override
    public OrderChangeView getView() {
        return new OrderChangeView(this);
    }
}