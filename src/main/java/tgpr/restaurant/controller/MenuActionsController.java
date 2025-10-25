package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.MenuItem;
import tgpr.restaurant.view.MenuActionsView;
import tgpr.restaurant.view.MenuChangeView;

import javax.swing.text.View;


public class MenuActionsController extends Controller<MenuActionsView> {
    private final MenuItem menu;
    private final MenuActionsView view;

    public MenuActionsController(MenuItem menu) {
        this.menu = menu;
        view = new MenuActionsView(this, menu);
    }

    @Override
    public MenuActionsView getView() {
        return view;
    }

    public void delete(){
        menu.delete();
    }

    public void changeOrder(){
        navigateTo(new MenuChangeOrderController(menu.getMenuName(), menu.getOrderInMenu()));
    }

}
