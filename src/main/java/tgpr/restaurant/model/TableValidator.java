package tgpr.restaurant.model;

import tgpr.framework.Error;

public class TableValidator {

    public static Error isValidCapacity(String capacity){
        if(capacity.isBlank()){
            return new Error("Capacité requise",Table.Fields.Capacity);
        }

        if(!capacity.matches("\\d{1,}")){
            return new Error("Doit être un nombre plus grand que 0",Table.Fields.Capacity);
        }

        int cpcity = Integer.parseInt(capacity);

        if(cpcity < 1 || cpcity > 20){
            return new Error("Doit être entre compris 1 et 20",Table.Fields.Capacity);
        }

        return Error.NOERROR;
    }

    public static Error isValidLocation(String location){
        if(location.isBlank()){
            return new Error("Location requise",Table.Fields.Location);
        }

        if(location.length() < 3 || location.length() > 50){
            return new Error("Minimum de 3 et Maximum de 50 caractères.",Table.Fields.Location);
        }

        return Error.NOERROR;
    }
}
