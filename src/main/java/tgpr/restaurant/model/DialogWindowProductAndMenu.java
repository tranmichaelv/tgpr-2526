package tgpr.restaurant.model;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.framework.ErrorList;

import java.util.List;

/*
    Ce model est une classe abstraite qui permet de regrouper les deux Dialogwindow
    de menu et product add
 */

public abstract class DialogWindowProductAndMenu<E> extends DialogWindow {

    public final Product product;
    public final E controller;
    protected ComboBox<Product.Type> cboType;
    private Button btnSave;

    private TextBox txtname;
    private TextBox txtprice;
    private TextBox txtdescription;

    private final Label lblErrorName = new Label("");
    private final Label lblErrorPrice= new Label("");
    private final Label lblErrorDescription= new Label("");

    public abstract List<Product.Type> getAvailableTypes();

    public DialogWindowProductAndMenu(String title, E controller, Product product) {
        super(title);
        this.controller = controller;
        this.product = product;

        setHints(List.of(Hint.CENTERED, Hint.MODAL));
        setCloseWindowWithEscape(true);

        Panel margin = Panel.gridPanel(1, Margin.of(0,1,0,0));

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
                // si nouveau → aps de nom sinon get le nom
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

        cboType = new ComboBox<>(getAvailableTypes());
        cboType.setSelectedItem(product.getType() == null ? getAvailableTypes().getFirst() : product.getType());
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

    private Panel createButtonPanel() {
        btnSave = new Button("Sauvegarder", this::save);
        btnSave.setEnabled(false); // désactivé par défaut

        boolean isNew = product.getName() == null ;

        // distingue si l'objet et nouveau ou deja existant et ajoute-les buttons nécessaires
        if(!isNew){
            return Panel.horizontalPanel(4,
                    new Button("Annuler", this::close),
                    btnSave,
                    new Button("Archiver", this::archive),
                    new Button("Desarchiver", this::unarchive),
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
                        // si txtname et txtprice sont vides il y a bien une erreur
                        // cela permet au bouton save de ne pas s'activer au debut quand
                        // les validations n'ont pas encore été appelées
                        txtname.getText().isEmpty() ||
                        txtprice.getText().isEmpty();

        // si aucun message d’erreur → bouton actif
        btnSave.setEnabled(!hasErrors);
    }

    public void save(){
        product.setName(txtname.getText());
        product.setDescription(txtdescription.getText());
        product.setPrice(Double.parseDouble(txtprice.getText()));
        product.setType(cboType.getSelectedItem());
        product.save();
        close();
    }

    public void delete(){
        product.delete();
        close();
    }

    public void archive(){
        product.setArchived(true);
        product.save();
        close();
    }

    public void unarchive(){
        product.setArchived(false);
        product.save();
        close();
    }

    // vérifie-les règles métiers ############################################################
    private void checkName() {
        String getName = txtname.getText();
        var errors = new ErrorList();
        errors.add(ProductValidator.isValidName(getName));

        lblErrorName.setText(errors.getFirstErrorMessage(Product.Fields.Name));
        updateSaveButtonState();
    }

    private void checkPrice() {
        String getPrice = txtprice.getText();
        var errors = new ErrorList();
        errors.add(ProductValidator.isValidPrice(getPrice));

        lblErrorPrice.setText(errors.getFirstErrorMessage(Product.Fields.Price));
        updateSaveButtonState();
    }

    private void checkDescription() {
        String getDescription = txtdescription.getText();
        var errors = new ErrorList();
        errors.add(ProductValidator.isValidDescription(getDescription));

        lblErrorDescription.setText(errors.getFirstErrorMessage(Product.Fields.Description));
        updateSaveButtonState();
    }
    //############################################################################################


}
