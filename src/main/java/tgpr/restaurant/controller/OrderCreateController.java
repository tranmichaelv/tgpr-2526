package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.view.OrderCreateView;

public class OrderCreateController extends Controller<OrderCreateView> {

    /**
     * Default constructor, takes a title for the dialog and runs code shared for dialogs
     *
     * //@param title Title of the window
     */

    public OrderCreateView getView() {
        return new OrderCreateView(this);
    }
}
