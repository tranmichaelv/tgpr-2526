package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderDetailsController;
import tgpr.restaurant.model.Order;

import java.time.format.DateTimeFormatter;

public class OrderDetailsView extends BasicWindow {
    private final OrderDetailsController controller;

    public OrderDetailsView(OrderDetailsController controller){
        this.controller = controller;

        setTitle("Détails de la commande");

        Order order = controller.getOrder(); // on récupère la commande sélectionnée
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Panel root = new Panel(new BorderLayout());

        Panel content = new Panel(new LinearLayout(Direction.VERTICAL));

        content.addComponent(new Label("Détails de la commande :"));
        content.addComponent(new EmptySpace(new TerminalSize(0,1)));

        content.addComponent(new Label("Client : " + order.getClient().getFirstName() + " " + order.getClient().getLastName()));
        content.addComponent(new Label("Date : " + order.getOrderDate().format(formatter)));
        content.addComponent(new Label("Table : " + (order.getTable() != null ? "Table " + order.getTable().getId() + " " + "(" + order.getTable().getCapacity() + "p)" : "Aucune")));
        content.addComponent(new Label("Service : " + order.getService().getName()));
        content.addComponent(new Label("Personnes effectives : " + order.getEffectivePeopleCount()));
        content.addComponent(new Label("Type : " + (order.getTakeAway() ? "À emporter" : "Sur place")));
        content.addComponent(new Label("En ligne : " + (order.getOnline() ? "Oui" : "Non")));
        content.addComponent(new EmptySpace(new TerminalSize(0,1)));

        Panel articlesPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        articlesPanel.addComponent(new Label("Articles :"));
        order.getOrderItems().forEach(item ->
                articlesPanel.addComponent(new Label(" - " + item.getProduct().getName() + " x" + item.getQuantity()))
        );
        content.addComponent(articlesPanel);
        content.addComponent(new EmptySpace(new TerminalSize(0,2)));

        content.addComponent(new Label("Sous-total : " + order.calculateSubTotal()));
        content.addComponent(new Label("Remise : " + order.calculateDiscount()));
        content.addComponent(new Label("Montant total : " + order.calculateTotal()));
        content.addComponent(new Label("Paiement : " + (order.getPaymentMethod() != null ? order.getPaymentMethod() : "En attente")));


        Panel bottomPanel = new Panel(new BorderLayout());
        Panel rightButtons = new Panel(new LinearLayout(Direction.HORIZONTAL));
        rightButtons.addComponent(new Button("OK", this::close));
        bottomPanel.addComponent(rightButtons, BorderLayout.Location.RIGHT);
        root.addComponent(bottomPanel, BorderLayout.Location.BOTTOM);

        root.addComponent(content, BorderLayout.Location.CENTER);
        setComponent(root);
    }
}
