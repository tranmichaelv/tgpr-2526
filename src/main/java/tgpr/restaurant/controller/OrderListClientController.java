package tgpr.restaurant.controller;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Window;
import tgpr.framework.Controller;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.model.Reservation;
import tgpr.restaurant.model.Security;
import tgpr.restaurant.model.User;
import tgpr.restaurant.view.OrderListClientView;

import java.util.Arrays;


public class OrderListClientController extends Controller<OrderListClientView> {

    private final OrderListClientView view;
    //private final User user;
//    private final Order order;

    public OrderListClientController(){
        this.view = new OrderListClientView(this, getUser());
    }
//    public OrderListClientController(){
//        //this.user = loggedUser;
//        this.view = new OrderListClientView(this,getUser());
//        //this.order = order;
//    }
    // pour pouvoir avancer en attendant que je fasse le lien avec le login
    public User getUser(){
        return Security.getLoggedUser();
    }

//    public Order getOrder() {
//        return order;
//    }
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

    public void openActions(Order order) {
        //  Crée contrôleur + vue actions sur la commande
        var ctrl = new OrderCheckController(order);
        var win = ctrl.getView(); // vue OrderDetailsView (BasicWindow)

        ctrl.setOnCloseCallBack(() -> view.refreshOrders());

        //  Configure l’affichage pour qu’elle s’ouvre par-dessus
        win.setHints(java.util.Arrays.asList(
                Window.Hint.MODAL,     // bloque les interactions dessous
                Window.Hint.CENTERED   // centre la fenêtre
        ));
        //  Affiche la fenêtre par-dessus sans fermer celle de la liste
        showModal(win);
    }

    public void openCreateNewOrder() {
        var newOrder = new Order(); // nouvelle commande vide
        var ctrl = new OrderCreateController(newOrder);
        var win = ctrl.getView();

        win.setHints(java.util.Arrays.asList(
                Window.Hint.MODAL,
                Window.Hint.CENTERED
        ));

        showModal(win);
    }

//    public void openCreateNewOrder(){
//
//        var ctrl = new OrderCreateController(order);
//        var win = ctrl.getView();
//
//        win.setHints(java.util.Arrays.asList(
//                Window.Hint.MODAL,     // bloque les interactions dessous
//                Window.Hint.CENTERED   // centre la fenêtre
//        ));

//        showModal(win);
//    }
    public void exit(){
        Security.login(null);
        navigateTo(new SecurityLoginController());
    }

    public void basket(){
        navigateTo(new PanierController() {
        });
    }

    public void menu(){
        navigateTo(new MenuController());
    }

    public void reservation() { navigateTo(new ReservationController()); }

    public void changeMenu(){
        navigateTo(new CartMenuManagementController());
    }

    public void closing(){
        navigateTo(new ClosingChangeController());
    }

    public void changeUser(){
        navigateTo(new UserChangeController());
    }

    public void order(){
        navigateTo(new OrderCreateController(new Order()));
    }

    public void clientRegister(){
        navigateTo(new ClientRegisterController());
    }

    public void tables(){
        navigateTo(new TablesCheckController());
    }

    // Method verifying if a reservation id exists in Order Tabel (by calling DoesReservationExists method in Reservation Model)
    public boolean ReservationWithOrder (int rID) {
        return Reservation.DoesReservationExists(rID) != null;
    }

    public void deleteReservation(Reservation r){
        if (askConfirmation("Vous étes sur de vouloir supprimer","suppression"))
            r.delete();
    }
}

