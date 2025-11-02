package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.MenuController;
import tgpr.restaurant.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class MenuView extends BasicWindow {

    private final MenuController controller;

    public MenuView(MenuController controller) {
        this.controller = controller;
        setTitle("Restaurant - Carte du restaurant");

        Panel root = new Panel(new LinearLayout(Direction.VERTICAL));

        Panel header = new Panel(new LinearLayout(Direction.VERTICAL));
        header.addComponent(new Label("↑↓ pour naviguer, Entrée: menus→détails, autres→description"));
        header.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        root.addComponent(header);

        ActionListBox listBox = new ActionListBox(new TerminalSize(60, 20));

        // === MENUS ===
        listBox.addItem("  === MENUS ===", () -> {});
        List<Product> menus = Product.getAll().stream()
                .filter(p -> p.getType() == Product.Type.Menu && !p.getArchived())
                .collect(Collectors.toList());

        for (Product menu : menus) {
            listBox.addItem("  " + menu.getName() + " - " + String.format("%.2f", menu.getPrice()) + " €",
                    () -> controller.afficherDetailsMenu(menu));
        }

        // === ENTRÉES ===
        listBox.addItem(" ", () -> {});
        listBox.addItem("  === ENTRÉES ===", () -> {});
        List<Product> entrees = Product.getAll().stream()
                .filter(p -> p.getType() == Product.Type.Starter && !p.getArchived())
                .collect(Collectors.toList());

        for (Product entree : entrees) {
            listBox.addItem("  " + entree.getName() + " - " + String.format("%.2f", entree.getPrice()) + " €",
                    () -> controller.afficherPopupProduit(entree));
        }

        // === PLATS PRINCIPAUX ===
        listBox.addItem(" ", () -> {});
        listBox.addItem("  === PLATS PRINCIPAUX ===", () -> {});
        List<Product> plats = Product.getAll().stream()
                .filter(p -> p.getType() == Product.Type.Main && !p.getArchived())
                .collect(Collectors.toList());

        for (Product plat : plats) {
            listBox.addItem("  " + plat.getName() + " - " + String.format("%.2f", plat.getPrice()) + " €",
                    () -> controller.afficherPopupProduit(plat));
        }

        // === DESSERTS ===
        listBox.addItem(" ", () -> {});
        listBox.addItem("  === DESSERTS ===", () -> {});
        List<Product> desserts = Product.getAll().stream()
                .filter(p -> p.getType() == Product.Type.Dessert && !p.getArchived())
                .collect(Collectors.toList());

        for (Product dessert : desserts) {
            listBox.addItem("  " + dessert.getName() + " - " + String.format("%.2f", dessert.getPrice()) + " €",
                    () -> controller.afficherPopupProduit(dessert));
        }

        // === BOISSONS ===
        listBox.addItem(" ", () -> {});
        listBox.addItem("  === BOISSONS ===", () -> {});
        List<Product> boissons = Product.getAll().stream()
                .filter(p -> p.getType() == Product.Type.Drink && !p.getArchived())
                .collect(Collectors.toList());

        for (Product boisson : boissons) {
            listBox.addItem("  " + boisson.getName() + " - " + String.format("%.2f", boisson.getPrice()) + " €",
                    () -> controller.afficherPopupProduit(boisson));
        }

        root.addComponent(listBox);

        // --- BOUTONS ---
        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Mon Panier", controller::ouvrirPanier));
        buttonPanel.addComponent(new Button("Retour", this::close));
        root.addComponent(buttonPanel);

        setComponent(root);
    }
}
