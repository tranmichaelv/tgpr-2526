package tgpr.restaurant.controller;

import com.googlecode.lanterna.gui2.BasicWindow;
import tgpr.framework.Controller;
import tgpr.restaurant.model.Reservation;
import tgpr.restaurant.model.Service;
import tgpr.restaurant.model.Table;
import tgpr.restaurant.view.ReservationEditView;
import tgpr.restaurant.view.ReservationView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReservationEditController extends Controller<ReservationEditView> {
    public static final LocalDate TODAY_DEMO = LocalDate.of(2025, 8, 20);
    public Reservation reservation;
    ReservationEditView view = new ReservationEditView(this);
    private YearMonth currentMonth = YearMonth.from(TODAY_DEMO);

    @Override
    public ReservationEditView getView() {
        var view = new ReservationEditView(this);
        YearMonth currentMonth = YearMonth.from(TODAY_DEMO);
        view.init();
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

    public void goToPreviousMonth() {
        currentMonth = currentMonth.minusMonths(1);
    }

    public void goToNextMonth() {
        currentMonth = currentMonth.plusMonths(1);
    }

    public YearMonth getCurrentMonth() {
        // guaranteed initialization of currentMonth
        if (currentMonth == null) {
            currentMonth = YearMonth.from(TODAY_DEMO);
        }
        return currentMonth;
    }

    // the two following methods allow me to take the reservation selected for edit
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }

    // method getting query result from model and displaying assigned table to the view
    public boolean assignTable(String nbGuestsText, String serviceName, LocalDate selectedDate) {
        try {
            int numberOfPeople = Integer.parseInt(nbGuestsText);

            if (selectedDate == null || selectedDate.isBefore(LocalDate.from(TODAY_DEMO))) {
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
