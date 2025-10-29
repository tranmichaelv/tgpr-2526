package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.ClosingPeriod;
import tgpr.restaurant.view.ClosingChangeview;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClosingChangeController extends Controller<ClosingChangeview> {

    @Override
    public ClosingChangeview getView() {
        return new ClosingChangeview(this);
    }

    public List<ClosingPeriod> listClosingPeriod(){
        return ClosingPeriod.getAll();
    }

    public void delete(ClosingPeriod closingPeriod){
        closingPeriod.delete();
    }

    public void add(String startDate, String endDate, String reason){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate startDateFormatted = LocalDate.parse(startDate, formatter);
        LocalDate endDateFormatted = LocalDate.parse(endDate, formatter);

        ClosingPeriod closingPeriod = new ClosingPeriod(startDateFormatted, endDateFormatted, reason);
        closingPeriod.save();
    }
}


