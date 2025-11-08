package tgpr.restaurant;

import tgpr.framework.Controller;
import tgpr.framework.Model;
import tgpr.framework.Tools;
import tgpr.restaurant.controller.*;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.model.User;

public class RestaurantApp {
    public final static String DATABASE_SCRIPT_FILE = "/database/tgpr-2526-c03.sql";

    public static void main(String[] args) {
        if (!Model.checkDb(DATABASE_SCRIPT_FILE))
            Controller.abort("Database is not available!");
        else {

            Order test = Order.getByKey(17);

           // Controller.navigateTo(new TestController());
           // Controller.navigateTo(new OrderAddController(test));
          // Controller.navigateTo(new OrderDeleteController(test));
           // Controller.navigateTo(new OrderPayController(test));
            Controller.navigateTo(new OrderChangeController(test));
           // Controller.navigateTo(new OrderCheckController());

        }
    }
}
