package tgpr.restaurant.controller;

import com.googlecode.lanterna.gui2.*;
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

    public void changeOrderItem(String titre){
        BasicWindow popup = new BasicWindow();
        popup.setTitle(titre);
        popup.setHints(java.util.List.of(Window.Hint.CENTERED));



        Panel panel = Panel.gridPanel(1,
                Margin.of(1,1,1,1),
                Spacing.of(1));

        ActionListBox list = new ActionListBox();
        for(MenuItem product : listMenuItems()){
            list.addItem(product.getProductName(), () -> {
                if(titre.contains("Supprimer")){
                    int orderInMenu = product.getOrderInMenu();
                    product.delete();
                    List<MenuItem> menuList = MenuItem.getAllInMenu(product.getMenuId());
                    for (int i = orderInMenu - 1; i < menuList.size(); i++) {
                        MenuItem currentMenu = menuList.get(i);
                        currentMenu.setOrderInMenu(i + 1); // si orderInMenu commence à 1
                        currentMenu.save();
                    }
                    popup.close();
                } else {
                    navigateTo(new MenuChangeOrderController(product));
                    popup.close();
                }
            });
        }
        panel.addComponent(list);
        panel.addComponent(new Button("Fermer", popup::close));
        popup.setComponent(panel);
        tgpr.framework.ViewManager.gui.addWindowAndWait(popup);

    }

    public void deleteItem(){

    }

}
