package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderDetailsController;

public class OrderDetailsView extends BasicWindow {
    private final OrderDetailsController controller;

    public OrderDetailsView (OrderDetailsController controller){
        this.controller = controller;

        setTitle("Détails de la commande");

        Panel root = new Panel(new BorderLayout());

        // Panneau principal vertical
        Panel content = new Panel(new LinearLayout(Direction.VERTICAL));
        content.addComponent(new Label("Détails de la commande:"));
        content.addComponent(new EmptySpace(new TerminalSize(0,1)));
         // Detail de la commande
        content.addComponent(new Label("Client:"));
        content.addComponent(new Label("Date:"));
        content.addComponent(new Label("Table:"));
        content.addComponent(new Label("Service:"));
        content.addComponent(new Label("Personnes:"));
        content.addComponent(new Label("Type"));
        content.addComponent(new Label("En ligne:"));
        content.addComponent(new EmptySpace(new TerminalSize(0,1)));

        // Panneau pour les articles seulement
        Panel article = new Panel(new LinearLayout(Direction.VERTICAL));
        article.addComponent(new Label("Articles:"));
        // afficher les articles
        content.addComponent(article);
        content.addComponent(new EmptySpace(new TerminalSize(0,2)));

        // Information paiement
        content.addComponent(new Label("Sous-total:"));
        content.addComponent(new Label("Remise"));
        content.addComponent(new Label("Montant total"));
        content.addComponent(new Label("Paiement"));

        // ajoute le contenu au centre
        root.addComponent(content, BorderLayout.Location.CENTER);

        // Bouton à droite
        Panel bottomPanel = new Panel(new BorderLayout());
        Panel rightButtons = new Panel(new LinearLayout(Direction.HORIZONTAL));

        rightButtons.addComponent(new Button("Ok", this::close));

        bottomPanel.addComponent(rightButtons, BorderLayout.Location.RIGHT);
        root.addComponent(bottomPanel, BorderLayout.Location.BOTTOM);

        setComponent(root);

    }

}
