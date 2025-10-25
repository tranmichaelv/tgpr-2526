package tgpr.restaurant.view;

import com.googlecode.lanterna.gui2.*;
import tgpr.framework.ViewManager;
import tgpr.restaurant.controller.MenuCheckController;
import tgpr.restaurant.model.Product;

public class MenuCheckView extends Panel{
    private final MenuCheckController controller;
    private ObjectTable<Product> tableProduct;

    public MenuCheckView(MenuCheckController controller) {
        this.controller = controller;

        this.asVerticalPanel(1);

        creaTable().addTo(this);

        createButtonAddPanel().addTo(this);


    }

    // envoie les donnes des produits
    // dans une table et les mets à chaque appel de cette classe
    public void reloadData() {
        // vide le tableau
        tableProduct.clear();
        // demande au contrôleur la liste des produits
        var menus = controller.listMenu();
        // ajoute l'ensemble des produits au tableau
        tableProduct.add(menus);
        // requis à cause d'un bug quand on ferme la fenêtre popup avec la touche Escape
        tableProduct.invalidate();
    }

    private Component creaTable() {
        // crée une table avec differentes colonnes
        tableProduct = new ObjectTable<>(
                new ColumnSpec<>("Nom", Product::getName),
                new ColumnSpec<>("Prix", Product::getPriceFormatted),
                new ColumnSpec<>("Description", Product::getDescription)
        );
        // donne une taille à la table
        tableProduct.sizeTo(ViewManager.getTerminalColumns(), 4);

        // spécifie l'action à exécuter quand on presse Enter ou la barre d'espace
        tableProduct.setSelectAction(() -> {
            // récupère l'objet couramment sélectionné en naviguant dessus
            var product = tableProduct.getSelected();
            // va vers le controlleur qui permet de changer le produit
            controller.MenuChange(product);
            // stocke les changements dans la liste et le renvoi
            reloadData();
            //  repart sur la ligne de l'élément stocké
            tableProduct.setSelected(product);
        });

        // charge les donnes dans la table
        reloadData();

        return tableProduct;
    }

    private Panel createButtonAddPanel(){
        Panel panel = new Panel();
        Button btnAddNewProduct = new Button("Ajouter menu", this::addNewMenu);
        panel.addComponent(btnAddNewProduct);

        return panel;
    }

    public void addNewMenu(){
        controller.addNewMenu();
        reloadData();
    }
}
