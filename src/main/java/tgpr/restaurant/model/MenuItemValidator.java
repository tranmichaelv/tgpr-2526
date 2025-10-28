package tgpr.restaurant.model;

import tgpr.framework.Error;

import java.util.List;


public class MenuItemValidator {

    public static Error isValidNumberOrder(String number, int maxSize){
        if(!number.isEmpty()) {
            try {
                // number était un string, parseInt permet de le transformer en int
                int numberOrder = Integer.parseInt(number);
                if (numberOrder < 1 || numberOrder > maxSize) {
                    return new Error("Ordre doit être compris entre 1 et " + maxSize, MenuItem.Fields.OrderInMenu);
                }
                // si les donnes entrées ne sont pas un nombre alors ça va sortir une erreur
            } catch (NumberFormatException e) {
                return new Error("Ordre non valide",  MenuItem.Fields.OrderInMenu);
            }
        } else {
            return new Error("Ne peut pas être vide", MenuItem.Fields.OrderInMenu);
        }
        return Error.NOERROR;
    }

    public static Error checkDouble(Product product, List<MenuItem> list){
        // pour tous les produits present dans le menu
        for(MenuItem listProduct : list){
            // produit de la liste == produit en parametres
            if(listProduct.getProductId() == product.getId()){
                return new Error("Ce produit est déjà dans le menu", MenuItem.Fields.Product);
            }
        }
        return Error.NOERROR;
    }
}
