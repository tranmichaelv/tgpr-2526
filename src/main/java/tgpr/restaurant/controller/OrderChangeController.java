package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.model.Service;
import tgpr.restaurant.model.Table;
import tgpr.restaurant.model.User;
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

    public List<Table> getAvailableTables(){
        return Table.getAll().stream().filter(table -> table.getCapacity() >= getOrder().getEffectivePeopleCount()).collect(Collectors.toList());

    }

    public List<Service> getAvailableServices(){
        var wd = Order.toWeekDay(getOrder().getOrderDate());
        return Service.getAll().stream()
                .filter(s -> s.getWeekDay() == wd)       // ne garder que le bon jour
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