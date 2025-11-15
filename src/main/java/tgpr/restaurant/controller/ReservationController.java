package tgpr.restaurant.controller;

import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.framework.Controller;
import tgpr.restaurant.model.Reservation;
import tgpr.restaurant.model.Service;
import tgpr.restaurant.model.Table;
import tgpr.restaurant.model.Time;
import tgpr.restaurant.model.Reservation;
import tgpr.restaurant.model.Service;
import tgpr.restaurant.model.Table;
import tgpr.restaurant.model.Time;
import tgpr.restaurant.view.OrderListClientView;
import tgpr.restaurant.view.ReservationView;

import java.time.*;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.util.Locale;

public class ReservationController extends Controller<DialogWindow> {

    private final ReservationView view = new ReservationView(this); // ReservationView has now access to this controller's methods
    private YearMonth currentMonth = YearMonth.from(Time.getDateChosen());

    @Override
    public DialogWindow getView() {
        ClosingPeriod closingPeriod =  Time.restaurantIsClosed();
        if(closingPeriod != null ){
            return new RestaurantClosedController(closingPeriod).getView();
        } else {
            return view;
        }
    }

    public void retour(){
        navigateTo(new SecurityLoginController());
    }

    // method to print days in the calendar
    public List<LocalDate> getCalendarDays(YearMonth month) {
        List<LocalDate> days = new ArrayList<>();

        LocalDate firstDayOfMonth = month.atDay(1);
        int startDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue(); // getting the week day of the first day of a month (ex.: 1 = monday)

        for (int i = 1; i < startDayOfWeek; i++) {
            days.add(null);
        } // for loop allowing us to reach the day of the week when the month starts

        // adding current month's days
        int lengthOfCurrentMonth = month.lengthOfMonth();
        for (int i = 1; i <= lengthOfCurrentMonth; i++) {
            days.add(month.atDay(i));
        }
        return days;
    }

    public YearMonth getCurrentMonth() {
        if (currentMonth == null) {
            currentMonth = YearMonth.from(Time.getDateChosen());
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

            if (selectedDate == null || selectedDate.isBefore(Time.getDateChosen().toLocalDate())) {
                view.showError("Veuillez sélectionner une date valide.");
                return false;
            }

            String weekDay = selectedDate.getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, Locale.FRENCH).toLowerCase();

            Service service = Service.getByNameAndDay(serviceName, weekDay);

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