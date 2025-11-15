package tgpr.restaurant.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialogBuilder;
import tgpr.restaurant.controller.ProductCheckController;
import tgpr.restaurant.model.OrderItem;
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

    public ProductCheckView(ProductCheckController controller, int order, String string) {
        this.controller = controller;
        this.view = string;

        initTable(6);
        if(Security.isBoss() && view.equals("CarteMenuManagementView"))
            createButtonAddPanel("Ajouter produit", this::addNewProduct);
        // action à éxecuter quand on appuie enter
        tableProduct.setSelectAction(() -> {
            // récupère le produit sélectionné
            var product = tableProduct.getSelected();
            if (product == null) return;

            // Si le rôle est boss ou employé : modification
            if ((Security.isBoss() && view.equals("CarteMenuManagementView"))) {
                controller.productChange(product);
                reloadData();
                tableProduct.setSelected(product);
            } else {
                OrderItem existing = null;
                // 0 autorisé si déjà présent suppression
                for(OrderItem productInOrder : OrderItem.getAllItemInOrder(order)){
                    if(product.equals(productInOrder.getProduct())){
                        existing = productInOrder;
                    }
                }
                Integer qty = askQuantity(
                        "Quantité",
                        "Quantité pour " + product.getName() + " :",
                        existing == null ? 1 : existing.getQuantity(),
                        // autorise 0 seulement si déjà présent
                        existing != null
                );
                //  Cancel
                if (qty == null) return;
                // si qty = 0 on supprime, si > 0 on ajoute ou met à jour la qty
                controller.addOrSetQuantity(product, qty, existing,order);
            }
        });
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
        if(Security.isBoss() && view.equals("CarteMenuManagementView")){
            return new ObjectTable<>(
                    new ColumnSpec<>("Archived", p -> p.getArchived() ? "X" : "" ),
                    new ColumnSpec<>("Nom", Product::getName),
                    new ColumnSpec<>("Type", Product::getType),
                    new ColumnSpec<>("Prix", Product::getPriceFormatted),
                    new ColumnSpec<>("Description", Product::getDescription)
            );
        } else {
            return new ObjectTable<>(
                    new ColumnSpec<>("Nom", Product::getName),
                    new ColumnSpec<>("Type", Product::getType),
                    new ColumnSpec<>("Prix", Product::getPriceFormatted),
                    new ColumnSpec<>("Description", Product::getDescription)
            );
        }
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

    // demande la quantité pour un produit / menu
    // doit etre positif sauf pour supprimer un produit déjà présent dans ma commande
    private Integer askQuantity(String title, String desc, int initial, boolean allowZero) {
        String pattern = allowZero ? "^(?:0|[1-9]\\d*)$" : "^[1-9]\\d*$";
        String s = new TextInputDialogBuilder()
                .setTitle(title)
                .setDescription(desc)
                .setInitialContent(String.valueOf(initial))
                .setValidationPattern(java.util.regex.Pattern.compile(pattern),
                        allowZero ? "Entrez 0 ou un entier positif." : "Entrez un entier positif.")
                .build()
                .showDialog((WindowBasedTextGUI) getTextGUI());
        return (s == null) ? null : Integer.parseInt(s);
    }

}
