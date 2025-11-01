package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.framework.Controller;
import tgpr.restaurant.controller.PanierController;
import tgpr.restaurant.model.Panier;
import tgpr.restaurant.model.Product;

import java.util.Map;

public class PanierView extends BasicWindow {

    private final PanierController controller;
    private final Panel root;

    public PanierView(PanierController controller) {
        this.controller = controller;
        setTitle("Restaurant - Mon Panier");

        root = new Panel(new LinearLayout(Direction.VERTICAL));
        setComponent(root);

        afficherContenu();
    }

    private void afficherContenu() {
        root.removeAllComponents();

        root.addComponent(new Label("↑↓ pour naviguer, Entrée: modifier quantité, Suppr: supprimer"));
        root.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        root.addComponent(new Label("=== COMMANDE ==="));
        root.addComponent(new Label("Connexion requise pour passer commande"));
        root.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        if (Panier.estVide()) {
            root.addComponent(new Label("Votre panier est vide pour le moment."));
        } else {
            root.addComponent(new Label("=== ARTICLES ==="));
            root.addComponent(new EmptySpace(new TerminalSize(0, 1)));

            ActionListBox listBox = new ActionListBox(new TerminalSize(60, 10));
            int index = 1;

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
        Panel buttons = new Panel(new LinearLayout(Direction.HORIZONTAL));

        Button retour = new Button("Retour", controller::retour);
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
        Controller.showInfo("Vous devez vous connecter pour passer une commande !");
    }
}
