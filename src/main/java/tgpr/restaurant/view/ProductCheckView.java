package tgpr.restaurant.view;

import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.ProductCheckController;
import tgpr.restaurant.model.PanelProductAndMenu;
import tgpr.restaurant.model.Product;
import tgpr.restaurant.model.Security;


/// Pour utiliser ce code, il faut appeler le controlleur
/// qui va retourner le panel de la vue :
///
///         Dans le controller :
///         ProductCheckController productCheckController = new ProductCheckController();
///         return productCheckController.getView();
///
///
///         root.addComponent(controller.getProductCheckView());




public class ProductCheckView extends PanelProductAndMenu<Product> {
    private final ProductCheckController controller;
    private final String view;

    public ProductCheckView(ProductCheckController controller, String view) {
        this.controller = controller;
        this.view = view;

        initTable(6);

        // si le patron alors, il peut ajouter de nouveaux products
        if(Security.isBoss()) {
            createButtonAddPanel("Ajouter produit", this::addNewProduct);
        }

    }

    // charge les donnes
    @Override
    public void loadData() {
        if(view.equals("CarteMenuManagementView")) {
            // demande au contrôleur la liste des produits
            var products = controller.listProduct();
            // ajoute l'ensemble des produits au tableau
            tableProduct.add(products);
        } else {
            // // demande au contrôleur la liste des produits et menu
            var products = controller.listProductAndMenu();
            // ajoute l'ensemble des produits au tableau
            tableProduct.add(products);
        }
    }

    // crée une table avec differentes colonnes
    @Override
    public ObjectTable<Product> createTable() {
        return new ObjectTable<>(
                new ColumnSpec<>("Nom", Product::getName),
                new ColumnSpec<>("Type", Product::getType),
                new ColumnSpec<>("Prix", Product::getPriceFormatted),
                new ColumnSpec<>("Description", Product::getDescription)
        );
    }

    // spécifie l'action à exécuter quand on presse ENTER sur une ligne
    @Override
    public void onProductSelected() {
        // si l'user est le boss
        if (view.equals("CarteMenuManagementView")) {
            // récupère l'objet couramment sélectionné en naviguant dessus
            var product = tableProduct.getSelected();
            // va vers le controlleur qui permet de changer le produit
            controller.productChange(product);
            // stocke les changements dans la liste et le renvoi
            reloadData();
            // repart sur la ligne de l'élément stocké
            tableProduct.setSelected(product);
            // si pas patron alors le client doit pouvoir ajoute un nombre de product qu'il veut
        } else {
            //...
            //...
            //...
        }
    }

    // ajoute un nouveau produit
    public void addNewProduct(){
        controller.addNewProduct();
        reloadData();
    }

}
