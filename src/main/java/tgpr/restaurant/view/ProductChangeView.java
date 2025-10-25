package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.restaurant.controller.ProductChangeController;
import tgpr.restaurant.model.Product;

import java.util.List;

public class ProductChangeView extends DialogWindow {

    private final Product product;
    private final ProductChangeController controller;
    private static final List<Product.Type> SortableFields = List.of(Product.Type.Starter, Product.Type.Main, Product.Type.Dessert, Product.Type.Drink);
    private final ComboBox<Product.Type> cboType = new ComboBox<>(SortableFields);
    private Button btnSave;

    private TextBox txtname;
    private TextBox txtprice;
    private TextBox txtdescription;

    private final Label lblErrorName = new Label("");
    private final Label lblErrorPrice= new Label("");
    private final Label lblErrorDescription= new Label("");

    public ProductChangeView(ProductChangeController controller, Product product) {
        super("Modifier le produit");
        this.controller = controller;
        this.product = product;

        setHints(List.of(Hint.CENTERED, Hint.MODAL));
        setCloseWindowWithEscape(true);

        Panel margin = Panel.gridPanel(1,Margin.of(0,1,0,0));

        setComponent(margin);
        createBody().addTo(margin);

        txtname.setCaretPosition(0).takeFocus();
    }

    private Panel createBody(){
        Panel panel = Panel.verticalPanel(1);
        createNamePanel().addTo(panel);
        createTypeTable().addTo(panel);
        createPricePanel().addTo(panel);
        createDescriptionPanel().addTo(panel);
        createButtonPanel().addTo(panel).center();
        return panel;
    }

    private Panel createNamePanel() {
        txtname = new TextBox(new TerminalSize(30,1),
                product.getName() == null ? "" : product.getName());

        Panel panel = Panel.gridPanel(2,
                new Label("Nom:"),
                txtname.setTextChangeListener((txt, byUser) -> checkName())
        );

        panel.addEmpty();
        lblErrorName.setForegroundColor(TextColor.ANSI.RED).addTo(panel);


        return panel;
    }

    private Panel createTypeTable(){
        Panel panel = Panel.gridPanel(2);
        new Label("Type:").addTo(panel);

        // Correction : vérifier si le type est null
        Product.Type selectedType = (product.getType() == null)
                ? SortableFields.getFirst()
                : product.getType();

        cboType.setSelectedItem(selectedType);
        cboType.addTo(panel);

        return panel;
    }

    private Panel createPricePanel() {
        txtprice = new TextBox(new TerminalSize(15,1),
                product.getPrice() == 0 ? "" : String.valueOf(product.getPrice()));

        Panel panel = Panel.gridPanel(2,
                new Label("Prix:"),
                txtprice.setTextChangeListener((txt, byUser) -> checkPrice())
        );

        panel.addEmpty();
        lblErrorPrice.setForegroundColor(TextColor.ANSI.RED).addTo(panel);

        return panel;
    }

    private Panel createDescriptionPanel() {
        txtdescription = new TextBox(new TerminalSize(40,3),
                product.getDescription() == null ? "" : product.getDescription());
        Panel panel = Panel.gridPanel(2,
                new Label("Description:"),
                txtdescription.setCaretPosition(0)
                        .setTextChangeListener((txt, byUser) -> checkDescription())
        );

        panel.addEmpty();
        lblErrorDescription.setForegroundColor(TextColor.ANSI.RED).addTo(panel);

        return panel;
    }

    private Panel createButtonPanel(){
        btnSave = new Button("Sauvegarder", this::save);
        btnSave.setEnabled(false); // désactivé par défaut

        if(!controller.isNew()){
            return Panel.horizontalPanel(4,
                    new Button("Annuler", this::close),
                    btnSave,
                    new Button("Archiver", this::archive),
                    new Button("Supprimer", this::delete)
            );
        } else {
            return Panel.horizontalPanel(4,
                    new Button("Annuler", this::close),
                    btnSave
            );
        }
    }

    private void updateSaveButtonState() {
        boolean hasErrors =
                !lblErrorName.getText().isEmpty() ||
                        !lblErrorPrice.getText().isEmpty() ||
                        !lblErrorDescription.getText().isEmpty() ||
                        txtname.getText().isEmpty() ||
                        txtprice.getText().isEmpty();

        // Si aucun message d’erreur → bouton actif
        btnSave.setEnabled(!hasErrors);
    }

    // verifie les regles metiers
    private void checkName() {
        var errors = controller.checkName(
                txtname.getText()
        );

        lblErrorName.setText(errors.getFirstErrorMessage(Product.Fields.Name));
        updateSaveButtonState();
    }

    private void checkPrice() {
        var errors = controller.checkPrice(
                txtprice.getText()
        );

        lblErrorPrice.setText(errors.getFirstErrorMessage(Product.Fields.Price));
        updateSaveButtonState();
    }

    private void checkDescription() {
        var errors = controller.checkDescription(
                txtdescription.getText()
        );

        lblErrorDescription.setText(errors.getFirstErrorMessage(Product.Fields.Description));
        updateSaveButtonState();
    }

    public void save(){
        controller.save(
                txtname.getText(),
                txtdescription.getText(),
                txtprice.getText(),
                cboType.getSelectedItem()
        );
        close();
    }

    public void delete(){
        controller.delete();
        close();
    }

    public void archive(){
        controller.archive();
        close();
    }
}
