package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.framework.Error;
import tgpr.framework.ErrorList;
import tgpr.restaurant.model.Reservation;
import tgpr.restaurant.model.Security;
import tgpr.restaurant.model.User;
import tgpr.restaurant.view.SecurityConnectView;

import java.util.List;

public class SecurityConnectController extends Controller<SecurityConnectView> {
    // exit
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
                pending.save();
                Security.clearPendingReservation();
                showInfo("Votre réservation a été confirmée !");
            }




            //-----------
            //  Vérifie s'il y a un panier temporaire et le restaure
            var panierTemp = tgpr.restaurant.model.Security.getPendingPanier();
            if (panierTemp != null && !panierTemp.isEmpty()) {
                for (var entry : panierTemp.entrySet()) {
                    var produit = entry.getKey();
                    var quantite = entry.getValue();
                    for (int i = 0; i < quantite; i++) {
                        tgpr.restaurant.model.Panier.ajouterProduit(produit);
                    }
                }
                tgpr.restaurant.model.Security.clearPendingPanier();
                showInfo("Les articles de votre panier ont été restaurés !");
            }




            // ✅ Message de confirmation (tu peux le changer ou l'enlever)
            showInfo("Connexion réussie !");
            // navigateTo(new MenuController()); // Exemple si tu veux rediriger vers le menu

            showError(new Error("c'est bon")); // à enlever c'est juste pour tester
            //...
            //...
            //...
            // pour se deconnecter, il y a la fonction logout presente dans security

        } else {
            errors.add("invalid credentials");
        }

        return errors;
    }

}
