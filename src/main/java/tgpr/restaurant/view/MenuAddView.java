package tgpr.restaurant.view;

import tgpr.restaurant.controller.MenuAddController;
import tgpr.restaurant.model.DialogWindowProductAndMenu;
import tgpr.restaurant.model.Product;

import java.util.List;

public class MenuAddView extends DialogWindowProductAndMenu<MenuAddController> {
    private static final List<Product.Type> SortableFields = List.of(Product.Type.Menu);

    public MenuAddView(MenuAddController controller, Product product) {
        super(controller.isNew() ? "Nouveau menu" : "Modifier le menu",
                controller, product);
    }

    @Override
    public List<Product.Type> getAvailableTypes() {
        return SortableFields;
    }


}
