package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.Product;
import tgpr.restaurant.view.MenuCheckView;

import java.util.List;

public class MenuCheckController extends Controller<MenuCheckView> {

    @Override
    public MenuCheckView getView() {
        return new MenuCheckView(this);
    }

    // retourne la liste de tous les menus
    public List<Product> listMenu(){
        return Product.getOrderMenu();
    }

    // va vers le controlleur qui permet de modifier le produit sélectionné
    public void MenuChange(Product product){
        navigateTo(new MenuChangeController(product));
    }

    public void addNewMenu(){
        //...
        //...
        //...
    }

}
