package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderCreateController;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.model.Service;
import tgpr.restaurant.model.Table;
import tgpr.restaurant.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderChangeController;

public class OrderChangeView extends BasicWindow{

    private final OrderChangeController controller;

    public OrderChangeView(OrderChangeController controller){
        this.controller = controller;


//        setTitle("Panel pour construire tableau de commandes");
        setTitle("Modifier une commande");

        Panel root = new Panel();

        root.addComponent(new EmptySpace());
        root.addComponent(new EmptySpace());

        Panel infoSection = new Panel(new LinearLayout(Direction.VERTICAL));
        root.addComponent(sectionHeader("Informations de la commande:"));
        infoSection.addComponent(new EmptySpace(new TerminalSize(0,1)));
        infoSection.addComponent(new Label("Client : "));

        // récupérer les donnés des client de la base de données

        infoSection.addComponent(new Label("Table : "));

        infoSection.addComponent(new Label("Service : "));

        infoSection.addComponent(new Label("A emporter "));

        infoSection.addComponent(new Label("Nombre de personnes : "));

        infoSection.addComponent(new Label("Réduction (€): "));

        infoSection.addComponent(new EmptySpace(new TerminalSize(0,1)));

        Label serviceLabel = new Label("Services disponibles:");
        serviceLabel.addStyle(SGR.ITALIC);
        infoSection.addComponent(serviceLabel);
        infoSection.addComponent(new EmptySpace(new TerminalSize(0,1)));

        Panel productsSection = new Panel(new LinearLayout(Direction.VERTICAL));
        productsSection.addComponent(sectionHeader("Produits disponibles:"));
        productsSection.addComponent(new EmptySpace(new TerminalSize(0,1)));

        Panel ligneProducts = new Panel(new LinearLayout(Direction.HORIZONTAL));
        ligneProducts.addComponent(emphasize("Nom              "));
        ligneProducts.addComponent(emphasize("Type      "));
        ligneProducts.addComponent(emphasize("Prix       "));
        ligneProducts.addComponent(emphasize("Description         "));

        productsSection.addComponent(ligneProducts);

        productsSection.addComponent(new EmptySpace(new TerminalSize(0,1)));

        Panel articles = new Panel(new LinearLayout(Direction.VERTICAL));
        articles.addComponent(sectionHeader("Articles de la commande:"));
        articles.addComponent(new EmptySpace(new TerminalSize(0,1)));


        double sousTotal = controller.getSousTotal();
        double reduction = controller.getReduction();
        double total = controller.getTotal();

        articles.addComponent(priceSummary(sousTotal, reduction, total));

        Panel ligneArticles = new Panel(new LinearLayout(Direction.HORIZONTAL));
        ligneArticles.addComponent(emphasize("Type     "));
        ligneArticles.addComponent(emphasize("Produit         "));
        ligneArticles.addComponent(emphasize("Quantité"));
        ligneArticles.addComponent(emphasize("Prix unitaire"));
        ligneArticles.addComponent(emphasize("Total"));

        articles.addComponent(ligneArticles);


        articles.addComponent(new EmptySpace(new TerminalSize(0, 4)));

        Panel buttons = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttons.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        buttons.addComponent(new Button("Modifier la commande")); // ouvre vue de confirmation screenshot 23
        buttons.addComponent(new EmptySpace(new TerminalSize(1, 0)));
        buttons.addComponent(new Button("Annuler")); // dirige vers la vue order_delete
        buttons.addComponent(new EmptySpace(new TerminalSize(1, 0)));
        buttons.addComponent(new Button("Retour", this::close));


        infoSection.addTo(root);
        productsSection.addTo(root);
        articles.addTo(root);
        buttons.addTo(root);

        setComponent(root);

    }
    // méthode couleur + style css
    private Component sectionHeader(String title) {
        Label header = new Label(title);
        header.addStyle(SGR.BOLD);
        header.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        return header;
    }

    // méthode qui souligne + style css
    private Component emphasize(String title) {
        Panel headerPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        // Label principal (gras + couleur)
        Label header = new Label(title);
        header.addStyle(SGR.BOLD);
        header.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);

        // Soulignement
        String underline = title.replaceAll(".", "─");

        Label underlineLabel = new Label(underline);

        // Assemblage vertical
        headerPanel.addComponent(header);
        headerPanel.addComponent(underlineLabel);

        return headerPanel;
    }
    // méthode qui fait le calcul du prix de la commande + style css
    private Component priceSummary(double sousTotal, double reduction, double total) {
        Panel summary = new Panel(new LinearLayout(Direction.HORIZONTAL));

        String texte = String.format(
                "Sous-total: %.2f €  -  Réduction: %.2f €  =  Total: %.2f €",
                sousTotal, reduction, total
        );

        Label lbl = new Label(texte);
        lbl.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        lbl.addStyle(SGR.BOLD);

        summary.addComponent(lbl);
        return summary;
    }

}