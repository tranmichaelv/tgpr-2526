package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.framework.Error;
import tgpr.framework.ErrorList;
import tgpr.framework.Params;
import tgpr.restaurant.model.User;
import tgpr.restaurant.model.UserValidator;
import tgpr.restaurant.view.SecurityRegisterView;

import java.util.List;

import static tgpr.framework.Model.execute;

public class SecurityRegisterController extends Controller<SecurityRegisterView> {

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

    public List<Error> Register(String lastname, String surname, String email,
                                    String password, String confirmPassword, String number) {

        var errors = new ErrorList();
        errors.add(UserValidator.isValidLastName(lastname));
        errors.add(UserValidator.isValidSurname(surname));
        errors.add(UserValidator.isValidEmail(email));
        errors.add(UserValidator.isValidPassword(password));
        errors.add(UserValidator.isValidConfirmPassword(password, confirmPassword));
        errors.add(UserValidator.isValidNumber(number));

        if(errors.isEmpty()){
            execute("insert into users (last_name, first_name, email, password, phone, role) " +
                    "values(:last_name,:first_name,:email,:password,:phone,:role)",
                    new Params("last_name", lastname)
                            .add("first_name", surname)
                            .add("email",email)
                            .add("password", password)
                            .add("phone", number)
                            .add("role","client")
                    );
            var user = User.getByEmail(email); // mets un user si l'email est present
            if (user != null)
                showError(new Error("Tout marche")); // Pour debug
        }

        return errors;
    }
}
