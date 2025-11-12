package tgpr.restaurant.view;

import tgpr.restaurant.controller.MenuAddControler;
import tgpr.restaurant.model.DialogWindowProductAndMenu;
import tgpr.restaurant.model.Product;

import java.util.List;

public class MenuAddView extends DialogWindowProductAndMenu<MenuAddControler> {

    private static final List<Product.Type> SortableFields = List.of(Product.Type.Menu);

    public MenuAddView(MenuAddControler controller, Product product) {
        super(controller.isNew() ? "Nouveau menu" : "Modifier le menu",
                controller, product);
    }

    @Override
    public List<Product.Type> getAvailableTypes() {
        return SortableFields;
    }


}
