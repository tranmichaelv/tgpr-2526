package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderDeleteController;

public class OrderDeleteView extends BasicWindow {
    private final OrderDeleteController controller;

    private double sousTotal;
    private double reduction;
    private double total;

    public OrderDeleteView(OrderDeleteController controller){
        this.controller = controller;

        setTitle("Annulation");

        Panel root = new Panel(new BorderLayout());

        // Informations de la commande
        Panel content = new Panel(new LinearLayout(Direction.VERTICAL));
        content.addComponent(new Label("Voulez-vous annulez cette commande ?"));
        content.addComponent(new EmptySpace(new TerminalSize(0,1)));

        content.addComponent(new Label("Client:"));
        content.addComponent(new Label("Table:"));
        content.addComponent(new Label("Service:"));
        content.addComponent(new Label("Personnes effectives:"));
        content.addComponent(new EmptySpace(new TerminalSize(0,1)));

        // affichage du prix
        String recap = String.format("Sous-total: %.2f €  -  Réduction: %.2f €  =  Total: %.2f €",
                sousTotal, reduction, total);
        content.addComponent(new Label(recap));
        content.addComponent(new EmptySpace());

        root.addComponent(content, BorderLayout.Location.CENTER);

        // Boutton à droite
        Panel bottomPanel = new Panel(new BorderLayout());
        Panel rightButtons = new Panel(new LinearLayout(Direction.HORIZONTAL));

        rightButtons.addComponent(new Button("Yes")); // supprime la commande de la BD
        rightButtons.addComponent(new Button("No", this::close));

        bottomPanel.addComponent(rightButtons, BorderLayout.Location.RIGHT);
        root.addComponent(bottomPanel, BorderLayout.Location.BOTTOM);

        setComponent(root);
    }
}