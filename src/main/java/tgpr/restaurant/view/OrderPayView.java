package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderPayController;
import tgpr.restaurant.model.Order;

import java.time.format.DateTimeFormatter;

public class OrderPayView extends BasicWindow {

    private final OrderPayController controller;

    public OrderPayView(OrderPayController controller) {
        this.controller = controller;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Order order = controller.getOrder(); // on récupère la commande sélectionnée
        setTitle("Enregistrer le paiement");

        Panel root = new Panel(new LinearLayout(Direction.VERTICAL));
        root.addComponent(new EmptySpace());
        root.addComponent(new EmptySpace());

        root.addComponent(sectionHeader("Informations de la commande:"));
        root.addComponent(new EmptySpace());
        Panel info = new Panel(new GridLayout(2));
        // ajout des informations de la commande
        info.addComponent(new Label("Client : " + order.getClient().getFirstName() + " " + order.getClient().getLastName()));
        info.addComponent(new EmptySpace(new TerminalSize(0,1)));
        info.addComponent(new Label("Date : " + order.getOrderDate().format(formatter)));
        info.addComponent(new EmptySpace(new TerminalSize(0,1)));
        info.addComponent(new Label("Table : " + (order.getTable() != null ? "Table " + order.getTable().getId() + " " + "(" + order.getTable().getCapacity() + "p)" : "Aucune")));
        info.addComponent(new EmptySpace(new TerminalSize(0,1)));
        info.addComponent(new Label("Service : " + order.getService().getName()));
        info.addComponent(new EmptySpace(new TerminalSize(0,1)));
        info.addComponent(new Label("Personnes effectives : " + order.getEffectivePeopleCount()));
        info.addComponent(new EmptySpace(new TerminalSize(0,1)));
        info.addComponent(new Label("Type : " + (order.getTakeAway() ? "À emporter" : "Sur place")));
        info.addComponent(new EmptySpace(new TerminalSize(0,1)));
        info.addComponent(new Label("En ligne : " + (order.getOnline() ? "Oui" : "Non")));
        info.addComponent(new EmptySpace(new TerminalSize(0,1)));

        root.addComponent(info);
        root.addComponent(new EmptySpace());

        root.addComponent(sectionHeader("Articles:"));

        // prix des articles
        Panel totals = new Panel(new GridLayout(2));
        Label sousTotalLbl = new Label("Sous-total:");
        Label sousTotalVal = new Label("");
        Label remiseLbl = new Label("Remise:");
        Label remiseVal = new Label("");
        Label totalLbl = new Label("Total:");
        Label totalVal = new Label("");

        root.addComponent(totals);
        double subTotal = order.calculateSubTotal();
        double discount = order.calculateDiscount();
        double total = order.calculateTotal();

        sousTotalVal.setText(String.format("%.2f €", subTotal));
        remiseVal.setText(String.format("-%.2f €", discount));
        totalVal.setText(String.format("%.2f €", total));

        totals.addComponent(sousTotalLbl);
        totals.addComponent(sousTotalVal);
        totals.addComponent(remiseLbl);
        totals.addComponent(remiseVal);
        totals.addComponent(totalLbl);
        totals.addComponent(totalVal);
        root.addComponent(new EmptySpace());

        // le client choisi sa méthode de paiement
        root.addComponent(sectionHeader("Méthode de paiement:"));
        root.addComponent(new EmptySpace());
        Panel pay = new Panel(new GridLayout(2));
        pay.addComponent(new Label("Méthode:"));
        ComboBox<String> cbMethod = new ComboBox<>("Cash", "Card", "Other");
        cbMethod.setPreferredSize(new TerminalSize(16, 1));
        pay.addComponent(cbMethod);
        root.addComponent(pay);

        root.addComponent(new EmptySpace(new TerminalSize(1,1)));

        Panel buttons = new Panel(new GridLayout(2));
        Button confirm = new Button("Confirmer le paiement", () -> {});
        Button cancel = new Button("Annuler",this::close);
        buttons.addComponent(confirm);
        buttons.addComponent(cancel);

        root.addComponent(buttons);

        setComponent(root);
    }
    // méthode pour ne pas dupliquer mon code
    private Component sectionHeader(String title) {
        Label header = new Label(title);
        header.addStyle(SGR.BOLD);
        header.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        return header;
    }
}