package tgpr.restaurant;

import tgpr.framework.Controller;
import tgpr.framework.Model;
import tgpr.framework.Tools;
import tgpr.restaurant.controller.TestController;
import tgpr.restaurant.model.User;

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
            var user = User.getByKey(1);
            // et ses réservations en tant que client
            var reservations = user.getClientReservations();
            // affiche les réservations
            for (var r : reservations) {
                System.out.println("Réservation #" + r.getId() + " : le " + Tools.asString(r.getReservationDate()) +
                        " pour " + r.getNumberOfPeople() + " personnes" +
                        " à une table de " + r.getTable().getCapacity() + " places.");
            }

            Controller.navigateTo(new TestController());
        }
    }
}
