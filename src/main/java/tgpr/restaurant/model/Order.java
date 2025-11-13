package tgpr.restaurant.model;

import org.springframework.util.Assert;
import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Order extends Model {
    public enum Fields {
        Id, OrderDate, Client, Table, TakeAway, Online, Service, Employee, Reservation, EffectivePeopleCount, Discount, PaymentMethod
    }

    public enum PaymentMethod {
        Cash, Card, Other;

        public static PaymentMethod valueOfIgnoreCase(String str) {
            return Arrays.stream(values()).filter(v -> v.name().equalsIgnoreCase(str)).findFirst().orElse(null);
        }
    }

    public Order() {
    }

    public Order(LocalDate orderDate, int clientId, boolean takeAway, boolean online, int serviceId, int effectivePeopleCount) {
        this.orderDate = orderDate;
        this.clientId = clientId;
        this.takeAway = takeAway;
        this.online = online;
        this.serviceId = serviceId;
        this.effectivePeopleCount = effectivePeopleCount;
    }

    public Order(LocalDate orderDate, int clientId, Integer tableId, boolean takeAway, boolean online, int serviceId, Integer employeeId, Integer reservationId, int effectivePeopleCount, Double discount, PaymentMethod paymentMethod) {
        this.orderDate = orderDate;
        this.clientId = clientId;
        this.tableId = tableId;
        this.takeAway = takeAway;
        this.online = online;
        this.serviceId = serviceId;
        this.employeeId = employeeId;
        this.reservationId = reservationId;
        this.effectivePeopleCount = effectivePeopleCount;
        this.discount = discount;
        this.paymentMethod = paymentMethod;
    }

    public Order copy() {
        return new Order(orderDate, clientId, tableId, takeAway, online, serviceId, employeeId, reservationId, effectivePeopleCount, discount, paymentMethod);
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<OrderItem> getOrderItems() {
        return queryList(OrderItem.class, "select * from order_items where `order`=:id",
                new Params("id", id));
    }

    private LocalDate orderDate;

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    private int clientId;

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public User getClient() {
        return User.getByKey(clientId);
    }

    private Integer tableId;

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Table getTable() {
        if (tableId == null) return null;
        return Table.getByKey(tableId);
    }

    private boolean takeAway;

    public boolean getTakeAway() {
        return takeAway;
    }

    public void setTakeAway(boolean takeAway) {
        this.takeAway = takeAway;
    }

    private boolean online;

    public boolean getOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    private int serviceId;

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public Service getService() {
        return Service.getByKey(serviceId);
    }

    private Integer employeeId;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public User getEmployee() {
        if (employeeId == null) return null;
        return User.getByKey(employeeId);
    }

    private Integer reservationId;

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public Reservation getReservation() {
        if (reservationId == null) return null;
        return Reservation.getByKey(reservationId);
    }

    private int effectivePeopleCount;

    public int getEffectivePeopleCount() {
        return effectivePeopleCount;
    }

    public void setEffectivePeopleCount(int effectivePeopleCount) {
        this.effectivePeopleCount = effectivePeopleCount;
    }

    private Double discount;

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    private PaymentMethod paymentMethod;

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order[" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", clientId=" + clientId +
                ", tableId=" + tableId +
                ", takeAway=" + takeAway +
                ", online=" + online +
                ", serviceId=" + serviceId +
                ", employeeId=" + employeeId +
                ", reservationId=" + reservationId +
                ", effectivePeopleCount=" + effectivePeopleCount +
                ", discount=" + discount +
                ", paymentMethod=" + paymentMethod +
                "]";
    }

    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        orderDate = rs.getObject("order_date", LocalDate.class);
        clientId = rs.getInt("client");
        tableId = rs.getObject("table", Integer.class);
        takeAway = rs.getBoolean("take_away");
        online = rs.getBoolean("online");
        serviceId = rs.getInt("service");
        employeeId = rs.getObject("employee", Integer.class);
        reservationId = rs.getObject("reservation", Integer.class);
        effectivePeopleCount = rs.getInt("effective_people_count");
        discount = rs.getObject("discount", Double.class);
        paymentMethod = PaymentMethod.valueOfIgnoreCase(rs.getString("payment_method"));
    }

    @Override
    public void reload() {
        reload("select * from orders where id=:id",
                new Params("id", id));
    }

    public static Order getByKey(int id) {
        return queryOne(Order.class, "select * from orders where id=:id",
                new Params("id", id));
    }

    public static List<Order> getAll() {
        return queryList(Order.class, "select * from orders");
    }

    public Order save() {
        int c;
        Order obj = getByKey(id);
        String sql;
        var params = new Params()
                .add("id", id)
                .add("order_date", orderDate)
                .add("client", clientId)
                .add("table", tableId == null ? null : tableId)
                .add("take_away", takeAway)
                .add("online", online)
                .add("service", serviceId)
                .add("employee", employeeId == null ? null : employeeId)
                .add("reservation", reservationId == null ? null : reservationId)
                .add("effective_people_count", effectivePeopleCount)
                .add("discount", discount == null ? null : discount)
                .add("payment_method", paymentMethod == null ? null : paymentMethod.name().toLowerCase());
        if (obj == null) {
            if (this.id > 0)
                throw new RuntimeException("Object not found");
            sql = "insert into orders (order_date,client,`table`,take_away,online,service,employee,reservation,effective_people_count,discount,payment_method) " +
                    "values (:order_date,:client,:table,:take_away,:online,:service,:employee,:reservation,:effective_people_count,:discount,:payment_method)";
            int id = insert(sql, params);
            if (id > 0)
                this.id = id;
        } else {
            sql = "update orders set order_date=:order_date," +
                    "client=:client," +
                    "`table`=:table," +
                    "take_away=:take_away," +
                    "online=:online," +
                    "service=:service," +
                    "employee=:employee," +
                    "reservation=:reservation," +
                    "effective_people_count=:effective_people_count," +
                    "discount=:discount," +
                    "payment_method=:payment_method " +
                    "where id=:id";
            c = execute(sql, params);
            Assert.isTrue(c == 1, "Something went wrong");
        }
        reload();   // reload to get default values
        return this;
    }

    public void delete() {
        int c = execute("delete from orders where id=:id",
                new Params("id", id));
        Assert.isTrue(c == 1, "Something went wrong");
    }

    public void validate() {
        java.util.List<String> errors = new java.util.ArrayList<>();
        // si c'est une nouvelle commande on vérifie pas pendant la modifiaction
        if ( id == 0 && orderDate.isBefore(LocalDate.now())) {
            errors.add("La date de commande ne peut pas être antérieure à aujourd'hui.");
        }
        if (effectivePeopleCount <= 0) {
            errors.add("Le nombre de personnes doit être supérieur à 0.");
        }
        if (discount != null && discount < 0) {
            errors.add("La réduction doit être positive.");
        }

        if (takeAway && getTable() != null) {
            errors.add("Une commande à emporter ne peut pas avoir de table.");
        }
        if (!takeAway && getTable() == null) {
            errors.add("Une commande sur place doit être associée à une table.");
        }
        // Si le service ne correspond pas au jour
        Service.WeekDay orderDay = toWeekDay(orderDate);
        if (getService() == null || getService().getWeekDay() != orderDay) {
            errors.add("Le service ne correspond pas au jour de la commande.");
        }

        // Les clients doivent avoir le rôle CLIENT
        if (getClient().getRole() != User.Role.Client) {
            errors.add("Le client sélectionné doit avoir le rôle 'client'.");
        }
        if (!takeAway && getTable() != null) {
            int cap = getTable().getCapacity();
            int nb  = getEffectivePeopleCount();
            if (nb > cap || nb < cap - 1) {
                errors.add("Le nombre de personnes doit être égal à la capacité de la table ou inférieur d’une seule personne.");
            }
        }

        // Commande en ligne : doit être à emporter et contenir au moins un produit
        if (online) {
            if (!takeAway) {
                errors.add("Les commandes en ligne doivent être à emporter.");
            }
            if (getOrderItems() == null || getOrderItems().isEmpty()) {
                errors.add("Les commandes en ligne doivent contenir au moins un produit.");
            }

        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }
    }

    public static Service.WeekDay toWeekDay(java.time.LocalDate d) {
        java.time.DayOfWeek dow = d.getDayOfWeek();
        return switch (dow) {
            case MONDAY -> Service.WeekDay.Lundi;
            case TUESDAY -> Service.WeekDay.Mardi;
            case WEDNESDAY -> Service.WeekDay.Mercredi;
            case THURSDAY -> Service.WeekDay.Jeudi;
            case FRIDAY -> Service.WeekDay.Vendredi;
            case SATURDAY -> Service.WeekDay.Samedi;
            case SUNDAY -> Service.WeekDay.Dimanche;
        };
    }
}
