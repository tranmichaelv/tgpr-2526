package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.view.ReservationView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
/*
            In a 5×7 grid (5 rows, 7 columns), we expect to see:
                - all the days of the current month;
                - the days from the previous (needed to fill the grid).

            For example:
            a) With October 1st being a Wednesday, we need to fill the previous Monday and Tuesday with September 29th and 30th, respectively.

            N.B: for few months, a 6th row may be necessary (that's why gridSize below is 42)
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

public class ReservationController extends Controller<ReservationView> {

    private final ReservationView view = new ReservationView(this); // ReservationView has now access to this controller's methods
    private YearMonth currentMonth = YearMonth.now();

    @Override
    public ReservationView getView() {
        return view;
    }

    // method to print days in the calendar
    public List<LocalDate> getCalendarDays(YearMonth month) {
        List<LocalDate> days = new ArrayList<>();

        LocalDate firstDayOfMonth = month.atDay(1);
        int startDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue(); // getting the week day of the first day of a month (ex.: 1 = monday)

        // calculating qty needed to fill first row with previous month's days
        int daysFromPreviousMonth = startDayOfWeek - 1;

        // adding previous month's days
        YearMonth prevMonth = month.minusMonths(1);
        int lenghtPrevMonth = prevMonth.lengthOfMonth(); // 30 (nb. days)
        for (int i = daysFromPreviousMonth; i > 0; i--) { // decrementing i while going towards the end of the previous month
            days.add(prevMonth.atDay(lenghtPrevMonth - i + 1));
        }

        // adding current month's days
        int lenghtOfCurrentMonth = month.lengthOfMonth();
        for (int i = 1; i <= lenghtOfCurrentMonth; i++) {
            days.add(month.atDay(i));
        }

        return days;
    }

    public YearMonth getCurrentMonth() {
        // guaranteed initialization of currentMonth
        if (currentMonth == null) {
            currentMonth = YearMonth.now();
        }
        return currentMonth;
    }

    public void goToPreviousMonth() {
        currentMonth = currentMonth.minusMonths(1);
    }

    public void goToNextMonth() {
        currentMonth = currentMonth.plusMonths(1);
    }
}