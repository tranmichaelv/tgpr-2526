package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.framework.Error;
import tgpr.framework.ErrorList;
import tgpr.restaurant.model.Security;
import tgpr.restaurant.model.User;
import tgpr.restaurant.view.SecurityConnectView;

import java.util.List;

public class SecurityConnectController extends Controller<SecurityConnectView> {
    // exit
    public void exit() {
        System.exit(0);
    }

    // view
    @Override
    public SecurityConnectView getView() {
        return new SecurityConnectView(this);
    }

    // retour
    public void back(){
        navigateTo(new SecurityLoginController());
    }

    // login il faut mettre la suite
    public List<Error> login(String email, String password) {
        var errors = new ErrorList();
        // check si l'email et le mdp sont bons
        var user = User.checkCredentials(email, password);
        if (user != null) {
            Security.login(user);
            showError(new Error("c'est bon")); // à enlever c'est juste pour tester
            //...
            //...
            //...
            // pour se deconnecter, il y a la fonction logout presente dans security

        } else
            showError(new Error("invalid credentials"));

        return errors;
    }

}
