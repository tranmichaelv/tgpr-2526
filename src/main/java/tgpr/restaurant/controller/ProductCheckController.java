package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.OrderItem;
import tgpr.restaurant.model.Product;
import tgpr.restaurant.view.ProductCheckView;

import java.util.List;

public class ProductCheckController extends Controller<ProductCheckView> {
    int order;
    private final String string; // permet de savoir sur quelle vue on se trouve

    private Runnable onOrderChanged;

    public void setOnOrderChanged(Runnable r) { this.onOrderChanged = r; }

    public ProductCheckController(int order){
            this.order = order;
            this.string = "";
    }

    public ProductCheckController(String string){
        this.string = string;
    }

    @Override
    public ProductCheckView getView() {
        return new ProductCheckView(this, order, string);
    }

    // tous les produits != menu
    public List<Product> listProduct(){
        return Product.getOrderProductAll();
    }

    // tous les produits non archived
    public List<Product> listProductAndMenu(){
        return Product.getOrderProductAndMenu();
    }

    // changer de produit
    public void productChange(Product product){
        navigateTo(new ProductChangeController(product));
    }

    // ajouter un nouveau produit
    public void addNewProduct(){
        navigateTo(new ProductChangeController(new Product()));

    }

    // ajoute un produit ou modifie la quantité
    public void addOrSetQuantity(Product p, int qty, OrderItem orderItem, int order) {
        // si mon produit se trouve dans ma commande
        // en mettant la quantité à 0 je le supprime
        if (qty == 0) {
            if (orderItem != null)
                orderItem.delete();
        } else {
            if (orderItem == null) {
                OrderItem it = new OrderItem();
                it.setOrderId(order);
                it.setProductId(p.getId());
                it.setQuantity(qty);
                // prix au moment de la commande
                it.setPrice(p.getPrice());
                it.save();
            } else {
                orderItem.setQuantity(qty);
                orderItem.save();
            }

        }
        if (onOrderChanged != null) {
            onOrderChanged.run();
        }
    }
}
