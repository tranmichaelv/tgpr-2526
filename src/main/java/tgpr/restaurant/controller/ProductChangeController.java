package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.framework.ErrorList;
import tgpr.restaurant.model.Product;
import tgpr.restaurant.model.ProductValidator;
import tgpr.restaurant.view.ProductChangeView;

import java.util.List;

public class ProductChangeController extends Controller<ProductChangeView> {
    private final ProductChangeView view;
    private final Product product;

    // mets le produit en parametre pour savoir de quel produit on modifie
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

    public List<Product> list(){
        return Product.getOrderProduct();
    }

    public void save(String txtname, String txtdescription,String txtprice,Product.Type type){
        product.setName(txtname);
        product.setDescription(txtdescription);
        product.setPrice(Double.parseDouble(txtprice));
        product.setType(type);
        product.save();
    }

    public void delete(){
        product.delete();
    }

    public void archive(){
        product.setArchived(true);
        product.save();
    }

    public ErrorList checkName(String name){
        var errors = new ErrorList();
        errors.add(ProductValidator.isValidName(name));
        return errors;
    }

    public ErrorList checkPrice(String price){
        var errors = new ErrorList();
        errors.add(ProductValidator.isValidPrice(price));
        return errors;
    }

    public ErrorList checkDescription(String description){
        var errors = new ErrorList();
        errors.add(ProductValidator.isValidDescription(description));
        return errors;
    }
}
