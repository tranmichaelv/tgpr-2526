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

public class Table extends Model {
    public enum Fields {
        Id, Capacity, Location
    }

    public Table() {
    }

    public Table(int capacity, String location) {
        this.capacity = capacity;
		this.location = location;
    }

    public Table copy() {
        return new Table(capacity, location);
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Order> getOrders() {
        return queryList(Order.class, "select * from orders where `table`=:id",
            new Params("id", id));
    }

    public List<Reservation> getReservations() {
        return queryList(Reservation.class, "select * from reservations where `table`=:id",
            new Params("id", id));
    }

    private int capacity;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return id == table.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Table[" +
			"id=" + id +
			", capacity=" + capacity +
			", location=" + location +
			"]";
    }

    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        capacity = rs.getInt("capacity");
        location = rs.getString("location");
    }

    @Override
    public void reload() {
        reload("select * from tables where id=:id",
            new Params("id", id));
    }

    public static Table getByKey(int id) {
        return queryOne(Table.class, "select * from tables where id=:id",
            new Params("id", id));
    }

    public static List<Table> getAll() {
        return queryList(Table.class, "select * from tables");
    }

    public Table save() {
        int c;
        Table obj = getByKey(id);
        String sql;
        var params = new Params()
			.add("id", id)
			.add("capacity", capacity)
			.add("location", location);
        if (obj == null) {
            if (this.id > 0)
                throw new RuntimeException("Object not found");
            sql = "insert into tables (capacity,location) " +
				"values (:capacity,:location)";
            int id = insert(sql, params);
            if (id > 0)
                this.id = id;
        } else {
            sql = "update tables set capacity=:capacity," +
				"location=:location " +
				"where id=:id";
            c = execute(sql, params);
            Assert.isTrue(c == 1, "Something went wrong");
        }
        return this;
    }

    public void delete() {
        int c = execute("delete from tables where id=:id",
			new Params("id", id));
        Assert.isTrue(c == 1, "Something went wrong");
    }
}
