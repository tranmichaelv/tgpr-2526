package tgpr.restaurant;

import tgpr.framework.Controller;
import tgpr.framework.Model;
import tgpr.framework.Tools;

import tgpr.restaurant.controller.*;
import tgpr.restaurant.model.Order;

import tgpr.restaurant.model.Time;
import tgpr.restaurant.model.User;

import java.time.LocalDate;

public class RestaurantApp {
    public final static String DATABASE_SCRIPT_FILE = "/database/tgpr-2526-c03.sql";

    public static void main(String[] args) {
        if (!Model.checkDb(DATABASE_SCRIPT_FILE))
            Controller.abort("Database is not available!");
        else {


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
           // Order newOrder = new Order();
            // Controller.navigateTo(new TestController());
            // Controller.navigateTo(new OrderAddController(test));
            // Controller.navigateTo(new OrderDeleteController(test));
            // Controller.navigateTo(new OrderPayController(test));
           // Controller.navigateTo(new OrderChangeController(test));
            // Controller.navigateTo(new OrderCheckController());
          //  Controller.navigateTo(new CartMenuManagementController());
           // Controller.navigateTo(new TestController());
          //  Controller.navigateTo(new OrderAddController(test));

            Order newOrder = new Order();
            newOrder.setOrderDate(LocalDate.now()); // ou LocalDate.now()

            Controller.navigateTo(new OrderCreateController(newOrder));


         //   Controller.navigateTo(new SecurityLoginController());

        }
    }
}
