package tgpr.restaurant.view;

import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.MenuCheckController;
import tgpr.restaurant.model.PanelProductAndMenu;
import tgpr.restaurant.model.Product;

public class MenuCheckView extends PanelProductAndMenu<Product> {
    private final MenuCheckController controller;

    public MenuCheckView(MenuCheckController controller) {
        this.controller = controller;

        initTable(6);
        createButtonAddPanel("Ajouter menu", this::addNewMenu);

    }

    // charge les donnes
    @Override
    public void loadData() {
        // demande au contrôleur la liste des produits
        var menus = controller.listMenu();
        // ajoute l'ensemble des produits au tableau
        tableProduct.add(menus);
    }

    // crée une table avec differentes colonnes
    @Override
    public ObjectTable<Product> createTable() {
        return new ObjectTable<>(
                new ColumnSpec<>("Nom", Product::getName),
                new ColumnSpec<>("Prix", Product::getPriceFormatted),
                new ColumnSpec<>("Description", Product::getDescription)
        );
    }

    // spécifie l'action à exécuter quand on presse ENTER sur une ligne
    @Override
    public void onProductSelected() {
        // récupère l'objet couramment sélectionné en naviguant dessus
        var product = tableProduct.getSelected();
        // va vers le controlleur qui permet de changer le menu
        controller.MenuChange(product);
        // stocke les changements dans la liste et le renvoi
        reloadData();
        //  repart sur la ligne de l'élément stocké
        tableProduct.setSelected(product);
    }

    // ajoute un nouveau menu
    public void addNewMenu(){
        controller.addNewMenu();
        reloadData();
    }
}
