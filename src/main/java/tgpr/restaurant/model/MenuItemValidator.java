package tgpr.restaurant.model;

import tgpr.framework.Error;


public class MenuItemValidator {

    public static Error isValidNumberOrder(String number, int maxSize){
        if(!number.isEmpty()) {
            try {
                int numberOrder = Integer.parseInt(number);
                if (numberOrder < 1 || numberOrder > maxSize) {
                    return new Error("Numero doit etre compris entre 1 et " + maxSize, MenuItem.Fields.OrderInMenu);
                }
            } catch (NumberFormatException e) {
                return new Error("Ordre non valide",  MenuItem.Fields.OrderInMenu);
            }
        } else {
            return new Error("Ne peut pas etre nul", MenuItem.Fields.OrderInMenu);
        }
        return Error.NOERROR;
    }
}
