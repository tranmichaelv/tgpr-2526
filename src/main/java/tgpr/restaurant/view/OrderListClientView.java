package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import tgpr.restaurant.controller.OrderListClientController;
import tgpr.restaurant.controller.ReservationController;
import tgpr.restaurant.controller.ReservationEditController;
import tgpr.restaurant.model.*;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.model.OrderItem;
import tgpr.restaurant.model.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


public class OrderListClientView extends BasicWindow {

    // Made-up local date to test the feature (before merging with develop branch)
    public static final LocalDate TODAY_DEMO_DATE = Time.getDateChosen().toLocalDate();
    public static final LocalTime TODAY_DEMO_TIME = Time.getDateChosen().toLocalTime();
    private final OrderListClientController controller;
    private final List<Order> ordersList = Order.getAll();
    private final List<Reservation> reservationList = Reservation.getAll(); // using getAll method from model
    private final Menu menuNav;
    private final Menu menuUser;
    private User userlloged;
    private final TextBox clientNameBox;
    private boolean hasOrders = false;
    // la liste sera scrollable et sélectionnable
    private final ActionListBox ordersListBox = new ActionListBox(new TerminalSize(120, 10));
    private CheckBox showAllOrders;
    private CheckBox showAllReservations;
    private Button newReservation;
    private ActionListBox reservationListBox = new ActionListBox(new TerminalSize(80, 7));

    public OrderListClientView(OrderListClientController controller, User userlogged) {
        this.controller = controller;
        this.userlloged = userlogged;

        // Title (will be changed according user's role)
        setTitle("Panel pour afficher les commandes en tant que " + userlogged.getRole());
        setHints(List.of(Hint.EXPANDED));

        // Root Panel englobing every element
        Panel root = new Panel();
        setComponent(root);
        // root.setPreferredSize(new TerminalSize(115,20));

        // menuFile nav
        MenuBar header = new MenuBar().addTo(root);
        menuUser = new Menu("Utilisateur");
        MenuItem menuDisconnect = new MenuItem("Exit",this::exit);
        menuUser.add(menuDisconnect);

        menuNav = new Menu("Navigation");
        header.add(menuUser);
        header.add(menuNav);

        if(Security.isClient()){
            menuClient(menuNav);
        } else if (Security.isEmployee()) {
            menuEmployee(menuNav);
        } else {
            menuBoss(menuNav);
        }


        // exemple du champs ou on peut saisir le nom dun client
        Panel clientNameFilter = new Panel(new LinearLayout(Direction.VERTICAL));
        clientNameFilter.addComponent(new Label("Filtrer par nom de client: "));
        clientNameBox = new TextBox(new TerminalSize(20, 1));
        clientNameBox.setValidationPattern(Pattern.compile("^[A-Za-z]+(?: [A-Za-z]*)*$"));
        clientNameBox.setTextChangeListener((newText, buUser) -> populateListBox());
        // affichage du filtre uniquement pour le boss et l'employee
        if (Security.isLogged() && !Security.isClient()) {
            root.addComponent(new EmptySpace(new TerminalSize(0,1)));
            clientNameFilter.addTo(root);
            clientNameBox.addTo(root);
        }

        // Creating sub-Panel reservations
        Panel reservations = new Panel(new LinearLayout(Direction.VERTICAL));
        root.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        // Title
        Label titleReservations = new Label("RESERVATIONS");
        titleReservations.addStyle(SGR.UNDERLINE);
        TextColor blue = new TextColor.RGB(0, 0, 204);
        titleReservations.setForegroundColor(blue);
        reservations.addComponent(titleReservations);

        // Adding all elements in reservation panel into root
        reservations.addTo(root);
        reservations.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        // CheckBox to choose whether to show all reservations
        showAllReservations = new CheckBox("Afficher toutes les réservations (y compris passées et avec commande)");
        showAllReservations.setChecked(false);
        showAllReservations.addListener(checked -> {
            updateReservationList(reservationListBox, reservationList, checked);
        });
        reservations.addComponent(showAllReservations);
        reservations.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        updateReservationList(reservationListBox, reservationList, false);
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
            TextColor gray = new TextColor.RGB(80, 80, 80);
            field.setForegroundColor(gray);
            displayReservationsHeader.addComponent(field);
        }

        // adding all components into reservation panel
        reservations.addComponent(displayReservationsHeader);
        reservations.addComponent(reservationListBox);
        reservations.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        // New reservation button
        newReservation = new Button("Nouvelle réservation");
        reservations.addComponent(newReservation);



        // COMMANDES
        root.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        Panel orders = new Panel(new LinearLayout(Direction.VERTICAL));

        Label titleOrders = new Label("COMMANDES");
        titleOrders.addStyle(SGR.UNDERLINE);
        titleOrders.addStyle(SGR.BOLD);
        titleOrders.setForegroundColor(blue);
        orders.addComponent(titleOrders);

