package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.restaurant.controller.ClosingChangeController;
import tgpr.restaurant.model.ClosingPeriod;

import java.util.List;

public class ClosingChangeview extends DialogWindow {

    private ClosingPeriod closingPeriod;
    private ObjectTable<ClosingPeriod> tableClosingPeriod;
    private final ClosingChangeController controller;

    private TextBox txtStartDate;
    private TextBox txtEndDate;
    private TextBox txtReason;

    private Label lblTitle;

    public ClosingChangeview(ClosingChangeController controller) {
        super("Gestion de la periode de fermeture");
        this.controller = controller;

        setHints(List.of(Hint.CENTERED, Hint.MODAL));
        setCloseWindowWithEscape(true);

        createBody();
    }

    public void createBody(){
        Panel root = Panel.gridPanel(1, Margin.of(1,1,0,0));
        setComponent(root);

        root.addComponent(creaTable());
        root.addComponent(createFieldsPanel());
        root.addComponent(createButtonPanel());

    }

    public void reloadData() {
        // vide le tableau
        tableClosingPeriod.clear();
        // demande au contrôleur la liste des closingPeriod
        var closingPeriod = controller.listClosingPeriod();
        // ajoute l'ensemble des closingPeriod au tableau
        tableClosingPeriod.add(closingPeriod);
        // requis à cause d'un bug quand on ferme la fenêtre popup avec la touche Escape
        tableClosingPeriod.invalidate();

    }

    private Component creaTable() {
        // crée une table avec differentes colonnes

        tableClosingPeriod = new ObjectTable<>(
                new ColumnSpec<>("Date debut", ClosingPeriod::getStartDateFormatted).setWidth(12),
                new ColumnSpec<>("Date fin", ClosingPeriod::getEndDateFormatted).setWidth(12),
                new ColumnSpec<>("Raison", ClosingPeriod::getReason).setWidth(40)
        );
        // donne une taille à la table
        tableClosingPeriod.sizeTo(80, 9);

        // spécifie l'action à exécuter quand on presse Enter ou la barre d'espace
        tableClosingPeriod.setSelectAction(() -> {

            // récupère l'objet couramment sélectionné en naviguant dessus
            closingPeriod = tableClosingPeriod.getSelected();
            fillFieldsPanel(closingPeriod);
            // stocke les changements dans la liste et le renvoi
            reloadData();
            //  repart sur la ligne de l'élément stocké
            tableClosingPeriod.setSelected(closingPeriod);
        });

        // charge les donnes dans la table
        reloadData();

        return tableClosingPeriod;
    }

    private Panel createFieldsPanel(){
        Panel panel = new Panel();

        lblTitle = new Label("NOUVELLE PERIODE DE FERMETURE ")
                .addStyle(SGR.BORDERED)
                .setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        panel.addComponent(lblTitle);

        Panel fields = Panel.gridPanel(2,
                Margin.of(1,1,1,1), Spacing.of(1));

        fields.addComponent(new Label("Date de debut (JJ/MM/AAAA) : "));
        txtStartDate = new TextBox(new TerminalSize(12,1));
        txtStartDate.addTo(fields);

        fields.addComponent(new Label("Date de fin (JJ/MM/AAAA) : "));
        txtEndDate = new TextBox(new TerminalSize(12,1));
        txtEndDate.addTo(fields);

        fields.addComponent(new Label("Raison de la fermeture : "));
        txtReason = new TextBox(new TerminalSize(40,1));
        txtReason.addTo(fields);

        fields.addTo(panel);
        return panel;
    }

    private void clearFieldsPanel(){
        txtStartDate.setText("");
        txtEndDate.setText("");
        txtReason.setText("");
        lblTitle.setText("NOUVELLE PÉRIODE DE FERMETURE");
        lblTitle.addStyle(SGR.BORDERED)
                .setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
    }

    private void fillFieldsPanel(ClosingPeriod closingPeriod){
        txtStartDate.setText(closingPeriod.getStartDateFormatted());
        txtEndDate.setText(closingPeriod.getEndDateFormatted());
        txtReason.setText(closingPeriod.getReason());
        lblTitle.setText("MODIFICATION DE LA PÉRIODE DE FERMETURE");
        lblTitle.addStyle(SGR.BORDERED)
                .setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
    }

    private Panel createButtonPanel(){
        return Panel.horizontalPanel(
                new Button("Ajouter",this::changeAndAdd),
                new Button("Modifier",this::changeAndAdd),
                new Button("Annuler", this::cancel),
                new Button("Supprimer",this::delete),
                new Button("Fermer", this::close)
        );
    }

    private void cancel(){
        clearFieldsPanel();
    }

    private void delete(){
        closingPeriod.delete();
        reloadData();
    }

    private void updateSaveAndChangeButton(){
        boolean hasErrors =
                txtStartDate.getText().isEmpty() ||
                        txtEndDate.getText().isEmpty() ||
                        txtReason.getText().isEmpty();

    }

    private void changeAndAdd(){
        controller.add(
                txtStartDate.getText(),
                txtEndDate.getText(),
                txtReason.getText()
        );
        reloadData();
    }
}

