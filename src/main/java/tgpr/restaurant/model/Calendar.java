package tgpr.restaurant.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class Calendar {
    private YearMonth currentMonth;

    public Calendar() {
        this.currentMonth = YearMonth.now();
    }

    public List<LocalDate> getDaysInMonth() {
        List<LocalDate> days = new ArrayList<>();
        for (int i = 1; i <= currentMonth.lengthOfMonth(); i++) {
            days.add(currentMonth.atDay(i));
        }
        return days;
    }
}
