package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.restaurant.controller.TablesCheckController;
import tgpr.restaurant.model.Security;
import tgpr.restaurant.model.Table;

import java.util.List;

public class TablesCheckView extends DialogWindow {
    ObjectTable<Table> tableTable;
    TablesCheckController controller;

    TextBox txtID = new TextBox(new TerminalSize(10,1)).setReadOnly(true);
    TextBox txtCapacity = new TextBox(new TerminalSize(10,1)).setReadOnly(true);
    TextBox txtLocation = new TextBox(new TerminalSize(30,1)).setReadOnly(true);

    Label lblErrorCapacity = new Label("").setForegroundColor(TextColor.ANSI.RED);
    Label lblErrorLocation = new Label("").setForegroundColor(TextColor.ANSI.RED);

    Button save = new Button("Sauvegarder", this::save).setEnabled(false);
    Button delete = new Button("Supprimer", this::delete).setEnabled(false);

    public TablesCheckView(TablesCheckController controller) {
        super("Voir les tables");
        this.controller = controller;

        setHints(List.of(Hint.MODAL,Hint.CENTERED));
        setCloseWindowWithEscape(true);

        Panel root = Panel.gridPanel(1,
                Margin.of(1,1,1,1),
                Spacing.of(1));
        setComponent(root);

        root.addComponent(createTable());
        if(Security.isBoss()){
            createFieldsPanel().addTo(root);
            createButtonFields().addTo(root);
        }
        createButton().addTo(root).centerCenter();
        tableTable.takeFocus();
    }

    private Panel createTable() {
        Panel panel = new Panel();

        if (Security.isClient()) {
            tableTable = new ObjectTable<>(
                    new ColumnSpec<>("ID",Table::getId),
                    new ColumnSpec<>("Capacité", t -> t.getCapacity() + "p"),
                    new ColumnSpec<>("Emplacement", Table::getLocation)
            );
        } else {
            tableTable = new ObjectTable<>(
                    new ColumnSpec<>("ID", Table::getId),
                    new ColumnSpec<>("Capacité", t -> t.getCapacity() + "p"),
                    new ColumnSpec<>("Emplacement", Table::getLocation),
                    new ColumnSpec<>("Utilisation",t -> {
                        var orders = controller.getNumberOfOrdersByTable(t.getId());
                        var reservations = controller.getNumberOfReservationsByTable(t.getId());

                        String result = "";

                        if (orders > 0) {
                            result += orders + " commande(s)";
                        }

                        if (orders > 0 && reservations > 0) {
                            result += ", ";
                        }

                        if (reservations > 0) {
                            result += reservations + " réservation(s)";
                        }

                        return result;
                    })
            );

            if(Security.isBoss()){
                tableTable.setSelectAction(() -> {
                    var currentTable = tableTable.getSelected();
                    controller.setTable(currentTable);
                    fillFieldsPanel(currentTable);
                    tableTable.setSelected(currentTable);
                });
            }
        }
        tableTable.sizeTo(85,8);
        tableTable.addTo(panel);
        reloadData();

        return panel;
    }

    public void reloadData() {
        tableTable.clear();
        var tables = controller.getTables();
        tableTable.add(tables);
        tableTable.invalidate();
    }

    private Panel createButton(){
        Panel panel = new Panel();
        panel.addComponent(new Button("Fermer",this::close));
        return panel;
    }

    private Panel createFieldsPanel(){
        Panel panel = Panel.verticalPanel(1);

        panel.addComponent(new Label("FORMULAIRE")
                .setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT));

        Panel fields = Panel.gridPanel(3,
                Margin.of(1,0,0,0));

        fields.addComponent(new Label("ID:"));
        txtID.addTo(fields);
        fields.addEmpty();

        fields.addComponent(new Label("Capacité:"));
        txtCapacity.addTo(fields)
                .setTextChangeListener((txt, byUser) -> checkCapacity());
        lblErrorCapacity.addTo(fields);

        fields.addComponent(new Label("Emplacement:"));
        txtLocation.addTo(fields)
                .setTextChangeListener((txt, byUser) -> checkLocation());
        lblErrorLocation.addTo(fields);

        fields.addTo(panel);
        return panel;
    }

    private Panel createButtonFields(){
        return Panel.horizontalPanel(
                save,
                delete,
                new Button("Nouveau")
        );
    }

    private void fillFieldsPanel(Table table){
        txtID.setText(String.valueOf(table.getId()));
        txtCapacity.setText(String.valueOf(table.getCapacity())).setReadOnly(false);
        txtLocation.setText(table.getLocation()).setReadOnly(false);

        delete.setEnabled(true);
    }

    private void save(){
        if(!hasErrors()) {
            controller.save(txtCapacity.getText(), txtLocation.getText());
            reloadData();
            clearFieldsPanel();
        }
    }

    private boolean hasErrors(){
        return !lblErrorLocation.getText().isEmpty() ||
                !lblErrorCapacity.getText().isEmpty() ||
                txtLocation.getText().isEmpty() ||
                txtCapacity.getText().isEmpty();
    }

    private void delete(){
        controller.delete();
        reloadData();
        clearFieldsPanel();
    }

    private void clearFieldsPanel(){
        txtID.setText("");
        txtCapacity.setText("").setReadOnly(true);
        txtLocation.setText("").setReadOnly(true);

        lblErrorCapacity.setText("");
        lblErrorLocation.setText("");

        disableButton();
    }

    private void disableButton(){
        save.setEnabled(false);
        delete.setEnabled(false);
    }

    private void checkCapacity(){
        var errors = controller.checkCapacity(txtCapacity.getText());
        lblErrorCapacity.setText(errors.getFirstErrorMessage(Table.Fields.Capacity));

        save.setEnabled(errors.isEmpty());
    }

    private void checkLocation(){
        var errors = controller.checkLocation(txtLocation.getText());
        lblErrorLocation.setText(errors.getFirstErrorMessage(Table.Fields.Location));
    }

}
