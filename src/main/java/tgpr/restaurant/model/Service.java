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

public class Service extends Model {
    public enum Fields {
        Id, Name, StartTime, EndTime, WeekDay
    }

    public enum WeekDay {
        Lundi, Mardi, Mercredi, Jeudi, Vendredi, Samedi, Dimanche;

        public static WeekDay valueOfIgnoreCase(String str) {
            return Arrays.stream(values()).filter(v -> v.name().equalsIgnoreCase(str)).findFirst().orElse(null);
        }
    }

    public Service() {
    }

    public Service(String name, LocalTime startTime, LocalTime endTime, WeekDay weekDay) {
        this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.weekDay = weekDay;
    }

    public Service copy() {
        return new Service(name, startTime, endTime, weekDay);
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Order> getOrders() {
        return queryList(Order.class, "select * from orders where service=:id",
            new Params("id", id));
    }

    public List<Reservation> getReservations() {
        return queryList(Reservation.class, "select * from reservations where service=:id",
            new Params("id", id));
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private LocalTime startTime;

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    private LocalTime endTime;

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    private WeekDay weekDay;

    public WeekDay getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return id == service.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Service[" +
			"id=" + id +
			", name=" + name +
			", startTime=" + startTime +
			", endTime=" + endTime +
			", weekDay=" + weekDay +
			"]";
    }

    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        name = rs.getString("name");
        startTime = rs.getObject("start_time", LocalTime.class);
        endTime = rs.getObject("end_time", LocalTime.class);
        weekDay = WeekDay.valueOfIgnoreCase(rs.getString("week_day"));
    }

    @Override
    public void reload() {
        reload("select * from services where id=:id",
            new Params("id", id));
    }

    public static Service getByKey(int id) {
        return queryOne(Service.class, "select * from services where id=:id",
            new Params("id", id));
    }

    public static List<Service> getAll() {
        return queryList(Service.class, "select * from services");
    }

    public Service save() {
        int c;
        Service obj = getByKey(id);
        String sql;
        var params = new Params()
			.add("id", id)
			.add("name", name)
			.add("start_time", startTime)
			.add("end_time", endTime)
			.add("week_day", weekDay.name().toLowerCase());
        if (obj == null) {
            if (this.id > 0)
                throw new RuntimeException("Object not found");
            sql = "insert into services (name,start_time,end_time,week_day) " +
				"values (:name,:start_time,:end_time,:week_day)";
            int id = insert(sql, params);
            if (id > 0)
                this.id = id;
        } else {
            sql = "update services set name=:name," +
				"start_time=:start_time," +
				"end_time=:end_time," +
				"week_day=:week_day " +
				"where id=:id";
            c = execute(sql, params);
            Assert.isTrue(c == 1, "Something went wrong");
        }
        return this;
    }

    public void delete() {
        int c = execute("delete from services where id=:id",
			new Params("id", id));
        Assert.isTrue(c == 1, "Something went wrong");
    }
    /*
    method used in AssignTable method (in ReservationController) to find a service with :
        a) a given name (either midi or soir);
        b) a given day of the week (the one from the date selected by the user);

    */
    public static Service getByNameAndDay(String name, String weekDay) {
        return queryOne(Service.class,
                "SELECT FROM services s WHERE name = :name AND week_day = :weekDay",
                new Params()
                        .add("name", name)
                        .add("weekDay", weekDay));
    }
}
