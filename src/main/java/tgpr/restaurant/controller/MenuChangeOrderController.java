package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.MenuItem;
import tgpr.restaurant.view.MenuActionsView;
import tgpr.restaurant.view.MenuChangeOrderView;

public class MenuChangeOrderController extends Controller<MenuChangeOrderView> {
    private MenuChangeOrderView view;

    public MenuChangeOrderController(String menuName, int productOrderInMenu) {
        view = new MenuChangeOrderView(this, menuName, productOrderInMenu);
    }
    @Override
    public MenuChangeOrderView getView() {
        return view;
    }
}
