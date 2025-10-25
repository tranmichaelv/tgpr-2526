package tgpr.restaurant.controller;

import com.googlecode.lanterna.gui2.Panel;
import tgpr.framework.Controller;
import tgpr.restaurant.model.Product;
import tgpr.restaurant.view.CartMenuManagementView;


public class CartMenuManagementController extends Controller<CartMenuManagementView>{

    @Override
    public CartMenuManagementView getView() {
        return new CartMenuManagementView(this);
    }

    public Panel getProductCheckView(){
        ProductCheckController productCheckController = new ProductCheckController();
        return productCheckController.getView();
    }

    public Panel getMenuCheckView(){
        MenuCheckController menuCheckController = new MenuCheckController();
        return menuCheckController.getView();
    }

    public void addNewMenu(){

    }

}
