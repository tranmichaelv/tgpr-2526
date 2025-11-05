package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import tgpr.framework.Controller;
import tgpr.restaurant.controller.OrderDetailsController;
import tgpr.restaurant.controller.OrderListClientController;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.model.OrderItem;
import tgpr.restaurant.model.User;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderListClientView extends BasicWindow {

    private final OrderListClientController controller;
    private final List<Order> ordersList = Order.getAll();
    private User user = new User();
    // la liste sera scrollable et sélectionnable
    private final ActionListBox ordersListBox = new ActionListBox(new TerminalSize(120, 10));

    public OrderListClientView(OrderListClientController controller, User user) {
        this.controller = controller;
        this.user = user;

        setTitle("Panel pour afficher les commandes en tant que client");

        Panel root = new Panel();
        setComponent(root);
        root.setPreferredSize(new TerminalSize(115,20));

        root.addComponent(new EmptySpace(new TerminalSize(0,1)));
        Panel orders = new Panel(new LinearLayout(Direction.VERTICAL));
        orders.addComponent(new Label("COMMANDES"));

        orders.addComponent(new EmptySpace(new TerminalSize(0,1)));
        CheckBox showAllOrders = new CheckBox("Afficher aussi les commandes terminées (payées)                           ");
        orders.addComponent(showAllOrders);

        orders.addComponent(new EmptySpace(new TerminalSize(0,1)));
        root.addComponent(orders);

        Panel headersLine = new Panel(new LinearLayout(Direction.HORIZONTAL));
        root.addComponent(headersLine);
        headersLine.addComponent(emphasize("  Date       "));
        headersLine.addComponent(new EmptySpace(new TerminalSize(1,0)));
        headersLine.addComponent(emphasize("Table       "));
        headersLine.addComponent(new EmptySpace(new TerminalSize(1,0)));
        headersLine.addComponent(emphasize("Service "));
        headersLine.addComponent(new EmptySpace(new TerminalSize(1,0)));
        headersLine.addComponent(emphasize("Pers. "));
        headersLine.addComponent(new EmptySpace(new TerminalSize(1,0)));
        headersLine.addComponent(emphasize("Type     "));
        headersLine.addComponent(new EmptySpace(new TerminalSize(1,0)));
        headersLine.addComponent(emphasize("En ligne "));
        headersLine.addComponent(new EmptySpace(new TerminalSize(1,0)));
        headersLine.addComponent(emphasize("Montant  "));
        headersLine.addComponent(new EmptySpace(new TerminalSize(1,0)));
        headersLine.addComponent(emphasize("Paiement"));

        root.addComponent(headersLine);

        // Appelle la fonction pour remplir la listBox
        populateListBox();
        root.addComponent(ordersListBox);
    }

    private void openDetails(Order order) {
        //  Crée contrôleur + vue des détails
        var ctrl = new OrderDetailsController(order);
        var win = ctrl.getView(); // vue OrderDetailsView (BasicWindow)

        //  Configure l’affichage pour qu’elle s’ouvre par-dessus
        win.setHints(java.util.Arrays.asList(
                Window.Hint.MODAL,     // bloque les interactions dessous
                Window.Hint.CENTERED   // centre la fenêtre
        ));
        //  Affiche la fenêtre par-dessus sans fermer celle de la liste
        getTextGUI().addWindowAndWait(win);
    }

    private void populateListBox() {
        ordersListBox.clearItems();

        boolean hasOrders = false;
        for (Order order : ordersList) {
            if (order.getClient().equals(user)) {
                String display = toString(order);
                ordersListBox.addItem(display, () -> openDetails(order));
                hasOrders = true;
            }
        }
        if (!hasOrders) {
            ordersListBox.addItem("(Aucune commande à afficher)", () -> {});
            ordersListBox.setSelectedIndex(0);
        }
    }

    // Tronque ou pad une chaîne pour qu’elle ait une largeur fixe
    private static String padOrTruncate(String text, int width) {
        if (text == null) text = "";
        if (text.length() > width - 3) {
            text = text.substring(0, width - 3) + "...";
        }
        return String.format("%-" + width + "s", text);
    }

    private Component emphasize(String title) {
        Panel headerPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        Label header = new Label(title);
        header.addStyle(SGR.BOLD);
        header.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        String underline = title.replaceAll(".", "─");
        Label underlineLabel = new Label(underline);
        headerPanel.addComponent(header);
        headerPanel.addComponent(underlineLabel);
        return headerPanel;
    }

    public String getType(boolean type){
        return type ? "A emporter" : "Sur place";
    }

    public String getonline(boolean online) {
        return online ? "Oui" : "Non";
    }
    public double getPrice(Order order) {
        double total = 0.0;
        for (OrderItem item : order.getOrderItems()) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public String toString(Order order) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = order.getOrderDate().format(formatter);
        String tableInfo = (order.getTable() != null)
                ? "Table " + order.getTable().getId() + " (" + order.getTable().getCapacity() + "p)"
                : "A emporter";
        String service = order.getService().getName();
        String pers = String.valueOf(order.getEffectivePeopleCount());
        boolean type = order.getTakeAway();
        boolean online = order.getOnline();
        String amount = String.format("%.2f €", getPrice(order));
        String paiement = String.valueOf(order.getPaymentMethod()); //


        return "  " + padOrTruncate(date, 14)
                + padOrTruncate(tableInfo, 15)
                + padOrTruncate(service, 11)
                + padOrTruncate(pers, 9)
                + padOrTruncate(getType(type), 12)
                + padOrTruncate(getonline(online), 12)
                + padOrTruncate(amount, 12)
                + padOrTruncate(paiement, 12);
    }
}