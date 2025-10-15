package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.view.ReservationView;
import tgpr.restaurant.view.TestView;

public class ReservationController extends Controller<ReservationView> {
    private final ReservationView view = new ReservationView(this);

    @Override
    public ReservationView getView() {
        return new ReservationView(this);
    }
}
