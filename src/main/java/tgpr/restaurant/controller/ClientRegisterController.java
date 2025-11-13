package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.framework.ErrorList;
import tgpr.restaurant.model.User;
import tgpr.restaurant.model.UserValidator;
import tgpr.restaurant.view.ClientRegisterView;

public class ClientRegisterController extends Controller<ClientRegisterView> {
    @Override
    public ClientRegisterView getView() {
        return new ClientRegisterView(this);
    }

    public ErrorList check(String lastname, String firstname, String email, String number) {
        var errors = new ErrorList();

        errors.add(UserValidator.isValidLastName(lastname));
        errors.add(UserValidator.isValidFirstName(firstname));
        errors.add(UserValidator.isValidEmail(email));
        errors.add(UserValidator.isValidNumber(number));

        return errors;
    }

    public void validate(String lastname, String firstname, String email, String number){

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
        User user = new User();
        user.setLastName(lastname);
        user.setFirstName(firstname);
        user.setEmail(email);
        user.setPhone(formated);
        user.setRole(User.Role.Client);
        user.save();

    }}
