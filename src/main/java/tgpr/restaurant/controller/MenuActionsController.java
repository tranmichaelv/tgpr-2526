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
        // orderInMenu de la ligne a supprimer
        int orderInMenu = menu.getOrderInMenu();
        // supprime la ligne
        menu.delete();
        // list des lignes restantes
        List<MenuItem> menuList = getMenuList();
        // change l'orderInMenu des lignes restantes en fonctions de la ligne supprime
        if(menuList.size() > 1){
            for(int i = orderInMenu - 1; i <= menuList.size(); ++i ){
                MenuItem currentMenu =  menuList.get(i);
                currentMenu.setOrderInMenu(i);
                currentMenu.save();
            }
        }
    }
    // recupere le menu avec tous ses produits
    public List<MenuItem> getMenuList(){
        return MenuItem.getAllInMenu(menu.getMenuId());
    }
    // change l'ordre
    public void changeOrder(){
        navigateTo(new MenuChangeOrderController(menu));
    }

}
