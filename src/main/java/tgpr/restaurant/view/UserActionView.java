package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.restaurant.controller.UserActionController;
import tgpr.restaurant.controller.UserChangeController;
import tgpr.restaurant.model.User;

import java.util.List;

public class UserActionView extends DialogWindow {
    UserActionController controller;
    private Label titre = new Label("");
    private ActionListBox listBox = new ActionListBox();
    public UserActionView(UserActionController controller, User user) {
        super("Action sur l'utilisateur");
        this.controller = controller;

        setHints(List.of(Hint.CENTERED, Hint.MODAL));
        setCloseWindowWithEscape(true);

        Panel root = Panel.gridPanel(1,
                Margin.of(1,0,1,0),
                Spacing.of(1));
        setComponent(root);


        root.addComponent(titre = new Label("Que voulez-vous faire avec " + user.getLastName() + ", " + user.getFirstName() + " ?"));
        root.addComponent(createButton());
        root.addComponent(new Button("Cancel",this::close).bottomRight());

        listBox.takeFocus();
    }

    private ActionListBox createButton(){
        listBox = new ActionListBox(new TerminalSize(titre.getText().length(), 3));
        listBox.addItem("Bloquer l'utilisateur", this::block);
        listBox.addItem("Voir les details", this::details);
        listBox.addItem("Annuler", this::close);

        return listBox;
    }


    private void block(){
        controller.block();
        close();
    }

    private void details(){
        controller.details();
    }

}
