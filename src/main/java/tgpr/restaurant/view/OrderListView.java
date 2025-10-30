package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderListController;
import tgpr.restaurant.model.Order;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderListView extends BasicWindow {

    private final OrderListController controller;
    private final List<Order> ordersList = Order.getAll();

    public OrderListView(OrderListController controller) {
        this.controller = controller;

        setTitle("Panel pour construire tableau de commandes");

        Panel root = new Panel();
        setComponent(root);
        root.setPreferredSize(new TerminalSize(100,20));

        root.addComponent(new EmptySpace(new TerminalSize(0,1)));
        Panel orders = new Panel(new LinearLayout(Direction.VERTICAL));
        orders.addComponent(new Label("COMMANDES"));
        //orders.setFillColorOverride(TextColor.ANSI.BLUE);
        // Underline

        orders.addComponent(new EmptySpace(new TerminalSize(0,1)));
        CheckBox showAllOrders = new CheckBox("Afficher aussi les commandes terminées (payées)                           ");
        orders.addComponent(showAllOrders);
        orders.addComponent(new EmptySpace(new TerminalSize(0,1)));
        root.addComponent(orders);


        Panel headersLine = new Panel(new GridLayout(20));
        root.addComponent(headersLine);

        Label clientLabel = new Label("Client             ");
        headersLine.addComponent(clientLabel);
        clientLabel.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        clientLabel.addStyle(SGR.UNDERLINE);
        //headersLine.addComponent(new EmptySpace(new TerminalSize(0,0)));

        Label dateLabel = new Label("Date       ");
        headersLine.addComponent(dateLabel);
        dateLabel.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        dateLabel.addStyle(SGR.UNDERLINE);
        //headersLine.addComponent(new EmptySpace(new TerminalSize(0,0)));

        Label tableLabel = new Label("Table     ");
        headersLine.addComponent(tableLabel);
        tableLabel.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        tableLabel.addStyle(SGR.UNDERLINE);
        //headersLine.addComponent(new EmptySpace(new TerminalSize(2,0)));

        Label serviceLabel = new Label("Service ");
        headersLine.addComponent(serviceLabel);
        serviceLabel.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        serviceLabel.addStyle(SGR.UNDERLINE);
        //headersLine.addComponent(new EmptySpace(new TerminalSize(2,0)));

        Label persLabel = new Label("Pers");
        headersLine.addComponent(persLabel);
        persLabel.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        persLabel.addStyle(SGR.UNDERLINE);
        //headersLine.addComponent(new EmptySpace(new TerminalSize(2,0)));

        Label typeLabel = new Label("Type   ");
        headersLine.addComponent(typeLabel);
        typeLabel.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        typeLabel.addStyle(SGR.UNDERLINE);
        //headersLine.addComponent(new EmptySpace(new TerminalSize(2,0)));

        Label onLineLabel = new Label("En ligne  ");
        headersLine.addComponent(onLineLabel);
        onLineLabel.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        onLineLabel.addStyle(SGR.UNDERLINE);
        //headersLine.addComponent(new EmptySpace(new TerminalSize(2,0)));

        Label amountLabel = new Label("Montant   ");
        headersLine.addComponent(amountLabel);
        amountLabel.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        amountLabel.addStyle(SGR.UNDERLINE);
        //headersLine.addComponent(new EmptySpace(new TerminalSize(2,0)));


        Label paiementLabel = new Label("Paiement");
        headersLine.addComponent(paiementLabel);
        paiementLabel.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        paiementLabel.addStyle(SGR.UNDERLINE);
        //headersLine.addComponent(new EmptySpace(new TerminalSize(2,0)));


        root.addComponent(headersLine);

        ComboBox<String> ordersInfoBox = new ComboBox<>();
        try {
            for (Order order : ordersList){
                String allInfos = toString(order);
                ordersInfoBox.addItem(allInfos);
            }
        } catch (Exception ex){
            System.out.println(" ");
        }

        root.addComponent(ordersInfoBox);
    }

    public String toString(Order order) {
        String res = "";
        //String fullName, date, service , pers, type, online, amount, paiement;
        String fullName = order.getClient().getFirstName() + "," + order.getClient().getLastName();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = order.getOrderDate().format(formatter);
        //String table = Integer.toString(order.getTableId()); // j'ai pas encore trouvé la solution pour afficher ca
        String service = String.valueOf(order.getService().getName());
        String pers = String.valueOf(order.getEffectivePeopleCount());
        String type = String.valueOf(order.getTakeAway()); // La même chose pour ca
        String online = String.valueOf(order.getOnline()); // La même
        String amount = String.valueOf(order.getDiscount()); // La même
        String paiement = String.valueOf(order.getDiscount()); // La même

        res = fullName + " " + date +  " " + "Table" + service + " " + pers + " "+ type  + " "
                + online + " " + amount + " " + paiement ;

        // un petit soucis au niveau de l'affichage (Espacement)
        return res;
    }
}
