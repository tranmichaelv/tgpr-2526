package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.MenuController;
import tgpr.restaurant.model.Product;

import java.util.List;

public class MenuView extends BasicWindow {

    private final MenuController controller;

    public MenuView(MenuController controller) {
        this.controller = controller;
        setTitle("Restaurant - Carte du restaurant");

        setHints(List.of(Hint.CENTERED, Hint.MODAL));

        Panel root = Panel.gridPanel(1,
                Margin.of(2, 0, 5, 0),
                Spacing.of(1));
        setComponent(root);

        root.addComponent(createHeader());
        root.addComponent(createMenu());
        root.addComponent(createButton());
    }

    private Panel createHeader(){
        Panel header = new Panel(new LinearLayout(Direction.VERTICAL));
        header.addComponent(new Label("↑↓ pour naviguer, Entrée: menus→détails, autres→description"));
        header.addEmpty();
        return header;
    }

    private ActionListBox createMenu(){
        ActionListBox listBox = new ActionListBox(new TerminalSize(60, 20));
        // === MENUS ===
        int cpt = 0;
        listBox.addItem("=== MENUS ===", () -> {
        });
        List<Product> menus = Product.getAll().stream()
                .filter(p -> p.getType() == Product.Type.Menu && !p.getArchived())
                .toList();

        for (Product menu : menus) {
            cpt += 1;
            listBox.addItem("  " + cpt + ". " + menu.getName() + " - " + String.format("%.2f", menu.getPrice()) + " €",
                    () -> controller.afficherDetailsMenu(menu));
        }

        // === ENTRÉES ===
        cpt = 0;
        listBox.addItem(" ", () -> {
        });
        listBox.addItem("=== ENTRÉES ===", () -> {
        });
        List<Product> entrees = Product.getAll().stream()
                .filter(p -> p.getType() == Product.Type.Starter && !p.getArchived())
                .toList();

        for (Product entree : entrees) {
            cpt += 1;
            listBox.addItem("  " + cpt + ". " + entree.getName() + " - " + String.format("%.2f", entree.getPrice()) + " €",
                    () -> controller.afficherPopupProduit(entree));
        }

        // === PLATS PRINCIPAUX ===
        cpt = 0;
        listBox.addItem(" ", () -> {
        });
        listBox.addItem("=== PLATS PRINCIPAUX ===", () -> {
        });
        List<Product> plats = Product.getAll().stream()
                .filter(p -> p.getType() == Product.Type.Main && !p.getArchived())
                .toList();

        for (Product plat : plats) {
            cpt += 1;
            listBox.addItem("  " + cpt + ". " + plat.getName() + " - " + String.format("%.2f", plat.getPrice()) + " €",
                    () -> controller.afficherPopupProduit(plat));
        }

        // === DESSERTS ===
        cpt = 0;
        listBox.addItem(" ", () -> {
        });
        listBox.addItem("=== DESSERTS ===", () -> {
        });
        List<Product> desserts = Product.getAll().stream()
                .filter(p -> p.getType() == Product.Type.Dessert && !p.getArchived())
                .toList();

        for (Product dessert : desserts) {
            cpt += 1;
            listBox.addItem("  " + cpt + ". " + dessert.getName() + " - " + String.format("%.2f", dessert.getPrice()) + " €",
                    () -> controller.afficherPopupProduit(dessert));
        }

        // === BOISSONS ===
        cpt = 0;
        listBox.addItem(" ", () -> {
        });
        listBox.addItem("=== BOISSONS ===", () -> {
        });
        List<Product> boissons = Product.getAll().stream()
                .filter(p -> p.getType() == Product.Type.Drink && !p.getArchived())
                .toList();

        for (Product boisson : boissons) {
            cpt += 1;
            listBox.addItem("  " + cpt + ". " + boisson.getName() + " - " + String.format("%.2f", boisson.getPrice()) + " €",
                    () -> controller.afficherPopupProduit(boisson));
        }

        return listBox;
    }

    // --- BOUTONS ---
    private Panel createButton(){
        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Mon Panier", controller::ouvrirPanier));
        buttonPanel.addComponent(new Button("Retour", this::back));

        return buttonPanel;
    }

    private void back(){
        controller.back();
    }
}
