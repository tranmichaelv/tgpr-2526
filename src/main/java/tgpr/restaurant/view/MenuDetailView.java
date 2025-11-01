package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.MenuDetailController;
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

        root.addComponent(new Label("  MENU: " + produit.getName()));
        root.addComponent(new Label("  Entrée, plat et dessert au choix"));
        root.addComponent(new EmptySpace());
        root.addComponent(new Label("      Prix du menu: " + produit.getPrice() + " €"));
        root.addComponent(new Label("         ÉCONOMIE: 8.00 €"));
        root.addComponent(new EmptySpace());
        root.addComponent(new Label("          Plats inclus dans ce menu :"));

        ActionListBox listBox = new ActionListBox(new TerminalSize(60, 10));
        listBox.addItem("  === ENTRÉES ===", () -> {});
        listBox.addItem("   Salade César - 8.50 €", () -> {});
        listBox.addItem("   Salade avec poulet, croutons et parmesan", () -> {});
        listBox.addItem("  === PLATS PRINCIPAUX ===", () -> {});
        listBox.addItem("   Steak frites - 18.00 €", () -> {});
        listBox.addItem("   Steck grillé servi avec frites ", () -> {});
        listBox.addItem("  === DESSERTS ===", () -> {});
        listBox.addItem("   Tiramisu - 6.50 €", () -> {});
        listBox.addItem("   Dessert italien au café et mascarpone", () -> {});
        root.addComponent(listBox);

        new EmptySpace().addTo(root);
        root.addComponent(new Label("  ______________________________________"));



        // probleme
        double totalPlats = produit.getTotalPrice();
        double economie = produit.getEconomy();

        // Calcul du total et de l’économie selon le menu choisi
//        double totalPlats;
//        double economie;
//
//        switch (produit.getName().toLowerCase().trim()) {
//            case "menu classique":
//                totalPlats = 33.00;
//                break;
//            case "menu végétarien":
//                totalPlats = 27.50;
//                break;
//            case "menu enfant":
//                totalPlats = 15.50;
//                break;
//            default:
//                totalPlats = produit.getPrice(); // fallback (au cas où)
//        }
//
//        economie = totalPlats - produit.getPrice();




        root.addComponent(new Label("  Total des plats à l’unité: " + String.format("%.2f", totalPlats) + " €"));
        root.addComponent(new Label("  Économie réalisée: " + String.format("%.2f", economie) + " €"));

        new EmptySpace().addTo(root);

        Panel buttons = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttons.addComponent(new Button("Ajouter à mon panier",
                controller::ajouterAuPanier));
        buttons.addComponent(new Button("Mon Panier", controller::ouvrirPanier));
        buttons.addComponent(new Button("Retour", controller::retour));
        root.addComponent(buttons);
    }

}
