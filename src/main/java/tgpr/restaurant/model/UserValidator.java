package tgpr.restaurant.model;

import tgpr.framework.Error;
import tgpr.restaurant.controller.SecurityRegisterController;

import java.util.regex.Pattern;

public class UserValidator {
    public enum Fields {
        PasswordConfirm
    }

    public static Error isValidLastName(String lastname) {
        if (lastname.isBlank())
            return new Error("Nom requis",User.Fields.LastName);

        if (lastname.length() < 2)
            return new Error("Nom minimun 2 lettres",User.Fields.LastName);

        return Error.NOERROR;
    }

    public static Error isValidFirstName(String firstname) {
        if(firstname.isBlank())
            return new Error("Prenom requis",User.Fields.FirstName);

        if (firstname.length() < 2 )
            return new Error("Prenom minimun 2 lettres",User.Fields.FirstName);

        return Error.NOERROR;
    }


    public static Error isValidEmail(String email) {
        var found = User.getByEmail(email);

        if(email.isBlank())
            return new Error("Email requis",User.Fields.Email);

        if(found != null )
            return new Error("Email deja utilise",User.Fields.Email);

        if (!Pattern.matches("\\w{1,}@email.com", email))
            return new Error("Doit etre un email",User.Fields.Email);

        return Error.NOERROR;
    }

    public static Error isValidPassword(String password) {
        if(password.isBlank())
            return new Error("Mot de passe requis",User.Fields.Password);

        if (!Pattern.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$", password))
            return new Error("Mot de passe incorrect",User.Fields.Password);


        return Error.NOERROR;
    }

    public static Error isValidConfirmPassword(String password, String confirmPassword) {
        if(confirmPassword.isBlank())
            return new Error("Confirmation requise",SecurityRegisterController.Fields.PasswordConfirm);

        if(!password.equals(confirmPassword))
            return new Error("Mot de passe pas pareil", SecurityRegisterController.Fields.PasswordConfirm);


        return Error.NOERROR;
    }

    public static Error isValidNumber(String number) {
        if(number.isBlank())
            return new Error("Téléphone requis",User.Fields.Phone);


        if (!number.matches("04\\d{8}") && !number.matches("\\+32\\d{9}"))
            return new Error("numéro belge invalide.",User.Fields.Phone);

        return Error.NOERROR;
    }


}
