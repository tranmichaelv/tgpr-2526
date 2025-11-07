package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import tgpr.restaurant.controller.OrderDetailsController;
import tgpr.restaurant.controller.OrderListClientController;
import tgpr.restaurant.model.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderListClientView extends BasicWindow {

    private final OrderListClientController controller;
    private final List<Order> ordersList = Order.getAll();
    private final List<Reservation> reservationList = Reservation.getAll(); // using getAll method from model
    private final Menu menuNav;
    private final Menu menuUser;
    private User user = new User();
    // la liste sera scrollable et sélectionnable
    private final ActionListBox ordersListBox = new ActionListBox(new TerminalSize(120, 10));
    private CheckBox showAllOrders;
    private CheckBox showAllReservations;
    private Button newReservation;

    public OrderListClientView(OrderListClientController controller, User user) {
        this.controller = controller;
        this.user = user;

        // Title (will be changed according user's role)
        setTitle("Panel pour afficher les commandes en tant que client");
        setHints(List.of(Hint.EXPANDED));

        // Root Panel englobing every element
        Panel root = new Panel();
        setComponent(root);
        root.setPreferredSize(new TerminalSize(115,20));

        // menuFile nav
        MenuBar header = new MenuBar().addTo(root);
        menuUser = new Menu("Utilisateur");
        menuNav = new Menu("Navigation");
        header.add(menuUser);
        header.add(menuNav);
        MenuItem menuBook = new MenuItem("Réserver une table");
        menuNav.add(menuBook);
        MenuItem menuFood = new MenuItem("Voir la carte");
        menuNav.add(menuFood);
        MenuItem menuBasket = new MenuItem("Voir le panier");
        menuNav.add(menuBasket);
        MenuItem menuTables = new MenuItem("Voir les tables");
        menuNav.add(menuTables);

        // Creating sub-Panel reservations
        Panel reservations = new Panel(new LinearLayout(Direction.VERTICAL));
        root.addComponent(new EmptySpace(new TerminalSize(0,1)));

        // Title
        Label titleReservations = new Label("RESERVATIONS");
        titleReservations.addStyle(SGR.UNDERLINE);
        TextColor blue = new TextColor.RGB(0,0,204);
        titleReservations.setForegroundColor(blue);
        reservations.addComponent(titleReservations);

        // Adding all elements in reservation panel into root
        reservations.addTo(root);
        reservations.addComponent(new EmptySpace(new TerminalSize(0,1)));

        // CheckBox to choose whether to show all reservations
        showAllReservations = new CheckBox("Afficher toutes les réservations (y compris passées et avec commande)");
        reservations.addComponent(showAllReservations);
        reservations.addComponent(new EmptySpace(new TerminalSize(0,1)));

        // List of Labels representing the "header" of Reservations
        List<Label> ls = new ArrayList<>();
        ls.add(new Label(String.format("%-15s", "Table")));
        ls.add(new Label(String.format("%-9s", "Service")));
        ls.add(new Label(String.format("%-12s", "Date")));
        ls.add(new Label(String.format("%-5s", "Pers.")));
        ls.add(new Label(String.format("%-8s", "En ligne")));

        // Panel displayReservationHeader
        Panel displayReservationsHeader = new Panel(new GridLayout(5)); // nb column == nb labels == 5
        // for each to add each label to displayReservationHeader
        for (Label field : ls) {
            field.addStyle(SGR.UNDERLINE);
            field.addStyle(SGR.BOLD);
            TextColor gray = new TextColor.RGB(80 ,80,80);
            field.setForegroundColor(gray);
            displayReservationsHeader.addComponent(field);
        }

        // Panel displayReservations
        Panel displayReservations = new Panel(new GridLayout(5));
        for (Reservation r : reservationList) {
            displayReservations.addComponent(new Label("Table " + String.format("%-2s", r.getTable().getId()) + " (" + String.format( "%1s", r.getTable().getCapacity()) + "p)"));
            displayReservations.addComponent(new Label(String.format("%6s", r.getService().getName())));
            displayReservations.addComponent(new Label(String.format("%15s", r.getReservationDate())));
            displayReservations.addComponent(new Label(String.format("%3s", r.getNumberOfPeople())));
            displayReservations.addComponent(new Label(String.format("%7s", r.getOnline() ? "Oui" : "Non")));
        }

        // adding all components into reservation panel
        reservations.addComponent(displayReservationsHeader);
        reservations.addComponent(displayReservations);

        // New reservation button
        newReservation = new Button("Nouvelle réservation");
        reservations.addComponent(new EmptySpace(new TerminalSize(0,5)));
        reservations.addComponent(newReservation);

    // COMMANDES
        root.addComponent(new EmptySpace(new TerminalSize(0,1)));
        Panel orders = new Panel(new LinearLayout(Direction.VERTICAL));
        orders.addComponent(new Label("COMMANDES"));

        orders.addComponent(new EmptySpace(new TerminalSize(0,1)));
        showAllOrders = new CheckBox("Afficher aussi les commandes terminées (payées)                           ");
        orders.addComponent(showAllOrders);

        // quand on coche / décoche -> recharger la liste
        showAllOrders.addListener((checkBox) -> populateListBox());

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

//    private void fillReservationList() {
//        reservationList.clear();
//
//        boolean hasReservations = false;
//        for (Reservation reservation : reservationList) {
//
//            //boolean isPaid = order.getPaymentMethod() != null;
//            if (reservation.getClient().equals(user)) {
//
//                String display = toString(reservation);
//                reservationList.add(display, () -> openDetails(reservation));
//                hasReservations = true;
//            }
//        }
//        if (!hasReservations) {
//            ordersListBox.addItem("(Aucune commande à afficher)", () -> {});
//            ordersListBox.setSelectedIndex(0);
//        }
//    }

    private void openDetails(Order order) {
        //  Crée contrôleur + vue des détails
        var ctrl = new OrderDetailsController(order);
        var win = ctrl.getReservationView(); // vue OrderDetailsView (BasicWindow)

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

            //boolean isPaid = order.getPaymentMethod() != null;
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
        String paiement = order.getPaymentMethod() == null ? "En cours" : String.valueOf(order.getPaymentMethod());


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