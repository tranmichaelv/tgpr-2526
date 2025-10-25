package tgpr.restaurant.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Panel;
import tgpr.framework.ViewManager;
import tgpr.restaurant.controller.ProductCheckController;
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




public class ProductCheckView  extends Panel{
    private final ProductCheckController controller;
    private ObjectTable<Product> tableProduct;

    public ProductCheckView(ProductCheckController controller) {
        this.controller = controller;

        this.asVerticalPanel(1);

        creaTable().addTo(this);

        // si le patron alors, il peut ajouter de nouveaux products
        if(Security.isBoss()) {
            createButtonAddPanel().addTo(this);
        }

    }

    // envoie les donnes des produits
    // dans une table et les mets à chaque appel de cette classe
    public void reloadData() {
        // vide le tableau
        tableProduct.clear();
        if(Security.isBoss()) {
            // demande au contrôleur la liste des produits
            var products = controller.listProduct();
            // ajoute l'ensemble des produits au tableau
            tableProduct.add(products);
        } else {
            var products = controller.listProductAndMenu();
            tableProduct.add(products);
        }
        // requis à cause d'un bug quand on ferme la fenêtre popup avec la touche Escape
        tableProduct.invalidate();
    }

    private Component creaTable() {
        // crée une table avec differentes colonnes
        tableProduct = new ObjectTable<>(
                new ColumnSpec<>("Nom", Product::getName),
                new ColumnSpec<>("Type", Product::getType),
                new ColumnSpec<>("Prix", Product::getPriceFormatted),
                new ColumnSpec<>("Description", Product::getDescription)
        );
        // donne une taille à la table
        tableProduct.sizeTo(ViewManager.getTerminalColumns(), 6);

        // spécifie l'action à exécuter quand on presse Enter ou la barre d'espace
        tableProduct.setSelectAction(() -> {

                // si l'user est le boss
                if (Security.isBoss()){

                // récupère l'objet couramment sélectionné en naviguant dessus
                var product = tableProduct.getSelected();
                // va vers le controlleur qui permet de changer le produit
                controller.productChange(product);
                // stocke les changements dans la liste et le renvoi
                reloadData();
                //  repart sur la ligne de l'élément stocké
                tableProduct.setSelected(product);

                // si pas patron alors le client doit pouvoir ajoute
                } else {
                    //...
                    //...
                    //...
                }
            });

        // charge les donnes dans la table
        reloadData();

        return tableProduct;
    }

    private Panel createButtonAddPanel(){
        Panel panel = new Panel();
        Button btnAddNewProduct = new Button("Ajouter produit", this::addNewProduct);
        panel.addComponent(btnAddNewProduct);

        return panel;
    }

    public void addNewProduct(){
        controller.addNewProduct();
        reloadData();
    }


}
