package tgpr.restaurant.view;

import tgpr.restaurant.controller.ProductChangeController;
import tgpr.restaurant.model.DialogWindowProductAndMenu;
import tgpr.restaurant.model.Product;

import java.util.List;

public class ProductChangeView extends DialogWindowProductAndMenu<ProductChangeController> {

    private static final List<Product.Type> SortableFields =
            List.of(Product.Type.Starter, Product.Type.Main, Product.Type.Dessert, Product.Type.Drink);

    public ProductChangeView(ProductChangeController controller, Product product) {
        super(controller.isNew() ? "Nouveau produit" : "Modifier le produit",
                controller, product);

    }

    @Override
    public List<Product.Type> getAvailableTypes() {
        return SortableFields;
    }


}
