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
    public Reservation reservation;
    ReservationEditView view = new ReservationEditView(this);
    private YearMonth currentMonth = YearMonth.now();

    @Override
    public ReservationEditView getReservationView() {
        var view = new ReservationEditView(this);
        view.init();
        return view;
    }

    // the two following methods allow me to take the reservation selected for edit
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }

}
