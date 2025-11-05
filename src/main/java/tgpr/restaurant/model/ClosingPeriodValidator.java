package tgpr.restaurant.model;

import tgpr.framework.Error;
import java.time.LocalDate;
import java.util.Objects;

public class ClosingPeriodValidator {
    /*
        Doit encore verfier qu'il est impossible de rajouter une date de fermeture
        s'il y a une reservation dans la periode de fermeture souhaite
     */
    public static Error isValidStartDate(String startDate, String endDate, ClosingPeriod current){
        if(startDate.isEmpty()){
            return new Error("Ne peut pas être vide", ClosingPeriod.Fields.StartDate);
        }

        // doit etre de type dd/mm/yyyy
        if(!startDate.matches("(0[1-9]|[1-2][0-9]|3[0-1])\\/(0[1-9]|1[0-2])\\/\\d{4}")){
            return new Error("Format de la date invalide", ClosingPeriod.Fields.StartDate);
        }

        LocalDate start = ClosingPeriod.dateFormatter(startDate);

        LocalDate end = null;
        // si endDate n'est pas valide on ne le teste pas
        if(endDate != null && endDate.matches("(0[1-9]|[1-2][0-9]|3[0-1])\\/(0[1-9]|1[0-2])\\/\\d{4}")){
            // formatte endDate (pas avant car il peut etre null/mauvais type donc ca va declencher une erreur)
            end = ClosingPeriod.dateFormatter(endDate);
            if(start.isAfter(end)){
                return new Error("Ne peut pas commencer après la date de fin", ClosingPeriod.Fields.StartDate);
            }
        }

        // test chevauchement
        for(ClosingPeriod c: ClosingPeriod.getAll()){
            if(!(current != null && c.getId() == current.getId())) {
                // test chevauchement
                if (!start.isAfter(c.getEndDate()) && !Objects.requireNonNullElse(end, start).isBefore(c.getStartDate())) {
                    return new Error("Ne peut pas chevaucher une autre date", ClosingPeriod.Fields.StartDate);
                }
            }

        }

        return Error.NOERROR;
    }

    public static Error isValidEndDate(String startDate, String endDate, ClosingPeriod current){
        if(endDate.isEmpty()){
            return new Error("Ne peut pas être vide", ClosingPeriod.Fields.EndDate);
        }
        // doit etre de type dd/mm/yyyy
        if(!endDate.matches("(0[1-9]|[1-2][0-9]|3[0-1])\\/(0[1-9]|1[0-2])\\/\\d{4}")){
            return new Error("Format de la date invalide", ClosingPeriod.Fields.EndDate);
        }

        LocalDate end = ClosingPeriod.dateFormatter(endDate);
        // si startDate n'est pas valide on ne le teste pas
        if(startDate != null && startDate.matches("(0[1-9]|[1-2][0-9]|3[0-1])\\/(0[1-9]|1[0-2])\\/\\d{4}")){
            // formatte startDate (pas avant car il peut etre null/mauvais type donc ca va declencher une erreur)
            LocalDate start = ClosingPeriod.dateFormatter(startDate);
            if(end.isBefore(start)){
                return new Error("Ne peut pas finir avant la date de début", ClosingPeriod.Fields.EndDate);
            }

            // test chevauchement
            for(ClosingPeriod c: ClosingPeriod.getAll()){
                if(!(current != null && c.getId() == current.getId())) {
                    if (!end.isBefore(c.getStartDate()) && !start.isAfter(c.getEndDate())) {
                        return new Error("Ne peut pas chevaucher une autre date", ClosingPeriod.Fields.EndDate);
                    }
                }
            }
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
