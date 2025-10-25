package tgpr.restaurant.model;

import tgpr.framework.Error;

import static java.lang.Integer.parseInt;

public class ProductValidator {

    public static Error isValidName(String name){
        if(name.isBlank()){
            return new Error("Nom obligatoire", Product.Fields.Name);
        }
        if(name.length() < 3){
            return new Error("Nom minimum 3 lettres", Product.Fields.Name);
        }

        return Error.NOERROR;
    }

    public static Error isValidPrice(String price){
        if(!price.isBlank()) {
            try {
                var priceInt = parseInt(price);
                if (priceInt <= 0) {
                    return new Error("Prix nul impossible", Product.Fields.Price);
                }
            } catch (NumberFormatException e) {
                return new Error("Prix non valide.",Product.Fields.Price);
            }
        } else {
            return new Error("Prix obligatoire.", Product.Fields.Price);
        }

        return Error.NOERROR;
    }

    public static Error isValidDescription(String description){
        if(!description.isBlank() && description.length() < 3){
            return new Error("Description minimun 3 caractères", Product.Fields.Description);
        } else
            return Error.NOERROR;
    }
}
