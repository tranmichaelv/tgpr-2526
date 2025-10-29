package tgpr.restaurant.model;

import org.springframework.util.Assert;
import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ClosingPeriod extends Model {
    public enum Fields {
        Id, StartDate, EndDate, Reason
    }

    public ClosingPeriod() {
    }

    public ClosingPeriod(LocalDate startDate, LocalDate endDate, String reason) {
        this.startDate = startDate;
		this.endDate = endDate;
		this.reason = reason;
    }

    public ClosingPeriod copy() {
        return new ClosingPeriod(startDate, endDate, reason);
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private LocalDate startDate;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    private LocalDate endDate;

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClosingPeriod closingPeriod = (ClosingPeriod) o;
        return id == closingPeriod.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ClosingPeriod[" +
			"id=" + id +
			", startDate=" + startDate +
			", endDate=" + endDate +
			", reason=" + reason +
			"]";
    }

    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        startDate = rs.getObject("start_date", LocalDate.class);
        endDate = rs.getObject("end_date", LocalDate.class);
        reason = rs.getString("reason");
    }

    @Override
    public void reload() {
        reload("select * from closing_periods where id=:id",
            new Params("id", id));
    }

    public static ClosingPeriod getByKey(int id) {
        return queryOne(ClosingPeriod.class, "select * from closing_periods where id=:id",
            new Params("id", id));
    }

    public static List<ClosingPeriod> getAll() {
        return queryList(ClosingPeriod.class, "select * from closing_periods");
    }

    public ClosingPeriod save() {
        int c;
        ClosingPeriod obj = getByKey(id);
        String sql;
        var params = new Params()
			.add("id", id)
			.add("start_date", startDate)
			.add("end_date", endDate)
			.add("reason", reason);
        if (obj == null) {
            if (this.id > 0)
                throw new RuntimeException("Object not found");
            sql = "insert into closing_periods (start_date,end_date,reason) " +
				"values (:start_date,:end_date,:reason)";
            int id = insert(sql, params);
            if (id > 0)
                this.id = id;
        } else {
            sql = "update closing_periods set start_date=:start_date," +
				"end_date=:end_date," +
				"reason=:reason " +
				"where id=:id";
            c = execute(sql, params);
            Assert.isTrue(c == 1, "Something went wrong");
        }
        return this;
    }

    public void delete() {
        int c = execute("delete from closing_periods where id=:id",
			new Params("id", id));
        Assert.isTrue(c == 1, "Something went wrong");
    }


    public String getStartDateFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return startDate.format(formatter);
    }

    public String getEndDateFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return endDate.format(formatter);
    }
}
