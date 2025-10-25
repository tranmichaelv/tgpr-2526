package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.MenuItem;
import tgpr.restaurant.view.MenuActionsView;

import java.util.List;


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
        int orderInMenu = menu.getOrderInMenu();
        menu.delete();
        List<MenuItem> menuList = getMenuList();
        if(menuList.size() > 1){
            for(int i = orderInMenu - 1; i <= menuList.size(); ++i ){
                MenuItem currentMenu =  menuList.get(i);
                currentMenu.setOrderInMenu(i);
                currentMenu.save();
            }
        }
    }

    public List<MenuItem> getMenuList(){
        return MenuItem.getAllInMenu(menu.getMenuId());
    }
    public void changeOrder(){
        navigateTo(new MenuChangeOrderController(menu));
    }

}
