package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderChangeController;
import tgpr.restaurant.controller.OrderCheckController;
import tgpr.restaurant.controller.OrderDetailsController;
import tgpr.restaurant.controller.OrderPayController;
import tgpr.restaurant.model.Order;

public class OrderCheckView extends BasicWindow {

    private final OrderCheckController controller;
    private final Order order;

    public OrderCheckView(OrderCheckController controller, Order order) {
        this.controller = controller;
        this.order = order;

        setTitle("Action sur la commande");


        Panel root = new Panel(new BorderLayout());
        Panel mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        mainPanel.addComponent(new Label("Que voulez-vous faire avec cette commande ?"));


        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        ActionListBox actionListBox = new ActionListBox();
        actionListBox.addItem("Éditer la commande", controller::openChangeView);
        actionListBox.addItem("Voir les détails", controller::openDetailsView);
        actionListBox.addItem("Enregistrer le paiement", controller::openRegisterPaymentView);
        actionListBox.addItem("Annuler", controller::openCancelOrderView);
        mainPanel.addComponent(actionListBox);

        root.addComponent(mainPanel, BorderLayout.Location.CENTER);

        Panel bottomPanel = new Panel(new BorderLayout());
        bottomPanel.addComponent(buttonCancel(), BorderLayout.Location.RIGHT);
        root.addComponent(bottomPanel, BorderLayout.Location.BOTTOM);

        setComponent(root);
    }
    public Panel buttonCancel() {
        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Cancel", this::close));
        return buttonPanel;
    }
}