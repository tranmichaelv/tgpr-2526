package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.framework.Error;
import tgpr.framework.ErrorList;
import tgpr.restaurant.model.Reservation;
import tgpr.restaurant.model.Security;
import tgpr.restaurant.model.User;
import tgpr.restaurant.view.SecurityConnectView;
import tgpr.restaurant.model.Panier;


import java.util.List;

public class SecurityConnectController extends Controller<SecurityConnectView> {
    // exit
//    User user =
    public void exit() {
        System.exit(0);
    }

    public void back(){
        navigateTo(new SecurityLoginController());
    }

    // view
    @Override
    public SecurityConnectView getView() {
        return new SecurityConnectView(this);
    }

    // login il faut mettre la suite
    public List<Error> login(String email, String password) {
        var errors = new ErrorList();
        // check si l'email et le mdp sont bons
        var user = User.checkCredentials(email, password);
        if (user != null) {
            Security.login(user);

            // Adding reservation credentials to the database if logged in from reservationView
            Reservation pending = Security.getPendingReservation();
            if (pending != null) {
                pending.setClientId(user.getId());
                if (user.getRole() == User.Role.Client) {
                    // only client can book for himself
                    // boss and employees must book for another person (whose role is client)
                    pending.save();
                }
                Security.clearPendingReservation();
                showInfo( // only client can book a table for himself
                        user.getRole() == User.Role.Client ?
                                "Votre réservation a été confirmée"
                                : "Le personnel du restaurant ne peut pas réserver pour lui-même. Réservation annulée.\n"
                );
            }




            //------
            // Vérifie s'il y a un panier temporaire et le restaure
            var panierTemp = Security.getPendingPanier();
            if (panierTemp != null && !panierTemp.isEmpty()) {
                for (var entry : panierTemp.entrySet()) {
                    var produit = entry.getKey();
                    var quantite = entry.getValue();

                    // Vérifie si le produit est déjà dans le panier
                    var panierActuel = Panier.getProduits();
                    if (!panierActuel.containsKey(produit)) {
                        // S'il n'existe pas encore → on l'ajoute
                        for (int i = 0; i < quantite; i++) {
                            Panier.ajouterProduit(produit);
                        }
                    }
                    // sinon on ne fait rien (pour éviter la duplication)
                }

                Security.clearPendingPanier();
            }

            //  Message de confirmation (tu peux le changer ou l'enlever)
            showInfo("Connexion réussie ! Bienvenue à nouveau");

            //si la connexion vient du panier, on y retourne
            if(Security.isFromPanier()) {
                Security.clearFromPanier();
                navigateTo(new MenuController());
            }else{
                navigateTo(new OrderListClientController());
            }


        } else
            showError(new Error("invalid credentials"));

        return errors;
    }

}
