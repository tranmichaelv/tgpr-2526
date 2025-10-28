package tgpr.restaurant;

import tgpr.framework.Controller;
import tgpr.framework.Model;
import tgpr.restaurant.controller.OrderChangeController;
import tgpr.restaurant.controller.OrderPayController;

public class RestaurantApp {
    public final static String DATABASE_SCRIPT_FILE = "/database/tgpr-2526-c03.sql";

    public static void main(String[] args) {
        if (!Model.checkDb(DATABASE_SCRIPT_FILE))
            Controller.abort("Database is not available!");
        else {


         //   Controller.navigateTo(new TestController());
            //   Controller.navigateTo(new OrderPayController());
                Controller.navigateTo(new OrderChangeController());

        }
    }
}