        orders.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        showAllOrders = new CheckBox("Afficher aussi les commandes terminées (payées)");
        orders.addComponent(showAllOrders);

        // Quand je coche / décoche -> recharger la liste
        showAllOrders.addListener((checkBox) -> populateListBox());

        orders.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        root.addComponent(orders);

        Panel displayOrdersHeader = new Panel();

        // Le nombre de colonnes dépend du rôle
        int nbCols = (userlogged.getRole() == User.Role.Employee || userlogged.getRole() == User.Role.Boss) ? 9 : 8;
        displayOrdersHeader.setLayoutManager(new GridLayout(nbCols));

        List<Label> headers = new ArrayList<>();


        if (userlogged.getRole() == User.Role.Employee || userlogged.getRole() == User.Role.Boss) {
            headers.add(new Label(String.format("%-15s", "Client")));
        }

        headers.add(new Label(String.format("%-12s", "Date")));
        headers.add(new Label(String.format("%-14s", "Table")));
        headers.add(new Label(String.format("%-9s", "Service")));
        headers.add(new Label(String.format("%-6s", "Pers.")));
        headers.add(new Label(String.format("%-12s", "Type")));
        headers.add(new Label(String.format("%-9s", "En ligne")));
        headers.add(new Label(String.format("%-9s", "Montant")));
        headers.add(new Label(String.format("%-9s", "Paiement")));

        // Appliquer style uniforme à tous les labels
        for (Label lbl : headers) {
            lbl.addStyle(SGR.UNDERLINE);
            lbl.addStyle(SGR.BOLD);
            lbl.setForegroundColor(new TextColor.RGB(80, 80, 80));
            displayOrdersHeader.addComponent(lbl);
        }

        orders.addComponent(displayOrdersHeader);

        updateTitle(userlogged);

        // Remplir la liste de commandes
        populateListBox();

        // ✅ Ajouter la liste DANS le panel "orders", pas dans root !
        orders.addComponent(ordersListBox);

