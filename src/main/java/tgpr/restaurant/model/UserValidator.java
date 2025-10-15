package tgpr.restaurant.model;

import tgpr.framework.Error;
import java.util.regex.Pattern;

public class UserValidator {

    public static Error isValidLastName(String lastname) {
        if (lastname.length() < 2) {
            return new Error("Nom minimun 2 lettres");

        }
        return Error.NOERROR;
    }

    public static Error isValidSurname(String lastname) {
        if (lastname.length() < 2) {
            return new Error("Prenom minimun 2 lettres");

        }
        return Error.NOERROR;
    }


    public static Error isValidEmail(String email) {
        if (!email.contains("@email.com")) {
            return new Error("Doit etre un email");
        }
        return Error.NOERROR;
    }

    public static Error isValidPassword(String password) {
//        if (password.length() < 8 && password.matches(".*[A-Z].*")
//            && password.matches(".*\\d.*") && password.matches(".*[^a-zA-Z0-9].*"))
            if (password.length() < 8 &&
                    !Pattern.matches(".*[A-Z].*[a-zA-Z0-9].*\\d.*", password)){
            return new Error("Mot de passe incorrect");
        }
        return Error.NOERROR;
    }

    public static Error isValidConfirmPassword(String password, String confirmPassword) {
        if(!password.equals(confirmPassword))
            return new Error("Mot de passe pas pareil");
        return Error.NOERROR;
    }

    public static Error isValidNumber(String number) {
        if (!number.startsWith("04")) {
            return new Error("numéro de téléphone belge invalide.");
        }
        return Error.NOERROR;
    }


}
