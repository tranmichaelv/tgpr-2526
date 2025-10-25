package tgpr.restaurant.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.framework.ViewManager;
import tgpr.restaurant.controller.MenuChangeController;
import tgpr.restaurant.model.MenuItem;
import tgpr.restaurant.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuChangeView extends DialogWindow {
    private final Product menu;
    private final MenuChangeController controller;
    private ObjectTable<MenuItem> tableProduct;
    private ComboBox<String> cboAddProduct;
    private final Map<String, Product> productMap = new HashMap<>();


    public MenuChangeView(MenuChangeController controller, Product menu) {
        super("Gerer les composant du menu: " + menu.getName());
        this.menu = menu;
        this.controller = controller;

        setHints(List.of(Hint.CENTERED, Hint.MODAL));
        setCloseWindowWithEscape(true);

        Panel root = Panel.gridPanel(1, Margin.of(0,1,0,1));

        setComponent(root);

        setComponent(root);

        creaTable().addTo(root);
        createPricePanel().addTo(root);
        root.addEmpty();
        createAddProductPanel().addTo(root);
        root.addEmpty();
        createButtonPanel().addTo(root);


    }

    public void reloadData() {
        // vide le tableau
        tableProduct.clear();
        // demande au contrôleur la liste des produits
        var products = controller.listMenuItems();
        // ajoute l'ensemble des produits au tableau
        tableProduct.add(products);
        // requis à cause d'un bug quand on ferme la fenêtre popup avec la touche Escape
        tableProduct.invalidate();
    }

    private Component creaTable() {
        // crée une table avec differentes colonnes
        tableProduct = new ObjectTable<>(
                new ColumnSpec<>("Produit", MenuItem::getProductName),
                new ColumnSpec<>("Type", MenuItem::getProductType),
                new ColumnSpec<>("Prix", MenuItem::getProductPriceFormatted),
                new ColumnSpec<>("Ordre", MenuItem::getOrderInMenu)
        );
        // donne une taille à la table
        tableProduct.sizeTo(60, 8);

        // spécifie l'action à exécuter quand on presse Enter ou la barre d'espace
        tableProduct.setSelectAction(() -> {

            // récupère l'objet couramment sélectionné en naviguant dessus
            var product = tableProduct.getSelected();
            // va vers le controlleur qui permet de changer le produit
            controller.menuActions(product);
            // stocke les changements dans la liste et le renvoi
            reloadData();
            //  repart sur la ligne de l'élément stocké
            tableProduct.setSelected(product);
        });

        // charge les donnes dans la table
        reloadData();

        return tableProduct;
    }

    private Panel createPricePanel(){
        return Panel.verticalPanel(LinearLayout.Alignment.Beginning,
                new Label("─".repeat(60)),
                new Label("Total de produits:" + totalPriceMenu() + "0 €"),
                new Label("Prix du menu: " + menu.getPrice() + "0 €"),
                new Label("Difference (avantage client): " + (totalPriceMenu() - menu.getPrice()) + "0 €"),
                new Label("─".repeat(60))
        );
    }

    private Panel createAddProductPanel(){
        cboAddProduct = new ComboBox<>();
        for (Product p : controller.listProduct()) {
            String display = p.getName() + " (" + p.getType() + ")";
            productMap.put(display, p);
            cboAddProduct.addItem(display);
        }



        Panel panel = Panel.horizontalPanel(3);

        panel.addComponent(new Label("Ajouter produit:"));
        panel.addComponent(cboAddProduct);
        panel.addComponent(new Button("Ajouter",this::addProductToMenu));

        return panel;
    }

    private void addProductToMenu(){
        String selectedText = cboAddProduct.getSelectedItem();
        Product selectedProduct = productMap.get(selectedText);
        controller.addProductToMenu(selectedProduct.getId());
        reloadData();
    }

    private double totalPriceMenu(){
        return controller.totalPriceMenu();
    }

    private Panel createButtonPanel(){
        return Panel.horizontalPanel(4,
                new Button("Fermer", this::close),
                new Button("Supprimer selectionne"),
                new Button("Changer ordre"),
                new Button("Modifier menu"),
                new Button("Archiver menu",this::archiveMenu),
                new Button("Supprimer menu",this::deleteMenu)
        );
    }

//    public void save(){
//        controller.save();
//        close();
//    }

//    public void deleteItem(){
//        controller.deleteItem();
//        close();
//    }

    public void deleteMenu(){
        controller.deleteMenu();
        close();
    }

    public void archiveMenu(){
        controller.archiveMenu();
        close();
    }

}
