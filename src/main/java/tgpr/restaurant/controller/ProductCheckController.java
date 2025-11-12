package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.Product;
import tgpr.restaurant.view.ProductCheckView;

import java.util.List;

public class ProductCheckController extends Controller<ProductCheckView> {

    @Override
    public ProductCheckView getView() {
        return new ProductCheckView(this,"CarteMenuManagementView");
    }

    // retourne la liste de tous les produits
    public List<Product> listProduct(){
        return Product.getOrderProduct();
    }

    public List<Product> listProductAndMenu(){
        return Product.getOrderProductAndMenu();
    }

    // va vers le controlleur qui permet de modifier le produit sélectionné
    public void productChange(Product product){
        navigateTo(new ProductChangeController(product));
    }

    public void addNewProduct(){
        navigateTo(new ProductChangeController(new Product()));

    }

}
