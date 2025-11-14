package tgpr.restaurant.controller;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Window;
import tgpr.framework.Controller;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.view.OrderCheckView;

import java.util.Arrays;

public class OrderCheckController extends Controller<OrderCheckView> {

    private final Order order;
    private final OrderCheckView view;
    private Runnable onCloseCallBack;

    public OrderCheckController(Order order) {
        this.order = order;
        this.view = new OrderCheckView(this, order);
    }

    @Override
    public OrderCheckView getView() {
        return view;
    }

    public void openChangeView() {
        var ctrl = new OrderChangeController(order);
        var win = ctrl.getView();
        showModal(win);
    }

    public void openRegisterPaymentView() {
        var ctrl = new OrderPayController(order);
        var win = ctrl.getView();
        showModal(win);
    }

    public void openDetailsView() {
        var ctrl = new OrderDetailsController(order);
        var win = ctrl.getView();
        showModal(win);
    }

    public void openCancelOrderView() {
        var ctrl = new OrderDeleteController(order);
        var win = ctrl.getView();
        showModal(win);
    }

    private void showModal(BasicWindow win) {
        win.setHints(Arrays.asList(
                Window.Hint.MODAL,
                Window.Hint.CENTERED
        ));
        // Je passe par la vue pour accéder au TextGUI
        view.getTextGUI().addWindowAndWait(win);
    }

    public void setOnCloseCallBack(Runnable callBack){
        this.onCloseCallBack = callBack;
    }
}