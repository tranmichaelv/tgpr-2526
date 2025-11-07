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

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderCreateView extends BasicWindow {

    private final OrderCreateController controller;
    private final List<User> usersList = User.getAll();
    private final List<Table> tablesList = Table.getAll();
    private final List<Order> ordersList = Order.getAll();
    private final List<Service> serviceList = Service.getAll();


    // Cette partie est dédiée a l'affichage des commandes
    // Private final List<Order> ordersList = Order.getAll();


    public OrderCreateView(OrderCreateController controller) {
        this.controller = controller;

        // Vue pour Ajouter/Commande Commande
        setTitle("Nouvelle commande");

        Panel root = new Panel();
        setComponent(root);

        root.addComponent(new EmptySpace(new TerminalSize(0,1)));


        Panel infoSection = new Panel(new LinearLayout(Direction.VERTICAL));
        infoSection.addComponent(new Label("Informations de la commande : "));
        infoSection.addComponent(new EmptySpace(new TerminalSize(0,1)));

        root.addComponent(infoSection);
        //infoSection.addTo(root);

        // PANEL CLIENT
        Panel clientPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        clientPanel.addComponent(new Label("Client : "));
        clientPanel.addComponent(new EmptySpace(new TerminalSize(3,0)));

        // stocker les clients récup de la DB dans le ComboBox

        ComboBox<String> clientsBox = new ComboBox();
        try {
            for (User user : usersList){
                String fullName = user.getFirstName() + "," + user.getLastName();
                clientsBox.addItem(fullName);
            }
        } catch (Exception ex){
            System.out.println("Utilisateur pas trouvé !");
        }

        //clientPanel.addComponent(clientsBox);
        //clientPanel.addTo(root);

        clientPanel.addComponent(clientsBox);
        infoSection.addComponent(clientPanel);

        // PANEL TABLE
        Panel tablePanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        tablePanel.addComponent(new Label("Table : "));
        tablePanel.addComponent(new EmptySpace(new TerminalSize(3,0)));

        ComboBox<String> tablesBox = new ComboBox<>();
        tablePanel.addComponent(tablesBox);
        infoSection.addComponent(tablePanel);


        Map<Integer,Integer> clientTableMap = new HashMap<>();
        for (Order order : ordersList){
            if (order.getClientId() != 0 && order.getTableId() != null){
                clientTableMap.put(order.getClientId(), order.getClientId());
            }
        }

        clientsBox.addListener((selectedIndex, previousSelection, changedByUserInteraction) -> {
           if (selectedIndex < 0) return;


           User selectedUser = usersList.get(selectedIndex);
           int clientId = selectedUser.getId();

           tablesBox.clearItems();

            if (clientTableMap.containsKey(clientId)) {
                int tableId = clientTableMap.get(clientId);

                Table selectedTable = tablesList.stream()
                        .filter(t -> t.getId() == tableId)
                        .findFirst()
                        .orElse(null);

                if (selectedTable != null) {
                    tablesBox.addItem("Table " + selectedTable.getId() + "(" + selectedTable.getCapacity() + "p)");
                    tablesBox.setSelectedIndex(0);
                } else {
                    tablesBox.addItem("Table non trouvée (ID " + tableId + ")");
                }
            } else {
                tablesBox.addItem("Aucune table réservée pour ce client");
            }
        });

        // PANEL SERVICE
        Panel servicePanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        servicePanel.addComponent(new Label("Service : "));
        servicePanel.addComponent(new EmptySpace(new TerminalSize(3, 0)));

        ComboBox<String> serviceBox = new ComboBox<>();
        servicePanel.addComponent(serviceBox);
        infoSection.addComponent(servicePanel);

// Construire la map client -> service
        Map<Integer, Integer> clientServiceMap = new HashMap<>();
        for (Order order : ordersList) {
            if (order.getClientId() != 0 && order.getServiceId() != 0) {
                clientServiceMap.put(order.getClientId(), order.getServiceId());
            }
        }

// Quand on change de client, mettre à jour le service associé
        clientsBox.addListener((selectedIndex, previousSelection, changedByUserInteraction) -> {
            if (selectedIndex < 0) return;

            User selectedUser = usersList.get(selectedIndex);
            int clientId = selectedUser.getId();

            serviceBox.clearItems();

            if (clientServiceMap.containsKey(clientId)) {
                int serviceId = clientServiceMap.get(clientId);

                Service selectedService = serviceList.stream()
                        .filter(s -> s.getId() == serviceId)
                        .findFirst()
                        .orElse(null);

                if (selectedService != null) {
                    serviceBox.addItem(selectedService.getName()); // ou selectedService.toString()
                    serviceBox.setSelectedIndex(0);
                } else {
                    serviceBox.addItem("Service non trouvé");
                }
            } else {
                serviceBox.addItem("Aucun service associé");
            }
        });


    }

}