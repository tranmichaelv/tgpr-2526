package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.framework.ErrorList;
import tgpr.restaurant.model.MenuItem;
import tgpr.restaurant.model.MenuItemValidator;
import tgpr.restaurant.view.MenuChangeOrderView;

import java.util.List;

public class MenuChangeOrderController extends Controller<MenuChangeOrderView> {
    private final MenuChangeOrderView view;
    private final MenuItem menu;

    public MenuChangeOrderController(MenuItem menu) {
        this.menu = menu;
        view = new MenuChangeOrderView(this, menu.getProductName(), menu.getOrderInMenu());
    }
    @Override
    public MenuChangeOrderView getView() {
        return view;
    }

    public void changeOrder(String numberToChange){
        int curentOrderInMenu = menu.getOrderInMenu();
        int number = Integer.parseInt(numberToChange);
        int temporaryNumber = 99999;
        // sauvegarde temporaire pour éviter conflit unique
        menu.setOrderInMenu(temporaryNumber);
        menu.save();
        List<MenuItem> list = MenuItem.getAllInMenu(menu.getMenuId());

        if(number <= curentOrderInMenu) {
            for (int i = list.size() - 2; i >= number - 1; i--) {
                list.get(i).setOrderInMenu(i + 2);
                list.get(i).save();
            }
        } else {
            for(int i = curentOrderInMenu - 1; i < number; i++){
                list.get(i).setOrderInMenu(i + 1);
                list.get(i).save();
            }
        }
        menu.setOrderInMenu(number);
        menu.save();
    }

    public ErrorList check(String numberOrder){
        List<MenuItem> nbProductInList = MenuItem.getAllInMenu(menu.getMenuId());

        var errors = new ErrorList();
        errors.add(MenuItemValidator.isValidNumberOrder(numberOrder, nbProductInList.size()));

        return errors;
    }
}
