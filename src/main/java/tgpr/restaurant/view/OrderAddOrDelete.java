package tgpr.restaurant.view;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.model.Order;

public abstract class OrderAddOrDelete<T> extends BasicWindow {

    protected final T controller;
    protected Order order;

    protected double sousTotal;
    protected double reduction;
    protected double total;

    protected OrderAddOrDelete(T controller,Order order, String title, String text, String buttonYes){
        this.controller = controller;
        this.order = order;
        setTitle(title);

        Panel root = new Panel(new BorderLayout());

        //  Contenu principal
        Panel content = new Panel(new LinearLayout(Direction.VERTICAL));
        content.addComponent(new Label(text));
        content.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        // Infos commande
        content.addComponent(new Label("Client:"  + " " + order.getClient().getFirstName() + " " + order.getClient().getLastName() ));
        content.addComponent(new Label("Table:" + (order.getTable() != null ? " "+ "Table" + " " +  order.getTable().getId() : "Aucune")));
        content.addComponent(new Label("Service:" + " " + order.getService().getName()));
        content.addComponent(new Label("Personnes effectives: " + " " + order.getEffectivePeopleCount()));
        content.addComponent(new EmptySpace(new TerminalSize(0, 1)));


        content.addComponent(new Label(buildRecap()));
        content.addComponent(new EmptySpace());

        root.addComponent(content, BorderLayout.Location.CENTER);

        // boutons yes no
        Panel bottomPanel = new Panel(new BorderLayout());
        Panel rightButtons = new Panel(new LinearLayout(Direction.HORIZONTAL));

        rightButtons.addComponent(new Button(buttonYes, this::onYes));
        rightButtons.addComponent(new Button("No", this::close));

        bottomPanel.addComponent(rightButtons, BorderLayout.Location.RIGHT);
        root.addComponent(bottomPanel, BorderLayout.Location.BOTTOM);

        setComponent(root);
    }

    // Affichage pour le prix
    protected String buildRecap() {
        if (order == null) {
            return "Sous-total: 0.00 €  -  Réduction: 0.00 €  =  Total: 0.00 €";
        }

        double sousTotalCalc = order.getOrderItems().stream()
                .mapToDouble(oi -> oi.getProduct().getPrice() * oi.getQuantity())
                .sum();

        double reductionCalc = order.getDiscount() != null ? order.getDiscount() : 0.0;
        double totalCalc = sousTotalCalc - reductionCalc;

        this.sousTotal = sousTotalCalc;
        this.reduction = reductionCalc;
        this.total = totalCalc;

        return String.format(
                "Sous-total: %.2f €  -  Réduction: %.2f €  =  Total: %.2f €",
                sousTotalCalc, reductionCalc, totalCalc
        );
    }

    // méthode pour mon bouton Yes
    protected abstract void onYes();
}