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
    private final Label lblErrorStartDate = new Label("");
    private final Label lblErrorEndDate = new Label("");
    private final Label lblErrorReason = new Label("");

    private boolean isFilling = false;

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
            // remplis-les different txtbox avec les donnees
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
                Margin.of(1,1,1,1));

        fields.addComponent(new Label("Date de debut (JJ/MM/AAAA) : "));
        txtStartDate = new TextBox(new TerminalSize(12,1));
        txtStartDate.addTo(fields)
                .setTextChangeListener((txt, byUser) -> checkStartDate());
        fields.addEmpty();
        lblErrorStartDate.setForegroundColor(TextColor.ANSI.RED).addTo(fields);

        fields.addComponent(new Label("Date de fin (JJ/MM/AAAA) : "));
        txtEndDate = new TextBox(new TerminalSize(12,1));
        txtEndDate.addTo(fields)
                .setTextChangeListener((txt, byUser) -> checkEndDate());
        fields.addEmpty();
        lblErrorEndDate.setForegroundColor(TextColor.ANSI.RED).addTo(fields);

        fields.addComponent(new Label("Raison de la fermeture : "));
        txtReason = new TextBox(new TerminalSize(40,1));
        txtReason.addTo(fields)
                .setTextChangeListener((txt, byUser) -> checkReason());
        fields.addEmpty();
        lblErrorReason.setForegroundColor(TextColor.ANSI.RED).addTo(fields);

        fields.addTo(panel);
        return panel;
    }

    // reset les txtbox
    private void clearFieldsPanel(){
        txtStartDate.setText("");
        txtEndDate.setText("");
        txtReason.setText("");
        lblTitle.setText("NOUVELLE PÉRIODE DE FERMETURE");
        lblTitle.addStyle(SGR.BORDERED)
                .setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);

        lblErrorStartDate.setText("");
        lblErrorEndDate.setText("");
        lblErrorReason.setText("");
    }

    // remplis avec les informations given
    private void fillFieldsPanel(ClosingPeriod closingPeriod){
        // isFilling permet d'attendre que tout sois set avant de faire quoique ce soit
        // sinon il va d'abord set txtStartDate mais dans le validator
        // il va mettre en comparaison le startDate du current avec le endDate du previous
        isFilling = true;

        txtStartDate.setText(closingPeriod.getStartDateFormatted());
        txtEndDate.setText(closingPeriod.getEndDateFormatted());
        txtReason.setText(closingPeriod.getReason());

        lblTitle.setText("MODIFICATION DE LA PÉRIODE DE FERMETURE");
        lblTitle.addStyle(SGR.BORDERED)
                .setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);

        // efface les messages d'erreurs quand on recharge une ligne
        lblErrorStartDate.setText("");
        lblErrorEndDate.setText("");
        lblErrorReason.setText("");

        isFilling = false;
    }

    private Panel createButtonPanel(){
        return Panel.horizontalPanel(
                new Button("Ajouter",this::add),
                new Button("Modifier",this::change),
                new Button("Annuler", this::cancel),
                new Button("Supprimer",this::delete),
                new Button("Fermer", this::close)
        );
    }

    private void cancel(){
        clearFieldsPanel();
        closingPeriod = null;
    }

    private void delete(){
        // si pas de closingPeriod -> impossible de supprimer
        if(closingPeriod == null) {
            controller.showAnError("supprimer");
            // cherche l'erreur
            check();
        } else {
            controller.delete(closingPeriod);
            // mets le closingPeriod null pour pas garder celui qui vient d'etre supprimer
            closingPeriod = null;

            reloadData();
            clearFieldsPanel();
        }
    }

    private void checkStartDate(){
        // si isFilling alors on fait rien
        if (isFilling) return;

        var errorsStart = controller.checkStartDate(
                txtStartDate.getText(),
                txtEndDate.getText(),
                closingPeriod);

        lblErrorStartDate.setText(errorsStart.getFirstErrorMessage(ClosingPeriod.Fields.StartDate));

        // Si End a déjà une erreur affichée, vérifier si elle doit disparaître

        if(!txtEndDate.getText().isEmpty()) {
            var errorsEnd = controller.checkEndDate(
                    txtStartDate.getText(),
                    txtEndDate.getText(),
                    closingPeriod
            );

            lblErrorEndDate.setText(errorsEnd.getFirstErrorMessage(ClosingPeriod.Fields.EndDate));

//             si plus d'erreur → effacer label
            if (errorsEnd.isEmpty()) {
                lblErrorEndDate.setText("");
            }
        }

    }

    private void checkEndDate(){
        // si isFilling alors on fait rien
        if (isFilling) return;

        var errorsEnd = controller.checkEndDate(
                txtStartDate.getText(),
                txtEndDate.getText(),
                closingPeriod);

        lblErrorEndDate.setText(errorsEnd.getFirstErrorMessage(ClosingPeriod.Fields.EndDate));

        if(!txtStartDate.getText().isEmpty()) {
            var errorsStart = controller.checkStartDate(
                    txtStartDate.getText(),
                    txtEndDate.getText(),
                    closingPeriod
            );

            lblErrorStartDate.setText(errorsStart.getFirstErrorMessage(ClosingPeriod.Fields.StartDate));

            if (errorsStart.isEmpty()) {
                lblErrorStartDate.setText("");
            }
        }

    }

    private void checkReason(){
        var errors = controller.checkReason(txtReason.getText());
        lblErrorReason.setText(errors.getFirstErrorMessage(ClosingPeriod.Fields.Reason));
    }

    private void check(){
        var errors = controller.check(
                txtStartDate.getText(),
                txtEndDate.getText(),
                txtReason.getText(),
                closingPeriod);

        lblErrorStartDate.setText(errors.getFirstErrorMessage(ClosingPeriod.Fields.StartDate));
        lblErrorEndDate.setText(errors.getFirstErrorMessage(ClosingPeriod.Fields.EndDate));
        lblErrorReason.setText(errors.getFirstErrorMessage(ClosingPeriod.Fields.Reason));
    }

    private void change(){
        // ne peut pas changer si pas de closingPeriod
        if(hasErrors() || closingPeriod == null) {
            controller.showAnError("changer");
            check();
        }else {
            controller.change(
                    txtStartDate.getText(),
                    txtEndDate.getText(),
                    txtReason.getText(),
                    closingPeriod
            );

            reloadData();
            clearFieldsPanel();
        }
        closingPeriod = null;
    }

    private boolean hasErrors(){
        return !lblErrorStartDate.getText().isEmpty() ||
                !lblErrorEndDate.getText().isEmpty() ||
                !lblErrorReason.getText().isEmpty() ||
                txtStartDate.getText().isEmpty() ||
                txtEndDate.getText().isEmpty() ||
                txtReason.getText().isEmpty();
    }

    private void add(){
        if(hasErrors() || closingPeriod != null) {
            controller.showAnError("ajouter");
            check();
        } else {
            controller.add(
                    txtStartDate.getText(),
                    txtEndDate.getText(),
                    txtReason.getText()
            );
            reloadData();
            clearFieldsPanel();
        }
        closingPeriod = null;
    }

}

