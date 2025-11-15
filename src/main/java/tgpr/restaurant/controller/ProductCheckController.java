package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.framework.Params;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.model.OrderItem;
import tgpr.restaurant.model.Product;
import tgpr.restaurant.view.OrderChangeView;
import tgpr.restaurant.view.ProductCheckView;

import java.util.List;

public class ProductCheckController extends Controller<ProductCheckView> {
    int order;
    String string;

    //    private  OrderChangeController parent;
    private Runnable onOrderChanged;
//
//    public ProductCheckController(OrderChangeController parent) { this.parent = parent; }
//
//    public ProductCheckController() {
//        this.parent = null;
//    }
//    // sert à mettre à jour mes produits de la commande
    public void setOnOrderChanged(Runnable r) { this.onOrderChanged = r; }
//    public Order getOrder() { return parent.getOrder(); }

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

    // retourne la liste de tous les produits
    public List<Product> listProduct(){
        return Product.getOrderProductAll();
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
    // cherche le produit
//    public OrderItem findItem(Product p) {
//        return OrderItem.queryOne(
//                OrderItem.class,
//                "select * from order_items where `order`=:o and product=:p",
//                new Params("o", getOrder().getId()).add("p", p.getId())
//        );
//    }

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
//            getOrder().reload();
            // refreshOrders la zone ou les produits sont affichés
//            if (onOrderChanged != null) onOrderChanged.run();
        }
        if (onOrderChanged != null) {
            onOrderChanged.run();
        }
    }

//        //  supprime un produit de la commande
//        public void removeProduct(Product p) {
//            OrderItem existing = findItem(p);
//            if (existing == null) return;
//            existing.delete();
//
//            getOrder().reload();
//            if (onOrderChanged != null) onOrderChanged.run();
//        }


}
