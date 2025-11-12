package tgpr.restaurant;

import tgpr.framework.Controller;
import tgpr.framework.Model;
import tgpr.framework.Tools;
import tgpr.restaurant.controller.*;
<<<<<<< HEAD
import tgpr.restaurant.controller.OrderAddController;
import tgpr.restaurant.controller.OrderCheckController;
import tgpr.restaurant.controller.OrderDeleteController;
import tgpr.restaurant.controller.TestController;
import tgpr.restaurant.model.Order;
=======
import tgpr.restaurant.controller.SecurityLoginController;
>>>>>>> eee58f0a7e7ab68512b04a1618137d54ce57a26c
import tgpr.restaurant.model.User;

public class RestaurantApp {
    public final static String DATABASE_SCRIPT_FILE = "/database/tgpr-2526-c03.sql";

    public static void main(String[] args) {
        if (!Model.checkDb(DATABASE_SCRIPT_FILE))
            Controller.abort("Database is not available!");
        else {

            Order test = Order.getByKey(1);

            // récupére un utilisateur
            var user = User.getByKey(1);
            // et ses réservations en tant que client
            var reservations = user.getClientReservations();
            // affiche les réservations
            for (var r : reservations) {
                System.out.println("Réservation #" + r.getId() + " : le " + Tools.asString(r.getReservationDate()) +
                        " pour " + r.getNumberOfPeople() + " personnes" +
                        " à une table de " + r.getTable().getCapacity() + " places.");
            }
            Order test = Order.getByKey(4);
            // Controller.navigateTo(new TestController());
            // Controller.navigateTo(new OrderAddController(test));
            // Controller.navigateTo(new OrderDeleteController(test));
            // Controller.navigateTo(new OrderPayController(test));
            Controller.navigateTo(new OrderChangeController(test));
            // Controller.navigateTo(new OrderCheckController());
          //  Controller.navigateTo(new CartMenuManagementController());
           // Controller.navigateTo(new TestController());
            Controller.navigateTo(new OrderAddController(test));

=======
            Controller.navigateTo(new SecurityLoginController());
>>>>>>> eee58f0a7e7ab68512b04a1618137d54ce57a26c
        }
    }
}
