package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderCheckController;

public class OrderCheckView extends BasicWindow {

    private final OrderCheckController controller;

    public OrderCheckView(OrderCheckController controller){
        this.controller = controller;

        setTitle("Action sur la commande");

        Panel root = new Panel(new LinearLayout(Direction.VERTICAL));
        root.addComponent(new Label("Que voulez-vous faire avec cette commande"));
        root.addComponent(new EmptySpace(TerminalSize.ONE));

        ActionListBox actionListBox = new ActionListBox();
        actionListBox.addItem("Éditer la commande", controller::openEditView);
        actionListBox.addItem("Enregistrer le paiement", controller::openRegisterPaymentView);
        actionListBox.addItem("Voir les détails", controller::openDetailsView);
        actionListBox.addItem("Annuler", controller::openCancelOrderView);
        root.addComponent(actionListBox);

        root.addComponent(buttonCancel());
        setComponent(root);
    }
    // cree le bouton cancel
    public Panel buttonCancel() {
        Panel button = new Panel();
        button.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        button.addComponent(new Button("Cancel", this::close));

        return button;
    }
}