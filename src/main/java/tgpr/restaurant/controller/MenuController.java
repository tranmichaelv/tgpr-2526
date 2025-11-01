package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.Product;
import tgpr.restaurant.view.MenuView;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.model.Panier;


public class MenuController extends Controller<MenuView> {

    @Override
    public MenuView getView(){
        return new MenuView(this);
    }

    public void afficherDetailsMenu(Product produit) {
        navigateTo(new MenuDetailController(produit));
    }


    public void ouvrirPanier() {
        navigateTo(new PanierController());
    }


    public void afficherPopupProduit(Product produit) {
        BasicWindow popup = new BasicWindow(produit.getName());
        popup.setHints(java.util.List.of(Window.Hint.CENTERED));

        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));
        panel.addComponent(new Label(produit.getDescription() != null
                ? produit.getDescription()
                : produit.getName()));
        panel.addComponent(new EmptySpace());
        panel.addComponent(new Label("Prix: " + produit.getPrice() + " €"));
        panel.addComponent(new EmptySpace());

        Panel boutons = new Panel(new LinearLayout(Direction.HORIZONTAL));
        boutons.addComponent(new Button("Ajouter à mon panier", () -> {
            Panier.ajouterProduit(produit);
            Controller.showInfo(produit.getName() + " ajouté au panier !");
            popup.close();
        }));
        boutons.addComponent(new Button("Fermer", popup::close));

        panel.addComponent(boutons);
        popup.setComponent(panel);

        tgpr.framework.ViewManager.showModal(popup);
    }






}
