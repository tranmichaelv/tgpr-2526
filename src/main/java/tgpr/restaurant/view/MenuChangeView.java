package tgpr.restaurant.view;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.restaurant.controller.MenuChangeController;
import tgpr.restaurant.model.MenuItem;
import tgpr.restaurant.model.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuChangeView extends DialogWindow {
    private final Product menu;
    private final MenuChangeController controller;
    private ObjectTable<MenuItem> tableProduct;
    private ComboBox<String> cboAddProduct;
    private final Map<String, Product> productMap = new HashMap<>();

    private final Label lblTotalProduits = new Label("");
    private final Label lblPrixMenu = new Label("");
    private final Label lblDifference = new Label("");
    private final Label lblErrorDoubleProduct = new Label("");

    private final Button btnAdd = new Button("Ajouter",this::addProductToMenu);

    public MenuChangeView(MenuChangeController controller, Product menu) {
        super("Gerer les composant du menu: " + menu.getName());
        this.menu = menu;
        this.controller = controller;

        setHints(List.of(Hint.CENTERED, Hint.MODAL));
        setCloseWindowWithEscape(true);

        Panel root = Panel.gridPanel(1, Margin.of(0,1,0,1));
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

        // met à jour les prix
        updatePriceLabels();
        updateTitle();
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

        Panel panel = Panel.verticalPanel(LinearLayout.Alignment.Beginning,
                new Label("─".repeat(60)),
                lblTotalProduits,
                lblPrixMenu,
                lblDifference,
                new Label("─".repeat(60))
        );

        updatePriceLabels(); // initialise les valeurs
        return panel;
    }

    private void updateTitle(){
        setTitle("Gérer les composants du menu : " + menu.getName());
    }

    private void updatePriceLabels() {
        double totalProduct = totalPriceMenu();
        double menuPrice = menu.getPrice();
        double difference = totalProduct - menuPrice;

        lblTotalProduits.setText("Total de produits : " + String.format("%.2f €", totalProduct));
        lblPrixMenu.setText("Prix du menu : " + String.format("%.2f €", menuPrice));
        lblDifference.setText("Différence (avantage client) : " + String.format("%.2f €", difference));
    }

    private Panel createAddProductPanel(){
        cboAddProduct = new ComboBox<>();
        for (Product p : controller.listProduct()) {
            String display = p.getName() + " (" + p.getType() + ")";
            productMap.put(display, p);
            cboAddProduct.addItem(display);
        }
        checkDouble();

        cboAddProduct.addListener((selectedIndex, previousSelection, changedByUserInteraction) -> checkDouble());



        Panel panel = Panel.horizontalPanel(3);

        panel.addComponent(new Label("Ajouter produit:"));
        panel.addComponent(cboAddProduct);
        panel.addComponent(btnAdd);
        lblErrorDoubleProduct.setForegroundColor(TextColor.ANSI.RED).addTo(panel);

        return panel;
    }

    private void addProductToMenu(){
        String selectedText = cboAddProduct.getSelectedItem();
        Product selectedProduct = productMap.get(selectedText);
        controller.addProductToMenu(selectedProduct.getId());
        checkDouble();
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
                new Button("Modifier menu",this::changeMenu),
                new Button("Archiver menu",this::archiveMenu),
                new Button("Supprimer menu",this::deleteMenu)
        );
    }

    public void checkDouble(){
        String selectedDisplay = cboAddProduct.getSelectedItem();
        Product selectedProduct = productMap.get(selectedDisplay);

        var errors = controller.checkDouble(selectedProduct);

        lblErrorDoubleProduct.setText(errors.getFirstErrorMessage(MenuItem.Fields.Product));

        btnAdd.setEnabled(errors.isEmpty());
        cboAddProduct.takeFocus();
    }
    public void deleteMenu(){
        controller.deleteMenu();
        reloadData();
        close();
    }

    public void archiveMenu(){
        controller.archiveMenu();
        reloadData();
        close();
    }

    public void changeMenu(){
        controller.changeMenu();
        reloadData();
    }

    public void deleteItem(){
        controller.deleteItem();
        reloadData();
    }

    public void changeOrderItem(){
        controller.changeOrderItem();
        reloadData();
    }

}
