package tgpr.restaurant;

import tgpr.framework.Controller;
import tgpr.framework.Model;
import tgpr.restaurant.controller.OrderCreateController;
import tgpr.restaurant.controller.OrderListClientController;
import tgpr.restaurant.controller.OrderListEmployeeController;

public class RestaurantApp {
    public final static String DATABASE_SCRIPT_FILE = "/database/tgpr-2526-c03.sql";

    public static void main(String[] args) {
        if (!Model.checkDb(DATABASE_SCRIPT_FILE))
            Controller.abort("Database is not available!");
        else {

            /*
             * Exemple de navigation dans les données via le modèle
             */

            // récupére un utilisateur
            /*var user = User.getByKey(1);
            // et ses réservations en tant que client
            var reservations = user.getClientReservations();
            // affiche les réservations
            for (var r : reservations) {
                System.out.println("Réservation #" + r.getId() + " : le " + Tools.asString(r.getReservationDate()) +
                        " pour " + r.getNumberOfPeople() + " personnes" +
                        " à une table de " + r.getTable().getCapacity() + " places.");
            }

            Controller.navigateTo(new TestController());*/

            // Page Ajouter commande
            //Controller.navigateTo(new OrderCreateController());

            // Page Pour afficher la liste des commande
            //Controller.navigateTo(new OrderChangeController());

            Controller.navigateTo(new OrderListClientController());
            //Controller.navigateTo(new OrderListEmployeeController());
            //Controller.navigateTo(new OrderCreateController());
        }
    }
}
