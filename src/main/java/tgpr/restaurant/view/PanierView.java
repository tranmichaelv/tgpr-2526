package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.framework.Controller;
import tgpr.restaurant.controller.PanierController;
import tgpr.restaurant.model.Panier;
import tgpr.restaurant.model.Product;
import tgpr.restaurant.model.Security;

import java.util.List;
import java.util.Map;

public class PanierView extends BasicWindow {

    private final PanierController controller;
    private final Panel root;

    public PanierView(PanierController controller) {
        this.controller = controller;
        setTitle("Restaurant - Mon Panier");

        setHints(List.of(Hint.CENTERED,Hint.MODAL));
        root = Panel.gridPanel(1, Margin.of(2,2,0,0));
        setComponent(root);

        afficherContenu();
    }

    private void afficherContenu() {
        root.removeAllComponents();

        root.addComponent(new Label("↑↓ pour naviguer, Entrée: modifier quantité, Suppr: supprimer"));
        root.addEmpty();
        root.addEmpty();

        if (Panier.estVide()) {
            root.addComponent(new Label("Votre panier est vide pour le moment."));
        } else {
            ActionListBox connection = new ActionListBox(new TerminalSize(60, 3));
            connection.addItem("=== COMMANDE ===", () -> {});
            if(!Security.isLogged()) {
                // si pas connecte alors
                connection.addItem("Connexion requise pour passer commande", this::connect);
            }
            root.addComponent(connection);

            ActionListBox listBox = new ActionListBox(new TerminalSize(60, 3));
            int index = 1;


            listBox.addItem("=== ARTICLES ===", () -> {
            });

            for (Map.Entry<Product, Integer> entry : Panier.getProduits().entrySet()) {
                Product produit = entry.getKey();
                int quantite = entry.getValue();
                double totalProduit = produit.getPrice() * quantite;

                String ligne = index++ + ". " + produit.getName() + " x" + quantite + " - " +
                        String.format("%.2f", totalProduit) + " €";

                listBox.addItem(ligne, () -> controller.modifierQuantite(produit));
            }

            root.addComponent(listBox);
            root.addComponent(new EmptySpace(new TerminalSize(0, 1)));
            root.addComponent(new Label("Total: " + String.format("%.2f", Panier.getTotal()) + " €"));
        }

        root.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        root.addComponent(creerBoutons());
    }

    private Panel creerBoutons() {
        Panel buttons = Panel.horizontalPanel();

        Button retour = new Button("Retour", this::back);
        buttons.addComponent(retour);

        if (!Panier.estVide()) {
            Button vider = new Button("Vider le panier", this::viderPanier);
            Button commander = new Button("Commander", this::commander);

            buttons.addComponent(vider);
            buttons.addComponent(commander);
        }

        return buttons;
    }

    private void viderPanier() {
        boolean confirmation = Controller.askConfirmation(
                "Voulez-vous vraiment vider votre panier ?",
                "Confirmation"
        );
        if (confirmation) {
            Panier.vider();
            Controller.showInfo("Le panier a été vidé !");
            this.close();
            Controller.navigateTo(new PanierController());
        }
    }

    private void commander() {
        if(Security.isLogged()){
            // si user connecte alors commande sinon mets le message
            Controller.showInfo("C'est bon !");
        } else {
            Controller.showInfo("Vous devez vous connecter pour passer une commande !");
        }
    }

    private void connect(){
        controller.connect();
    }

    private void back(){
        controller.back();
    }
}
