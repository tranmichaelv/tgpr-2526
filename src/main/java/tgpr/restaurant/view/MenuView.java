package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.MenuController;
import tgpr.restaurant.model.Product;

public class MenuView extends BasicWindow {

    private final MenuController controller;

    public MenuView(MenuController controller) {
        this.controller = controller;
        setTitle("Restaurant - Carte du restaurant");

        Panel root = new Panel(new LinearLayout(Direction.VERTICAL));

        Panel pour = new Panel(new LinearLayout(Direction.VERTICAL));
        pour.addComponent(new Label("↑↓ pour naviguer, Entrée: menus→détails, autres→ description"));
        pour.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        root.addComponent(pour);

        ActionListBox listBox = new ActionListBox(new TerminalSize(60, 20));
        listBox.addItem("  === MENUS ===", () -> {});
        listBox.addItem("  Menu classique - 25.00 €", () -> {
            Product menu = new Product("Menu classique", 25.00, Product.Type.Menu);
            //controller.afficherDetailsMenu(menu);
        });

        listBox.addItem("  Menu végétarien - 23.00 €", () -> {
            Product menu = new Product("Menu végétarien", 23.00, Product.Type.Menu);
//            controller.afficherDetailsMenu(menu);
        });
        listBox.addItem("  Menu enfant - 12.00 €", () -> {
            Product menu = new Product("Menu végétarien", 12.00, Product.Type.Menu);
            //controller.afficherDetailsMenu(menu);
        });
        listBox.addItem(" ", () -> {});
        listBox.addItem("  === ENTRÉES ===", () -> {});

        listBox.addItem("  Salade César - 8.50 €", () -> {
            Product salade = new Product("Salade César", 8.50, Product.Type.Starter);
            controller.afficherPopupProduit(salade);
        });

        listBox.addItem("  Soupe du jour - 6.00 €", () -> {
            Product salade = new Product("Salade du jour", 6.00, Product.Type.Starter);
            controller.afficherPopupProduit(salade);
        });

        listBox.addItem("  Bruschetta - 7.00 €", () -> {
            Product salade = new Product("Bruschetta", 7.00, Product.Type.Starter);
            controller.afficherPopupProduit(salade);
        });
        listBox.addItem(" ", () -> {});

        listBox.addItem("  === PLATS PRINCIPAUX ===", () -> {});

        listBox.addItem("  Steak frites - 18.00 €", () -> {
            Product salade = new Product("Steak frites", 18.00, Product.Type.Starter);
            controller.afficherPopupProduit(salade);
        });

        listBox.addItem("  Poulet rôti - 16.50 €", () -> {
            Product salade = new Product("Poulet rôti", 16.50, Product.Type.Starter);
            controller.afficherPopupProduit(salade);
        });

        listBox.addItem("  Lasagnes - 15.00 €", () -> {
            Product salade = new Product("Lasagnes", 15.00, Product.Type.Starter);
            controller.afficherPopupProduit(salade);
        });

        listBox.addItem("  Lasagnes végétariennes - 15.00 €", () -> {
            Product salade = new Product("Lasagnes végétariennes", 15.00, Product.Type.Starter);
            controller.afficherPopupProduit(salade);
        });
        listBox.addItem("  Spaghetti bolognaise - 10.00 €", () -> {
            Product salade = new Product("Spaghetti bolognaise", 10.00, Product.Type.Starter);
            controller.afficherPopupProduit(salade);
        });

        listBox.addItem(" ", () -> {});
        listBox.addItem("  === DESSERTS ===", () -> {});
        listBox.addItem("  Tiramisu - 6.50 €", () -> {
            Product salade = new Product("Tiramisu", 6.50, Product.Type.Starter);
            controller.afficherPopupProduit(salade);
        });

        listBox.addItem("  Mousse au chocolat - 5.50 €", () -> {
            Product salade = new Product("Mousse au chocolat", 5.50, Product.Type.Starter);
            controller.afficherPopupProduit(salade);
        });

        listBox.addItem("  Crème brûlée - 6.00 €", () -> {
            Product salade = new Product("Crème brûlée", 6.00, Product.Type.Starter);
            controller.afficherPopupProduit(salade);
        });

        listBox.addItem("  Salade de fruits - 5.50 €", () -> {
            Product salade = new Product("Salade de fruits", 5.50, Product.Type.Starter);
            controller.afficherPopupProduit(salade);
        });

        listBox.addItem(" ", () -> {});
        listBox.addItem("  === BOISSONS ===", () -> {});
        listBox.addItem("  Coca-Cola - 2.50 €", () -> {
            Product salade = new Product("Coca-Cola", 2.50, Product.Type.Starter);
            controller.afficherPopupProduit(salade);
        });



        root.addComponent(listBox);

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Mon Panier", controller::getView));     //ouvrirPanier
        buttonPanel.addComponent(new Button("Retour", this::close));
        root.addComponent(buttonPanel);

        setComponent(root);
    }
}
