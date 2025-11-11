package tgpr.restaurant.controller;

import com.googlecode.lanterna.gui2.*;
import tgpr.framework.Controller;
import tgpr.restaurant.model.Product;
import tgpr.restaurant.view.PanierView;

public class PanierController extends Controller<PanierView> {

    @Override
    public PanierView getView() {
        return new PanierView(this);
    }

    public void modifierQuantite(Product produit) {
        BasicWindow popup = new BasicWindow("Modifier la quantité");
        popup.setHints(java.util.List.of(Window.Hint.CENTERED));

        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));
        panel.addComponent(new Label(produit.getName()));
        panel.addComponent(new EmptySpace());



        // Récupère la quantité actuelle du produit dans le panier
        //int quantiteActuelle = tgpr.restaurant.model.Panier.getProduits().get(produit);

        Integer quantiteActuelle = tgpr.restaurant.model.Panier.getProduits().get(produit);
        if (quantiteActuelle == null) {
            // Le produit n'est plus dans le panier → on ferme tout et on recharge
            popup.close();

            if (tgpr.framework.ViewManager.gui.getActiveWindow() != null)
                tgpr.framework.ViewManager.gui.getActiveWindow().close();

            Controller.navigateTo(new PanierController());
            return;
        }


        panel.addComponent(new Label("Quantité actuelle : " + quantiteActuelle));
        panel.addComponent(new EmptySpace());

        // Création du panneau horizontal contenant les boutons d’action (+1, -1, Supprimer, Fermer)
        Panel boutons = new Panel(new LinearLayout(Direction.HORIZONTAL));

        //  +1
        boutons.addComponent(new Button("+1", () -> {
            tgpr.restaurant.model.Panier.ajouterProduit(produit);
            popup.close();
            Controller.navigateTo(new PanierController());
        }));

        //  -1
        boutons.addComponent(new Button("-1", () -> {
            tgpr.restaurant.model.Panier.retirerProduit(produit);
            popup.close();


            if (tgpr.framework.ViewManager.gui.getActiveWindow() != null)
                tgpr.framework.ViewManager.gui.getActiveWindow().close();
            Controller.navigateTo(new PanierController());
        }));



        //  Supprimer
//        boutons.addComponent(new Button("Supprimer", () -> {
//            tgpr.restaurant.model.Panier.supprimerProduit(produit);
//            popup.close();
//            Controller.navigateTo(new PanierController());
//        }));

        // Bouton "Supprimer" : supprime complètement le produit du panier
        boutons.addComponent(new Button("Supprimer", () -> {
            tgpr.restaurant.model.Panier.supprimerProduit(produit);
            popup.close();
            if (tgpr.framework.ViewManager.gui.getActiveWindow() != null)
                tgpr.framework.ViewManager.gui.getActiveWindow().close();
            Controller.navigateTo(new PanierController());
        }));


        //  Fermer
        boutons.addComponent(new Button("Fermer", popup::close));

        panel.addComponent(boutons);
        popup.setComponent(panel);

        // Affiche la popup de manière modale et bloque l’exécution jusqu’à sa fermeture.
        tgpr.framework.ViewManager.gui.addWindowAndWait(popup);    }

    public void connect(){
        navigateTo(new SecurityConnectController());
    }
}
