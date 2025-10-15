package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.framework.Error;
import tgpr.framework.ErrorList;
import tgpr.restaurant.model.User;
import tgpr.restaurant.view.SecurityConnectView;

import java.util.List;

public class SecurityConnectController extends Controller<SecurityConnectView> {
    // Exit
    public void exit() {
        System.exit(0);
    }

    // View
    @Override
    public SecurityConnectView getView() {
        return new SecurityConnectView(this);
    }

    // Back
    public void retour(){
        navigateTo(new SecurityLoginController());
    }

    // Login
    public List<Error> login(String email, String password) {
        var errors = new ErrorList();
        // Check si l'email et le mdp sont bon
        var member = User.checkCredentials(email, password);
        if (member != null) {
            showMessage("It's working","Congratulations","ok"); // Pour debug
        } else
            showError(new Error("invalid credentials"));

        return errors;
    }
}
