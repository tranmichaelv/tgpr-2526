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

    // method getting query result from model and displaying assigned table to the view
    public void assignTable(String nbGuestsText, String serviceName, LocalDate selectedDate) {
        try {
            int numberOfPeople = Integer.parseInt(nbGuestsText);

            // Although view prevents us from selecting dates before today or mondays, it's safer to double-check in the controller as well
            if (selectedDate == null || selectedDate.isBefore(LocalDate.now())) {
                view.showError("Veuillez sélectionner une date valide.");
                return;
            }

            // converting the day of the week in french lowercase (as it is displayed in "week_day" column in "services" table)
            String weekDay = selectedDate.getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, Locale.FRENCH).toLowerCase();

            // calling getByNameAndDay method in Service model
            Service service = Service.getByNameAndDay(serviceName, weekDay);

            // Managing sunday case (it has a "midi" service only"
            if (service == null) {
                view.showError("Pas de service de " + serviceName + " le " + weekDay);
                return;
            }

            // checking if there are tables available left
            Table table = Reservation.findAvailableTable(numberOfPeople, selectedDate, service.getId());
            if (table == null) {
                view.updateTableLabel("(aucune table disponible)");
            } else {
                view.updateTableLabel("Table " + table.getId() + " (" + table.getCapacity() + "p)");
            }
        } catch (NumberFormatException e) {
            view.showError("Veuillez entrer un nombre valide.");
        }
    }
}