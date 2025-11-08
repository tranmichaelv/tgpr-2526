package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.framework.Error;
import tgpr.framework.ErrorList;
import tgpr.restaurant.model.Security;
import tgpr.restaurant.model.User;
import tgpr.restaurant.model.UserValidator;
import tgpr.restaurant.view.SecurityRegisterView;

public class SecurityRegisterController extends Controller<SecurityRegisterView> {
    private final User user;
    public enum Fields {
        PasswordConfirm
    }

    public SecurityRegisterController() {
        this.user = new User();
    }

    public void exit() {
        System.exit(0);
    }

    @Override
    public SecurityRegisterView getView() {
        return new SecurityRegisterView(this);
    }

    public void retour(){
        navigateTo(new SecurityLoginController());
    }

    public ErrorList check(String lastname, String firstname, String email, String password, String confirmPassword, String number) {
        var errors = new ErrorList();

        errors.add(UserValidator.isValidLastName(lastname));
        errors.add(UserValidator.isValidFirstName(firstname));
        errors.add(UserValidator.isValidEmail(email));
        errors.add(UserValidator.isValidPassword(password));
        errors.add(UserValidator.isValidConfirmPassword(password, confirmPassword));
        errors.add(UserValidator.isValidNumber(number));

        return errors;
    }

    // cree l'user et il faut mettre la suite
    public void validate(String lastname, String firstname, String email, String password, String number){

        // change le numero pour le mettre au format demande
        // si commence par +32 le change pour qu'il commence par 0.
        if(number.startsWith("+32")) {
            number = number.replace("+32", "0");
        }

        // puis ajoute la barre et les points aux bons endroits
        String formated = number.substring(0, 4) + "/" +
                number.substring(4, 6) + "." +
                number.substring(6, 8) + "." +
                number.substring(8);

        // assigne chaque variable au nouvel user
        user.setLastName(lastname);
        user.setFirstName(firstname);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(formated);
        user.setRole(User.Role.Client);
        user.save();

        var user = User.getByEmail(email); // mets un user si l'email est present
        if (user != null) {
            Security.login(user);
            showError(new Error("c'est bon")); // à enlever c'est juste pour tester
            //...
            //...
            //...
            // pour se deconnecter, il y a la fonction logout presente dans security
        }
    }
}
