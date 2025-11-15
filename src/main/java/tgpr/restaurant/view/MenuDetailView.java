package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.restaurant.controller.MenuDetailController;
import tgpr.restaurant.model.MenuItem;
import tgpr.restaurant.model.Product;

import java.util.List;

public class MenuDetailView extends DialogWindow {
    private final MenuDetailController controller;
    private final Product produit;

    public MenuDetailView(MenuDetailController controller, Product produit) {
        super("Restaurant - Détails du menu");
        this.controller = controller;
        this.produit = produit;

        setHints(List.of(Hint.CENTERED, Hint.MODAL));
        setCloseWindowWithEscape(true);

        Panel root = Panel.gridPanel(1,
                Margin.of(2,1,0,0),
                Spacing.of(1));
        setComponent(root);

        root.addComponent(createText());
        root.addComponent(createBody());
        root.addComponent(createPrice());
        root.addComponent(createButton());
    }

    private Panel createText(){
        Panel panel = Panel.verticalPanel();

        // --- Informations générales sur le menu ---
        panel.addComponent(new Label("MENU: " + produit.getName()));
        panel.addEmpty();
        panel.addEmpty();
        if (produit.getDescription() != null && !produit.getDescription().isBlank()) {
            panel.addComponent(new Label(produit.getDescription()));
            panel.addEmpty();
        }
        panel.addComponent(new Label("    Prix du menu: " + String.format("%.2f", produit.getPrice()) + " €"));
        panel.addComponent(new Label("       ECONOMIE: " + String.format("%.2f", produit.getEconomy()) + " €"));
        panel.addEmpty();
        panel.addEmpty();
        panel.addComponent(new Label("        Plats inclus dans ce menu :"));

        return panel;
    }

    private ActionListBox createBody() {
        // --- Liste des plats récupérés depuis la base de données ---
        ActionListBox listBox = new ActionListBox(new TerminalSize(45, 10));
        var items = produit.getMenuMenuItems();

        if (items == null || items.isEmpty()) {
            listBox.addItem("   Aucun élément trouvé dans ce menu.", () -> {
            });
        } else {

            // === ENTRÉES ===
            listBox.addItem("=== ENTRÉES ===", () -> {
            });
            for (MenuItem item : items) {
                Product p = item.getProduct();
                if (p.getType() == Product.Type.Starter) {
                    listBox.addItem("  • " + p.getName() + " - " + String.format("%.2f", p.getPrice()) + " €", () -> {
                    });
                    if (p.getDescription() != null && !p.getDescription().isBlank())
                        listBox.addItem("    " + p.getDescription(), () -> {
                        });
                }
            }

            // === PLATS PRINCIPAUX ===
            listBox.addItem("=== PLATS PRINCIPAUX ===", () -> {
            });
            for (MenuItem item : items) {
                Product p = item.getProduct();
                if (p.getType() == Product.Type.Main) {
                    listBox.addItem("  • " + p.getName() + " - " + String.format("%.2f", p.getPrice()) + " €", () -> {
                    });
                    if (p.getDescription() != null && !p.getDescription().isBlank())
                        listBox.addItem("    " + p.getDescription(), () -> {
                        });
                }
            }

            // === DESSERTS ===
            listBox.addItem("=== DESSERTS ===", () -> {
            });
            for (MenuItem item : items) {
                Product p = item.getProduct();
                if (p.getType() == Product.Type.Dessert) {
                    listBox.addItem("  • " + p.getName() + " - " + String.format("%.2f", p.getPrice()) + " €", () -> {
                    });
                    if (p.getDescription() != null && !p.getDescription().isBlank())
                        listBox.addItem("    " + p.getDescription(), () -> {
                        });
                }
            }
        }
        return listBox;
    }

    private Panel createPrice(){

        Panel panel = new Panel();

        panel.addComponent(new Label("―".repeat(40)));

        // --- Totaux dynamiques ---
        double totalPlats = produit.getTotalPrice();
        double economie = produit.getEconomy();

        panel.addComponent(new Label("Total des plats à l’unité : " + String.format("%.2f", totalPlats) + " €"));
        panel.addComponent(new Label("Économie réalisée : " + String.format("%.2f", economie) + " €"));

        return panel;
    }

    // --- Boutons d’action ---
    private Panel createButton(){
        Panel buttons = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttons.addComponent(new Button("Ajouter à mon panier", controller::ajouterAuPanier));
        buttons.addComponent(new Button("Mon Panier", controller::ouvrirPanier));
        buttons.addComponent(new Button("Retour", this::close));
        return buttons;
    }
}
