package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.restaurant.controller.MenuChangeOrderController;
import tgpr.restaurant.model.MenuItem;

import java.util.List;

public class MenuChangeOrderView extends DialogWindow {
    private final MenuChangeOrderController controller;
    private final String productName;
    private final int orderInMenu;
    private TextBox txtNumberToChange;
    private final Label lblErrorNumberToChange = new Label("");
    private final Button btnAccept = new Button("OK",this::changeOrder);

    public MenuChangeOrderView(MenuChangeOrderController controller, String productName, int orderInMenu) {
        super("Changer l'ordre");
        this.controller = controller;
        this.productName = productName;
        this.orderInMenu = orderInMenu;

        setHints(List.of(Hint.CENTERED, Hint.MODAL));
        setCloseWindowWithEscape(true);

        Panel root = Panel.gridPanel(1, Margin.of(1,0,1,0));
        setComponent(root);

        root.addComponent(createBodyPanel());
        root.addComponent(createButtonPanel());


        txtNumberToChange.takeFocus();
    }

    private Panel createBodyPanel(){
        String texte = "Nouvel ordre pour '" + productName + "' :";
        int texteLength = texte.length();

        Label lblTexte = new Label(texte);
        txtNumberToChange = new TextBox(new TerminalSize(texteLength,1), String.valueOf(orderInMenu))
                .setTextChangeListener((txt, byUser) -> check());

        Panel panel = Panel.verticalPanel();

        lblTexte.addTo(panel);
        panel.addEmpty();
        txtNumberToChange.addTo(panel);
        lblErrorNumberToChange.setForegroundColor(TextColor.ANSI.RED).addTo(panel);
        return panel;
    }

    private Panel createButtonPanel(){
        return Panel.horizontalPanel(LinearLayout.Alignment.End,
                btnAccept,
                new Button("Cancel", this::cancel)
        );
    }

    private void check(){
        var error = controller.check(txtNumberToChange.getText());
        lblErrorNumberToChange.setText(error.getFirstErrorMessage(MenuItem.Fields.OrderInMenu));

        btnAccept.setEnabled(error.isEmpty());
    }

    private void cancel(){
        close();
    }

    private void changeOrder(){
        controller.changeOrder(txtNumberToChange.getText());
        close();
    }
}
