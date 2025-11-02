package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.MenuDetailController;
import tgpr.restaurant.model.MenuItem;
import tgpr.restaurant.model.Product;

import java.util.List;

public class MenuDetailView extends BasicWindow {

    private final MenuDetailController controller;
    private final Product produit;

    public MenuDetailView(MenuDetailController controller, Product produit) {
        super("Restaurant - Détails du menu");
        this.controller = controller;
        this.produit = produit;

        setHints(List.of(Hint.CENTERED));

        Panel root = new Panel(new LinearLayout(Direction.VERTICAL));
        setComponent(root);

        // --- Informations générales sur le menu ---
        root.addComponent(new Label("  MENU: " + produit.getName()));
        if (produit.getDescription() != null && !produit.getDescription().isBlank()) {
            root.addComponent(new Label("  " + produit.getDescription()));
        }
        root.addComponent(new EmptySpace());
        root.addComponent(new Label("      Prix du menu: " + String.format("%.2f", produit.getPrice()) + " €"));
        root.addComponent(new EmptySpace());
        root.addComponent(new Label("          Plats inclus dans ce menu :"));

        // --- Liste des plats récupérés depuis la base de données ---
        ActionListBox listBox = new ActionListBox(new TerminalSize(60, 12));
        var items = produit.getMenuMenuItems();

        if (items == null || items.isEmpty()) {
            listBox.addItem("   Aucun élément trouvé dans ce menu.", () -> {});
        } else {

            // === ENTRÉES ===
            listBox.addItem("  === ENTRÉES ===", () -> {});
            for (MenuItem item : items) {
                Product p = item.getProduct();
                if (p.getType() == Product.Type.Starter) {
                    listBox.addItem("   " + p.getName() + " - " + String.format("%.2f", p.getPrice()) + " €", () -> {});
                    if (p.getDescription() != null && !p.getDescription().isBlank())
                        listBox.addItem("     " + p.getDescription(), () -> {});
                }
            }

            // === PLATS PRINCIPAUX ===
            listBox.addItem("  === PLATS PRINCIPAUX ===", () -> {});
            for (MenuItem item : items) {
                Product p = item.getProduct();
                if (p.getType() == Product.Type.Main) {
                    listBox.addItem("   " + p.getName() + " - " + String.format("%.2f", p.getPrice()) + " €", () -> {});
                    if (p.getDescription() != null && !p.getDescription().isBlank())
                        listBox.addItem("     " + p.getDescription(), () -> {});
                }
            }

            // === DESSERTS ===
            listBox.addItem("  === DESSERTS ===", () -> {});
            for (MenuItem item : items) {
                Product p = item.getProduct();
                if (p.getType() == Product.Type.Dessert) {
                    listBox.addItem("   " + p.getName() + " - " + String.format("%.2f", p.getPrice()) + " €", () -> {});
                    if (p.getDescription() != null && !p.getDescription().isBlank())
                        listBox.addItem("     " + p.getDescription(), () -> {});
                }
            }
        }

        root.addComponent(listBox);
        new EmptySpace().addTo(root);
        root.addComponent(new Label("  ______________________________________"));

        // --- Totaux dynamiques ---
        double totalPlats = produit.getTotalPrice();
        double economie = produit.getEconomy();

        root.addComponent(new Label("  Total des plats à l’unité : " + String.format("%.2f", totalPlats) + " €"));
        root.addComponent(new Label("  Économie réalisée : " + String.format("%.2f", economie) + " €"));

        new EmptySpace().addTo(root);

        // --- Boutons d’action ---
        Panel buttons = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttons.addComponent(new Button("Ajouter à mon panier", controller::ajouterAuPanier));
        buttons.addComponent(new Button("Mon Panier", controller::ouvrirPanier));
        buttons.addComponent(new Button("Retour", controller::retour));
        root.addComponent(buttons);
    }
}
