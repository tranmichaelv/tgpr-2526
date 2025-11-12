package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.ClosingPeriod;
import tgpr.restaurant.model.Panier;
import tgpr.restaurant.model.Product;
import tgpr.restaurant.model.Time;
import tgpr.restaurant.view.MenuDetailView;

public class MenuDetailController extends Controller<MenuDetailView> {

    private final Product produit;
    ClosingPeriod closingPeriod =  Time.restaurantIsClosed();

    public MenuDetailController(Product produit) {
        this.produit = produit;
    }
    public void ouvrirPanier() {
        if(closingPeriod != null ){
            navigateTo(new RestaurantClosedController(closingPeriod));
        } else {
            navigateTo(new PanierController());
        }
    }

    @Override
    public MenuDetailView getView() {
        return new MenuDetailView(this, produit);
    }

    // Ajouter le menu au panier
    public void ajouterAuPanier() {
        if(closingPeriod != null ){
            navigateTo(new RestaurantClosedController(closingPeriod));
        } else {
            Panier.ajouterProduit(produit);
            Controller.showInfo(produit.getName() + " a été ajouté au panier !");
        }
    }
}
