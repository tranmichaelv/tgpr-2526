package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.framework.Model;
import tgpr.restaurant.RestaurantApp;
import tgpr.restaurant.view.SecurityLoginView;


public class SecurityLoginController extends Controller<SecurityLoginView> {

    public void exit() {
        System.exit(0);
    }

    @Override
    public SecurityLoginView getView() {
        return new SecurityLoginView(this);
    }

    public void seedData() {
        Model.seedData(RestaurantApp.DATABASE_SCRIPT_FILE);
    }

    public void login(){
        navigateTo(new SecurityConnectController());
    }

    public void register(){
        navigateTo(new SecurityRegisterController());
    }

}
