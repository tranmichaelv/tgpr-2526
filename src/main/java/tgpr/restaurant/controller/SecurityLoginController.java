package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.framework.Model;
import tgpr.restaurant.RestaurantApp;
import tgpr.restaurant.model.Time;
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

    public void checkDate(String date){
        if(date.matches("(0[1-9]|[12][0-9]|3[01])\\/(0[1-9]|1[0-2])\\/\\d{4} ([0-1]\\d|2[0-3]):([0-5]\\d)")){
            Time.dateChosen(Time.dateStrToLocalDate(date));
        } else {
            Time.dateChosen(Time.dateOfToday());
        }
    }

}
