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

public class Reservation extends Model {
    public enum Fields {
        Id, NumberOfPeople, Client, Table, Service, Employee, Online, ReservationDate
    }

    public Reservation() {
    }

    public Reservation(int numberOfPeople, int clientId, int tableId, int serviceId, boolean online, LocalDate reservationDate) {
        this.numberOfPeople = numberOfPeople;
		this.clientId = clientId;
		this.tableId = tableId;
		this.serviceId = serviceId;
		this.online = online;
		this.reservationDate = reservationDate;
    }

    public Reservation(int numberOfPeople, int clientId, int tableId, int serviceId, Integer employeeId, boolean online, LocalDate reservationDate) {
        this.numberOfPeople = numberOfPeople;
		this.clientId = clientId;
		this.tableId = tableId;
		this.serviceId = serviceId;
		this.employeeId = employeeId;
		this.online = online;
		this.reservationDate = reservationDate;
    }

    public Reservation copy() {
        return new Reservation(numberOfPeople, clientId, tableId, serviceId, employeeId, online, reservationDate);
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Order> getOrders() {
        return queryList(Order.class, "select * from orders where reservation=:id",
            new Params("id", id));
    }

    private int numberOfPeople;

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
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

    private int tableId;

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public Table getTable() {
        return Table.getByKey(tableId);
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

    private boolean online;

    public boolean getOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    private LocalDate reservationDate;

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation reservation = (Reservation) o;
        return id == reservation.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reservation[" +
			"id=" + id +
			", numberOfPeople=" + numberOfPeople +
			", clientId=" + clientId +
			", tableId=" + tableId +
			", serviceId=" + serviceId +
			", employeeId=" + employeeId +
			", online=" + online +
			", reservationDate=" + reservationDate +
			"]";
    }

    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        numberOfPeople = rs.getInt("number_of_people");
        clientId = rs.getInt("client");
        tableId = rs.getInt("table");
        serviceId = rs.getInt("service");
        employeeId = rs.getObject("employee", Integer.class);
        online = rs.getBoolean("online");
        reservationDate = rs.getObject("reservation_date", LocalDate.class);
    }

    @Override
    public void reload() {
        reload("select * from reservations where id=:id",
            new Params("id", id));
    }

    public static Reservation getByKey(int id) {
        return queryOne(Reservation.class, "select * from reservations where id=:id",
            new Params("id", id));
    }

    public static List<Reservation> getAll() {
        return queryList(Reservation.class, "select * from reservations order by reservation_date DESC"); // added order by DESC to show last reservations first
    }

    public Reservation save() {
        int c;
        Reservation obj = getByKey(id);
        String sql;
        var params = new Params()
			.add("id", id)
			.add("number_of_people", numberOfPeople)
			.add("client", clientId)
			.add("table", tableId)
			.add("service", serviceId)
			.add("employee", employeeId == null ? null : employeeId)
			.add("online", online)
			.add("reservation_date", reservationDate);
        if (obj == null) {
            if (this.id > 0)
                throw new RuntimeException("Object not found");
            sql = "insert into reservations (number_of_people,client,`table`,service,employee,online,reservation_date) " +
				"values (:number_of_people,:client,:table,:service,:employee,:online,:reservation_date)";
            int id = insert(sql, params);
            if (id > 0)
                this.id = id;
        } else {
            sql = "update reservations set number_of_people=:number_of_people," +
				"client=:client," +
				"`table`=:table," +
				"service=:service," +
				"employee=:employee," +
				"online=:online," +
				"reservation_date=:reservation_date " +
				"where id=:id";
            c = execute(sql, params);
            Assert.isTrue(c == 1, "Something went wrong");
        }
        reload();   // reload to get default values
        return this;
    }

    public void delete() {
        int c = execute("delete from reservations where id=:id",
			new Params("id", id));
        Assert.isTrue(c == 1, "Something went wrong");
    }

    // method allowing us to find available tables (LIMIT 1 gives us only the first row)
    public static Table findAvailableTable(int numberOfPeople, LocalDate date, int serviceId) {
        return queryOne(Table.class,
                "SELECT * FROM tables " +
                        "WHERE capacity IN (:numberOfPeople, :numberOfPeoplePlusOne) " +
                        "AND id NOT IN (" +
                        "SELECT `table` FROM reservations WHERE reservation_date = :date AND service = :serviceId" +
                        ") " +
                        "ORDER BY capacity ASC LIMIT 1", // starting from rows where nbPeople = capacity (keping rows with nbPeople = capacity + 1 at the botttom)
                new Params()
                        .add("numberOfPeople", numberOfPeople)
                        .add("numberOfPeoplePlusOne", numberOfPeople + 1)
                        .add("date", date)
                        .add("serviceId", serviceId));
    }
}
