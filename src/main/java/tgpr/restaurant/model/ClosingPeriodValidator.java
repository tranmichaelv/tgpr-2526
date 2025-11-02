package tgpr.restaurant.model;

import tgpr.framework.Error;
import java.time.LocalDate;

public class ClosingPeriodValidator {
    public static Error isValidStartDate(LocalDate startDate, LocalDate endDate){
        if(startDate == null || endDate == null ){
            return new Error("Ne peut pas etre vide", ClosingPeriod.Fields.StartDate);
        }

        if(startDate.isAfter(endDate)){
            return new Error("Ne peut pas commencer apres la date de fin", ClosingPeriod.Fields.StartDate);
        }

        return Error.NOERROR;
    }

    public static Error isValidEndDate(LocalDate endDate){
        if(endDate == null){
            return new Error("Ne peut pas etre vide", ClosingPeriod.Fields.StartDate);
        }
        return Error.NOERROR;
    }

    public static Error isValidReason(String reason){
        if(reason.isEmpty()){
           return new Error("Ne peut pas etre vide", ClosingPeriod.Fields.Reason);
        }

        if(reason.length() < 3){
            return new Error("Doit avoir min 3 characteres", ClosingPeriod.Fields.Reason);
        }

        return Error.NOERROR;
    }
}
