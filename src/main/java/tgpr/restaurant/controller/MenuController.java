package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.ClosingPeriod;
import tgpr.restaurant.model.Product;
import tgpr.restaurant.model.Panier;
import tgpr.restaurant.model.Time;
import tgpr.restaurant.view.MenuView;
import com.googlecode.lanterna.gui2.*;

public class MenuController extends Controller<MenuView> {

    @Override
    public MenuView getView() {
        return new MenuView(this);
    }

    /**
     * Ouvre la page de détails d’un menu sélectionné.
     * (ex. Menu Classique, Menu Enfant, etc.)
     */
    public void afficherDetailsMenu(Product produit) {
        navigateTo(new MenuDetailController(produit));
    }

    /**
     * Ouvre le panier actuel de l’utilisateur.
     */
    public void ouvrirPanier() {
        ClosingPeriod closingPeriod =  Time.restaurantIsClosed();
        if(closingPeriod != null ){
            navigateTo(new RestaurantClosedController(closingPeriod));
        } else {
            navigateTo(new PanierController());
        }
    }

    /**
     * Affiche une petite fenêtre (popup) avec la description d’un produit (hors menu),
     * permettant d’ajouter ce produit au panier directement.
     */
    public void afficherPopupProduit(Product produit) {
        BasicWindow popup = new BasicWindow(produit.getName());
        popup.setHints(java.util.List.of(Window.Hint.CENTERED));

        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));

        // Description du produit (si vide, on affiche juste le nom)
        String description = (produit.getDescription() != null && !produit.getDescription().isBlank())
                ? produit.getDescription()
                : "Aucune description disponible.";
        panel.addComponent(new Label(description));
        panel.addComponent(new EmptySpace());

        // Prix du produit
        panel.addComponent(new Label("Prix : " + String.format("%.2f", produit.getPrice()) + " €"));
        panel.addComponent(new EmptySpace());

        // Boutons : Ajouter / Fermer
        Panel boutons = new Panel(new LinearLayout(Direction.HORIZONTAL));

        boutons.addComponent(new Button("Ajouter à mon panier", () -> {
            Panier.ajouterProduit(produit);
            Controller.showInfo(produit.getName() + " a été ajouté au panier !");
            popup.close();
        }));

        boutons.addComponent(new Button("Fermer", popup::close));

        panel.addComponent(boutons);
        popup.setComponent(panel);
        // Affiche la popup de manière modale et bloque l’exécution jusqu’à sa fermeture
        tgpr.framework.ViewManager.gui.addWindowAndWait(popup);
    }

    public void restaurantClosed(ClosingPeriod closingPeriod){
        navigateTo(new RestaurantClosedController(closingPeriod));
    }
}
