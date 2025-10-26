package tgpr.restaurant.model;

import tgpr.framework.Error;

import java.util.List;


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

    public static Error checkDouble(Product product, List<MenuItem> list){
        for(MenuItem item : list){
            if(item.getProductId() == product.getId()){
                return new Error("Ce produit est déjà dans le menu", MenuItem.Fields.Product);
            }
        }
        return Error.NOERROR;
    }
}
