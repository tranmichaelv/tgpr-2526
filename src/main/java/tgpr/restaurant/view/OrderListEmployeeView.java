package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderCheckController;
import tgpr.restaurant.controller.OrderListEmployeeController;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.model.OrderItem;
import tgpr.restaurant.model.User;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderListEmployeeView extends BasicWindow {

    private final OrderListEmployeeController controller;
    private final List<Order> ordersList = Order.getAll();
    private User user = new User();
    private boolean hasOrders = false;


    private final Panel root = new Panel();
    private final Panel orders = new Panel(new LinearLayout(Direction.VERTICAL));
    private final ActionListBox ordersListBox = new ActionListBox(new TerminalSize(120, 10));// la liste sera scrollable et sélectionnable
    private CheckBox showAllOrders;
    private Panel headersLine = new Panel(new LinearLayout(Direction.HORIZONTAL));


    public OrderListEmployeeView(OrderListEmployeeController controller,User user) {
        this.controller = controller;
        this.user = user;

        setTitle("Panel pour afficher les commandes en tant que Employée");
        setComponent(root);
        root.setPreferredSize(new TerminalSize(115,20));
        root.addComponent(new EmptySpace(new TerminalSize(0,1)));
        orders.addComponent(new Label("COMMANDES"));

        orders.addComponent(new EmptySpace(new TerminalSize(0,1)));
        showAllOrders = new CheckBox("Afficher aussi les commandes terminées (payées)                           ");
        orders.addComponent(showAllOrders);

        // quand on coche / décoche -> recharger la liste
        showAllOrders.addListener((checkBox) -> populateListBox());
        orders.addComponent(new EmptySpace(new TerminalSize(0,1)));
        root.addComponent(orders);

        root.addComponent(headersLine);
        headersLine.addComponent(emphasize("  Client   "));
        headersLine.addComponent(new EmptySpace(new TerminalSize(1,0)));
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

    private void openActions(Order order) {
        //  Crée contrôleur + vue actions sur la commande
        var ctrl = new OrderCheckController(order);
        //var ctrl = new OrderDetailsController(order);
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

        boolean showAll = showAllOrders.isChecked(); // je récupère l'état du checkBox
        for (Order order : ordersList) {
            if (order.getClient().equals(user)) {

                boolean isPaid = false;
                if (showAll || !isPaid){
                    String display = toString(order);
                    ordersListBox.addItem(display, () -> openActions(order));
                    hasOrders = true;
                }
            }
        }
        if (!hasOrders) {
            //ordersListBox.addItem("(Aucune commande à afficher)", () -> {});
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

    public double getPrice(Order order) {
        double total = 0.0;
        for (OrderItem item : order.getOrderItems()) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public String toString(Order order) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String clientfullName = order.getClient().getFirstName() + "," + order.getClient().getLastName();
        String date = order.getOrderDate().format(formatter);
        String tableInfo = (order.getTable() != null)
                ? "Table " + order.getTable().getId() + " (" + order.getTable().getCapacity() + "p)"
                : "A emporter";
        String service = order.getService().getName();
        String pers = String.valueOf(order.getEffectivePeopleCount());
        String type = order.getTakeAway() ? "A emporter" : "Sur place";
        String online = order.getOnline() ? "Oui" : "Non";
        String amount = String.format("%.2f €", getPrice(order));
        String paiement = order.getPaymentMethod() == null ? "En cours" : String.valueOf(order.getPaymentMethod());

        return "  " + padOrTruncate(clientfullName, 12)
                + padOrTruncate(date, 16)
                + padOrTruncate(tableInfo, 15)
                + padOrTruncate(service, 11)
                + padOrTruncate(pers, 9)
                + padOrTruncate(type, 12)
                + padOrTruncate(online, 12)
                + padOrTruncate(amount, 12)
                + padOrTruncate(paiement, 14);
    }
}
