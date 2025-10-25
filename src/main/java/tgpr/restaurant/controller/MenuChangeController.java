package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.MenuItem;
import tgpr.restaurant.model.Product;
import tgpr.restaurant.view.MenuChangeView;

import java.util.List;


public class MenuChangeController extends Controller<MenuChangeView> {
    private final MenuChangeView view;
    private final Product product;
    private List<MenuItem> menu;

    public MenuChangeController(Product product) {
        this.product = product;
        this.menu = product.getMenuMenuItems();
        view = new MenuChangeView(this, product);
    }

    @Override
    public MenuChangeView getView() {
        return view;
    }

//    public void deleteItem(){
//        this.menu.delete();
//    }

    public void deleteMenu(){
        this.product.delete();
    }

//    public void save(){
//        this.menu.save();
//    }

    public void archiveMenu(){
        this.product.setArchived(true);
        this.product.save();
    }

    public List<MenuItem> listMenuItems(){
        return product.getMenuMenuItems();
    }

    public void menuActions(MenuItem menu){
        navigateTo(new MenuActionsController(menu));
    }

    public double totalPriceMenu(){
        double price = 0;
        for(MenuItem product : menu){
            price += product.getProduct().getPrice();
        }
        return price;
    }

    public List<Product> listProduct(){
        return Product.getOrderProduct();
    }

    public int getProductId(){
        return product.getId();
    }

    public int numberOfProductsInMenu(){
        return menu.size();
    }

    public void addProductToMenu(int productToAddId){
        this.menu = product.getMenuMenuItems();
        MenuItem newMenu = new MenuItem(getProductId(), productToAddId, numberOfProductsInMenu() + 1);
        newMenu.save();
    }
}
