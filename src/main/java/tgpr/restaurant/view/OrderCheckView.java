package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderCheckController;

public class OrderCheckView extends BasicWindow {

    private final OrderCheckController controller;

    public OrderCheckView(OrderCheckController controller){
        this.controller = controller;

        Panel root = new Panel(new BorderLayout());
        Panel fillPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));

        setTitle("Action sur la commande");
        root.addComponent(new Label("Que voulez-vous faire avec cette commande"));
        root.addComponent(new EmptySpace(TerminalSize.ONE));

        ActionListBox actionListBox = new ActionListBox();
        actionListBox.addItem("Éditer la commande", controller::openEditView);
        actionListBox.addItem("Enregistrer le paiement", controller::openRegisterPaymentView);
        actionListBox.addItem("Voir les détails", controller::openDetailsView);
        actionListBox.addItem("Annuler", controller::openCancelOrderView);

        fillPanel.addComponent(actionListBox);

        root.addComponent(fillPanel, BorderLayout.Location.CENTER);

        Panel bottomPanel = new Panel(new BorderLayout());
        bottomPanel.addComponent(buttonCancel(), BorderLayout.Location.RIGHT);
        root.addComponent(bottomPanel, BorderLayout.Location.BOTTOM);

        setComponent(root);
    }
    public Panel buttonCancel() {
        Panel button = new Panel();
        button.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        button.addComponent(new Button("Cancel", this::close));

        return button;
    }
}