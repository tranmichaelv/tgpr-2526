package tgpr.restaurant.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.restaurant.controller.MenuActionsController;
import tgpr.restaurant.controller.MenuCheckController;
import tgpr.restaurant.model.MenuItem;

import java.util.List;

public class MenuActionsView extends DialogWindow {
    private final MenuActionsController controller;

    public MenuActionsView(MenuActionsController controller, MenuItem menu) {
        super("Actions pour:" + menu.getProductName());
        this.controller = controller;

        setHints(List.of(Hint.CENTERED, Hint.MODAL));
        setCloseWindowWithEscape(true);

        Panel root = Panel.gridPanel(1,Margin.of(0,1,1,1),Spacing.of(1));
        setComponent(root);

        root.addComponent(new Label("Que voulez-vous faire avec ce produit ?"));
        root.addEmpty();

        root.addComponent(createButtonPanel());

        root.addEmpty();
    }

    private Panel createButtonPanel(){
        Panel panel = Panel.verticalPanel(LinearLayout.Alignment.Beginning,
                new Button("Supprimer selectionne",this::delete),
                new Button("Changer ordre",this::changeOrder),
                new Button("Annuler",this::close)
        );

        ((LinearLayout) panel.getLayoutManager()).setSpacing(1);
        return panel;
    }

    private void delete(){
        controller.delete();
        close();
    }

    private void changeOrder(){
        controller.changeOrder();
        close();
    }


}
