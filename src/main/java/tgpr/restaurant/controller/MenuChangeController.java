package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.framework.ErrorList;
import tgpr.restaurant.model.MenuItem;
import tgpr.restaurant.model.MenuItemValidator;
import tgpr.restaurant.model.Product;
import tgpr.restaurant.view.MenuChangeView;

import java.util.List;


public class MenuChangeController extends Controller<MenuChangeView> {
    private final MenuChangeView view;
    private final Product menu;

    public MenuChangeController(Product product) {
        this.menu = product;
        view = new MenuChangeView(this, product);
    }

    @Override
    public MenuChangeView getView() {
        return view;
    }

    public void deleteMenu(){
        this.menu.delete();
    }

    public void archiveMenu(){
        this.menu.setArchived(true);
        this.menu.save();
    }

    public List<MenuItem> listMenuItems(){
        return menu.getMenuMenuItems();
    }

    public void menuActions(MenuItem menu){
        navigateTo(new MenuActionsController(menu));
    }

    public double totalPriceMenu(){
        double price = 0;
        for(MenuItem product : getAllProductInMenu()){
            price += product.getProduct().getPrice();
        }
        return price;
    }

    public List<Product> listProduct(){
        return Product.getOrderProduct();
    }

    public int getProductId(){
        return menu.getId();
    }

    public int numberOfProductsInMenu(){
        return getAllProductInMenu().size();
    }

    public void addProductToMenu(int productToAddId){
        MenuItem newMenu = new MenuItem(getProductId(), productToAddId, numberOfProductsInMenu() + 1);
        newMenu.save();
    }

    public ErrorList checkDouble(Product selectedProduct){
        var errors = new ErrorList();
        errors.add(MenuItemValidator.checkDouble(selectedProduct ,getAllProductInMenu()));
        return errors;
    }

    public List<MenuItem> getAllProductInMenu(){
        return MenuItem.getAllInMenu(menu.getId());
    }

    public void changeMenu(){
        navigateTo(new MenuAddControler(menu));

        if(menu.getArchived() || !menu.existsInDatabase()){
            view.close();
        }
    }

}
