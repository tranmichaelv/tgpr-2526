package tgpr.restaurant.controller;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Window;
import tgpr.framework.Controller;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.model.Security;
import tgpr.restaurant.model.User;
import tgpr.restaurant.view.OrderListEmployeeView;

import java.util.Arrays;

public class OrderListEmployeeController extends Controller<OrderListEmployeeView> {

    private final OrderListEmployeeView view;

    public OrderListEmployeeController() {
        this.view = new OrderListEmployeeView(this,getUser());
    }

    public User getUser() {
        return Security.getLoggedUser();
    }

    @Override
    public OrderListEmployeeView getView() {
        return view;
    }

//    @Override
//    public OrderListEmployeeView getView() {
//        return view;
//    }


    private void showModal(BasicWindow win) {
        win.setHints(Arrays.asList(
                Window.Hint.MODAL,
                Window.Hint.CENTERED
        ));
        // Je passe par la vue pour accéder au TextGUI
        view.getTextGUI().addWindowAndWait(win);
    }

    public void openActions(Order order) {
        //  Crée contrôleur + vue actions sur la commande
        var ctrl = new OrderCheckController(order);
        var win = ctrl.getView(); // vue OrderDetailsView (BasicWindow)

        //  Configure l’affichage pour qu’elle s’ouvre par-dessus
        win.setHints(java.util.Arrays.asList(
                Window.Hint.MODAL,     // bloque les interactions dessous
                Window.Hint.CENTERED   // centre la fenêtre
        ));
        //  Affiche la fenêtre par-dessus sans fermer celle de la liste
       showModal(win);
    }
}
