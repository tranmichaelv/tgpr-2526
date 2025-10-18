package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.view.ReservationView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class ReservationController extends Controller<ReservationView> {
    public static final int gridSize = 42; // 6 rows, 7 columns
    private final ReservationView view = new ReservationView(this);

    @Override
    public ReservationView getView() {
        return new ReservationView(this);
    }

    // method to print days in the calendar
    public List<LocalDate> getCalendarDays(YearMonth month) {
        List<LocalDate> days = new ArrayList<>();

        /*
            In a 5×7 grid (5 rows, 7 columns), we expect to see:
                - all the days of the current month;
                - the days from the previous (needed to fill the grid).

            For example:
            a) With October 1st being a Wednesday, we need to fill the previous Monday and Tuesday with September 29th and 30th, respectively.

                        here begins october
                                |
                                v
                lun     mar    mer     jeu     ven     sam     dim
                *29*    *30*    1       2       3       4       5
                6       7       8       9       10      11      12
                13      14      15      16      17      18      19
                20      21      22      23      24      25      26
                27      28      29      30      31
                                                 ^
                                                 |
                                   here ends october (and starts november)
        */

        LocalDate firstOfMonth = month.atDay(1);
        int startDayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        // calculating days needed for the previous month
        int nbDaysPrec = startDayOfWeek - 1;

        // adding previous month's days
        YearMonth prevMonth = month.minusMonths(1);
        int lenghtPrevMonth = prevMonth.lengthOfMonth();
        for (int i = nbDaysPrec; i > 0; i--) {
            days.add(prevMonth.atDay(lenghtPrevMonth - i + 1));
        }

        // adding current month's days
        int lenghtOfCurrentMonth = month.lengthOfMonth();
        for (int i = 1; i <= lenghtOfCurrentMonth; i++) {
            days.add(month.atDay(i));
        }

        return days;
    };
}
