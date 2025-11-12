package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.Reservation;
import tgpr.restaurant.model.Service;
import tgpr.restaurant.model.Table;
import tgpr.restaurant.view.ReservationView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.util.Locale;
/*
            In a 5×7 grid (5 rows, 7 columns), we expect to see:
                - all the days of the current month;

            For example:
            a) With October 1st being a Wednesday, we need to fill the previous Monday and Tuesday with September 29th and 30th, respectively.

            N.B: for few months, a 6th row may be necessary (that's why gridSize below is 42)
                        here begins october
                                |
                                v
                lun     mar    mer     jeu     ven     sam     dim
                                1       2       3       4       5
                6       7       8       9       10      11      12
                13      14      15      16      17      18      19
                20      21      22      23      24      25      26
                27      28      29      30      31
                                                 ^
                                                 |
                                   here ends october (and starts november)
*/

public class ReservationController extends Controller<ReservationView> {
    public static final LocalDate TODAY_DEMO = LocalDate.of(2025, 8, 20);

    private final ReservationView view = new ReservationView(this); // ReservationView has now access to this controller's methods
    private YearMonth currentMonth = YearMonth.from(TODAY_DEMO);

    @Override
    public ReservationView getReservationView() {
        return view;
    }

        // method to print days in the calendar
    public List<LocalDate> getCalendarDays(YearMonth month) {
        List<LocalDate> days = new ArrayList<>();

        LocalDate firstDayOfMonth = month.atDay(1);
        int startDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue(); // getting the week day of the first day of a month (ex.: 1 = monday)

        for (int i = 1; i < startDayOfWeek; i++) {
            days.add(null);
        } // for loop allowing us to reach the day of the week where the month starts

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
            currentMonth = YearMonth.from(TODAY_DEMO);
        }
        return currentMonth;
    }

    public void goToPreviousMonth() {
        currentMonth = currentMonth.minusMonths(1);
    }

    public void goToNextMonth() {
        currentMonth = currentMonth.plusMonths(1);
    }

    // method getting query result from model and displaying assigned table to the view
    public boolean assignTable(String nbGuestsText, String serviceName, LocalDate selectedDate) {
        try {
            int numberOfPeople = Integer.parseInt(nbGuestsText);

            if (selectedDate == null || selectedDate.isBefore(LocalDate.now())) {
                view.showError("Veuillez sélectionner une date valide.");
                return false;
            }

            String weekDay = selectedDate.getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, Locale.FRENCH).toLowerCase();

            Service service = Service.getByNameAndDay(serviceName, weekDay);

            if (service == null) {
                view.showError("Pas de service de " + serviceName + " le " + weekDay);
                return false;
            }

            Table table = Reservation.findAvailableTable(numberOfPeople, selectedDate, service.getId());
            if (table == null) {
                view.updateTableLabel("(aucune table disponible)");
                return false;
            } else {
                view.updateTableLabel("Table " + table.getId() + " (" + table.getCapacity() + "p)");
                return true;
            }
        } catch (NumberFormatException e) {
            view.showError("Veuillez entrer un nombre valide.");
            return false;
        }
    }
}