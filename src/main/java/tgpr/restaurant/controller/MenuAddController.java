package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.Product;
import tgpr.restaurant.view.MenuAddView;

public class MenuAddController extends Controller<MenuAddView> {
    private final MenuAddView view;
    private final Product product;

    public MenuAddController(Product product) {
        this.product = product;
        view = new MenuAddView(this, product);
    }

    @Override
    public MenuAddView getView() {
        return view;
    }
    // retourne nouveau menu ?
    public Boolean isNew() {
        return product.getName() == null ;
    }
}
