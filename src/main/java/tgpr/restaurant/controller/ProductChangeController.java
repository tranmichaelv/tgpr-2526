package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.Product;
import tgpr.restaurant.view.ProductChangeView;

public class ProductChangeController extends Controller<ProductChangeView> {
    private final ProductChangeView view;
    private final Product product;

    // mets le produit en paramètre pour savoir de quel produit on modifie
    public ProductChangeController(Product product) {
        // produit a modifier
        this.product = product;
        view = new ProductChangeView(this,product);

    }

    @Override
    public ProductChangeView getView() {
        return view;
    }

    public Boolean isNew() {
        return product.getName() == null ;
    }
}
