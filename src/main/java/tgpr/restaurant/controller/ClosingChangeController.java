package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.framework.ErrorList;
import tgpr.restaurant.model.ClosingPeriod;
import tgpr.restaurant.model.ClosingPeriodValidator;
import tgpr.restaurant.view.ClosingChangeView;

import java.time.LocalDate;
import java.util.List;

public class ClosingChangeController extends Controller<ClosingChangeView> {

    @Override
    public ClosingChangeView getView() {
        return new ClosingChangeView(this);
    }

    public List<ClosingPeriod> listClosingPeriod(){
        return ClosingPeriod.getAll();
    }

    public void delete(ClosingPeriod closingPeriod){
        closingPeriod.delete();
    }

    public void add(String startDate, String endDate, String reason){
        LocalDate startDateFormatted = ClosingPeriod.dateFormatter(startDate);
        LocalDate endDateFormatted = ClosingPeriod.dateFormatter(endDate);

        ClosingPeriod closingPeriod = new ClosingPeriod(startDateFormatted, endDateFormatted, reason);
        closingPeriod.save();
    }

    public void change(String startDate, String endDate, String reason, ClosingPeriod closingPeriod){
        closingPeriod.setStartDate(ClosingPeriod.dateFormatter(startDate));
        closingPeriod.setStartDate(ClosingPeriod.dateFormatter(endDate));
        closingPeriod.setReason(reason);
        closingPeriod.save();
    }

    public ErrorList checkStartDate(String startDate, String endDate, ClosingPeriod closingPeriod) {
        var errors = new ErrorList();
        errors.add(ClosingPeriodValidator.isValidStartDate(startDate, endDate, closingPeriod));
        return errors;

    }

    public ErrorList checkEndDate(String startDate, String endDate, ClosingPeriod closingPeriod) {
        var errors = new ErrorList();
        errors.add(ClosingPeriodValidator.isValidEndDate(startDate, endDate, closingPeriod));
        return errors;


    }

    public ErrorList checkReason(String reason) {
        var errors = new ErrorList();
        errors.add(ClosingPeriodValidator.isValidReason(reason));
        return errors;
    }

    public ErrorList check(String startDate, String endDate, String reason, ClosingPeriod closingPeriod){
        var errors = new ErrorList();
        errors.add(ClosingPeriodValidator.isValidReason(reason));
        errors.add(ClosingPeriodValidator.isValidEndDate(startDate,endDate, closingPeriod));
        errors.add(ClosingPeriodValidator.isValidStartDate(startDate, endDate, closingPeriod));
        return errors;
    }

    public void showAnError(String message){
        showError("Ne peut pas " + message + " car les informations ne sont pas correctes");
    }
}


