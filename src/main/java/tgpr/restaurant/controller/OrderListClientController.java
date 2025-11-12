package tgpr.restaurant.controller;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Window;
import tgpr.framework.Controller;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.model.Security;
import tgpr.restaurant.model.User;
import tgpr.restaurant.view.OrderListClientView;

import java.util.Arrays;


public class OrderListClientController extends Controller<OrderListClientView> {

    private final OrderListClientView view;
    private final User user;

    public OrderListClientController(User loggedUser){
        this.user = loggedUser;
        this.view = new OrderListClientView(this,getUser());
    }
    // pour pouvoir avancer en attendant que je fasse le lien avec le login
    public User getUser(){
        return Security.getLoggedUser();
    }

    @Override
    public OrderListClientView getView() {
        return view;
    }

    // public OrderListClientView getView() { return view; }

    private void showModal(BasicWindow win) {
        win.setHints(Arrays.asList(
                Window.Hint.MODAL,
                Window.Hint.CENTERED
        ));
        // Je passe par la vue pour accéder au TextGUI
        view.getTextGUI().addWindowAndWait(win);
    }
    public void openDetails(Order order) {
        //  Crée contrôleur + vue des détails
        var ctrl = new OrderDetailsController(order);
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

