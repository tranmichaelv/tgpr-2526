package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderCheckController;

public class OrderCheckView extends BasicWindow {

    /*
       select one order (open this view) : can edit,
       register, show details, cancel order or come back
    */


    private final OrderCheckController controller;

    public OrderCheckView(OrderCheckController controller){
        this.controller = controller;


        setTitle("Action sur la commande");

        Panel root = new Panel();
        root.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        Label sousTitre = new Label("Que voulez-vous faire avec cette commande");
        root.addComponent(sousTitre);
        root.addComponent(new EmptySpace(TerminalSize.ONE));

        ActionListBox actionListBox = new ActionListBox();
        actionListBox.addItem("Éditer la commande", controller::openEditView);
        actionListBox.addItem("Enregistrer le paiement", controller::openRegisterPaymentView);
        actionListBox.addItem("Voir les détails", controller::openDetailsView);
        actionListBox.addItem("Annuler", controller::openCancelOrderView);


//        Button cancelButton = new Button();
//
        actionListBox.addTo(root);
//        root.addComponent(cancelButton);
        var rightPanel = new Panel(new BorderLayout());
        rightPanel.addComponent(buttonCancel().right());


        root.addComponent(rightPanel);
//
        setComponent(root);
    }

    public Panel buttonCancel() {
        Panel button = new Panel();
        button.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        button.addComponent(new Button("Cancel", this::close));

        return button;
    }

}