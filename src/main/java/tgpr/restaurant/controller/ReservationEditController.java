package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.Reservation;
import tgpr.restaurant.view.ReservationEditView;

public class ReservationEditController extends Controller<ReservationEditView> {
    public Reservation r;
    ReservationEditView view = new ReservationEditView();
    @Override
    public ReservationEditView getReservationView() {
        return view;
    }
}
