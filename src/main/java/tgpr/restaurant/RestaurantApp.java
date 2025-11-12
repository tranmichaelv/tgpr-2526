package tgpr.restaurant;

import tgpr.framework.Controller;
import tgpr.framework.Model;
import tgpr.framework.Tools;
import tgpr.restaurant.controller.OrderAddController;
import tgpr.restaurant.controller.OrderCheckController;
import tgpr.restaurant.controller.OrderDeleteController;
import tgpr.restaurant.controller.TestController;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.controller.SecurityLoginController;
import tgpr.restaurant.model.User;

public class RestaurantApp {
    public final static String DATABASE_SCRIPT_FILE = "/database/tgpr-2526-c03.sql";

    public static void main(String[] args) {
        if (!Model.checkDb(DATABASE_SCRIPT_FILE))
            Controller.abort("Database is not available!");
        else {

            Controller.navigateTo(new SecurityLoginController());
        }
    }
}