        // Espace avant le bouton
        orders.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        // Bouton "Nouvelle commande" en bas
        Button newOrder = new Button("Nouvelle commande", () -> controller.openCreateNewOrder());
        orders.addComponent(newOrder);
        // Enfin, ajouter le panel complet au root
        root.addComponent(orders);

    }

    // displaying title according to User's type
    public void updateTitle(User user) {
        String roleLabel;

        if (user.getRole().toString().equalsIgnoreCase("client")) {
            roleLabel = "client";
        } else if (user.getRole().toString().equalsIgnoreCase("employee")) {
            roleLabel = "employee";
        } else if (user.getRole().toString().equalsIgnoreCase("boss")) {
            roleLabel = "boss";
        } else {
            roleLabel = "unknown";
        }

        setTitle(String.format("Restaurant (%s, %s - %s) - Date simulée: %s %s",
                user.getFirstName(),
                user.getFirstName(),
                roleLabel,
                TODAY_DEMO_DATE.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                TODAY_DEMO_TIME.format(DateTimeFormatter.ofPattern("HH:mm"))
        ));
    }

     public void refreshReservation() {
        updateReservationList(reservationListBox, reservationList, true);
    }

    public void updateReservationList(ActionListBox reservationListBox, List<Reservation> allReservations, boolean showAll) {
        reservationListBox.clearItems();

        for (Reservation r : allReservations) {
            if (showAll || !(r.getReservationDate().isBefore(TODAY_DEMO_DATE))) {
                String row = String.format("%s%s%7s%16s%4s%8s",
                        " Table " + (r.getTable().getId() < 10 ?
                                r.getTable().getId() + " " :
                                r.getTable().getId()
                        ),
                        " (" + r.getTable().getCapacity() + "p)",
                        r.getService().getName(),
                        r.getReservationDate(),
                        r.getNumberOfPeople(),
                        r.getOnline() ? "Oui" : "Non"
                );
                reservationListBox.addItem(row, () -> {
                    if (r.getReservationDate().isBefore(TODAY_DEMO_DATE))
                        return;
                    editReservation(r);
                });
            }
        }
    }

    private void createNewReservation() {
        var ctrl = new ReservationController();
        BasicWindow win = ctrl.getView();

        win.setHints(Arrays.asList(
                Window.Hint.MODAL,
                Window.Hint.CENTERED
        ));

        getTextGUI().addWindowAndWait(win);
    }

    private void editReservation(Reservation reservationToEdit) {
        var ctrl = new ReservationEditController();
        ctrl.setReservation(reservationToEdit);
        var win = ctrl.getView();

        win.setHints(java.util.Arrays.asList(
                Window.Hint.MODAL,
                Window.Hint.CENTERED
        ));

        getTextGUI().addWindowAndWait(win);
    }

    private void populateListBox() {
        ordersListBox.clearItems();

        String filtreNom = clientNameBox.getText().trim().toLowerCase();

        boolean showAll = showAllOrders.isChecked(); // état de la checkbox

        for (Order order : ordersList) {
            User client = order.getClient();
            boolean isPaid = order.getPaymentMethod() != null;

            // Je filtre selon le texte saisi
            boolean correspond = filtreNom.isEmpty()
                    || client.getLastName().toLowerCase().contains(filtreNom)
                    || client.getFirstName().toLowerCase().contains(filtreNom);

            boolean visible = false;

            // Détermine si cette commande doit être affichée
            if (userlloged.getRole() == User.Role.Client) {
                // Un client ne voit que ses propres commandes
                if (client.equals(userlloged) && correspond && (showAll || !isPaid)) {
                    visible = true;
                }
            } else {
                // Employé ou boss voit tout, filtré par nom &&  statut payé
                if (correspond && (showAll || !isPaid)) {
                    visible = true;
                }
            }

            if (visible) {
                String display = toString(order);

                if (userlloged.getRole() == User.Role.Client) {
                    ordersListBox.addItem(display, () -> controller.openDetails(order));
                } else {
                    ordersListBox.addItem(display, () -> controller.openActions(order));
                }
            }

            if (ordersListBox.getItemCount() == 0) {
                ordersListBox.addItem("", () -> {
                });
            }
        }
    }

    private static String padOrTruncate(String text, int width) {
        if (text == null) text = "";
        if (text.length() > width - 3) {
            text = text.substring(0, width - 3) + "...";
        }
        return String.format("%-" + width + "s", text);
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
        String paiement = String.valueOf(order.getPaymentMethod()); //

        if (userlloged.getRole() == User.Role.Client){
            return    padOrTruncate(date, 16)
                    + padOrTruncate(tableInfo, 15)
                    + padOrTruncate(service, 11)
                    + padOrTruncate(pers, 9)
                    + padOrTruncate(type, 12)
                    + padOrTruncate(online, 12)
                    + padOrTruncate(amount, 12)
                    + padOrTruncate(paiement, 12);
        } else if (userlloged.getRole() == User.Role.Employee || userlloged.getRole() == User.Role.Boss){
            return  " " + padOrTruncate(clientfullName, 16)
                    + padOrTruncate(date, 13)
                    + padOrTruncate(tableInfo, 15)
                    + padOrTruncate(service, 10)
                    + padOrTruncate(pers, 7)
                    + padOrTruncate(type, 13)
                    + padOrTruncate(online, 10)
                    + padOrTruncate(amount, 10)
                    + padOrTruncate(paiement, 14);
        }
        return null;
    }

    private void exit(){
        controller.exit();
    }

    private void menuClient(Menu menu){
        MenuItem menuBook = new MenuItem("Réserver une table", this::reservation);
        menu.add(menuBook);
        MenuItem menuFood = new MenuItem("Voir la carte", this::menu);
        menu.add(menuFood);
        MenuItem menuBasket = new MenuItem("Voir le panier", this::basket);
        menu.add(menuBasket);
        MenuItem menuTables = new MenuItem("Voir les tables");
        menu.add(menuTables);

    }

    private Menu menuEmployee(Menu menu){
        MenuItem menuBook = new MenuItem("Réserver une table");
        menu.add(menuBook);
        MenuItem menuFood = new MenuItem("Enregistrer une commande",this::order);
        menu.add(menuFood);
        MenuItem menuCLient = new MenuItem("Enregistrer un client",this::clientRegister);
        menu.add(menuCLient);
        MenuItem menuTables = new MenuItem("Voir les tables");
        menu.add(menuTables);

        return menu;
    }

    private Menu menuBoss(Menu menu){
        MenuItem menuBook = new MenuItem("Réserver une table");
        menu.add(menuBook);
        MenuItem menuFood = new MenuItem("Enregistrer une commande",this::order);
        menu.add(menuFood);
        MenuItem menuCLient = new MenuItem("Enregistrer un client",this::clientRegister);
        menu.add(menuCLient);
        MenuItem menuMenu = new MenuItem("Modifier la carte",this::changeMenu);
        menu.add(menuMenu);
        MenuItem menuClosing = new MenuItem("Gerer les periodes de fermeture",this::closing);
        menu.add(menuClosing);
        MenuItem menuUser = new MenuItem("Gerer les utilisateurs",this::changeUser);
        menu.add(menuUser);
        MenuItem menuTables = new MenuItem("Gerer les tables");
        menu.add(menuTables);

        return menu;
    }

    private void basket(){
        controller.basket();
    }

    private void menu(){
        controller.menu();
    }

    private void reservation() {
        var win = new ReservationView(new ReservationController());
        win.setHints(Arrays.asList(Window.Hint.MODAL, Window.Hint.CENTERED));
        getTextGUI().addWindowAndWait(win);
    }

    private void changeMenu(){
        controller.changeMenu();
    }

    private void closing(){
        controller.closing();
    }

    private void changeUser(){
        controller.changeUser();
    }

    private void order(){
        controller.order();
    }

    private void clientRegister(){
        controller.clientRegister();
    }
}