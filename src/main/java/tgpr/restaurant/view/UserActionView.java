package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.restaurant.controller.UserActionController;

import java.util.List;

public class UserActionView extends DialogWindow {
    private final UserActionController controller;

    private Label titre;
    private ActionListBox listBox = new ActionListBox();

    public UserActionView(UserActionController controller) {
        super("Action sur l'utilisateur");
        this.controller = controller;

        setHints(List.of(Hint.CENTERED, Hint.MODAL));
        setCloseWindowWithEscape(true);

        createView();
    }

    private void createView(){
        Panel root = Panel.gridPanel(1,
                Margin.of(1,0,1,0),
                Spacing.of(1));
        setComponent(root);


        root.addComponent(titre = new Label("Que voulez-vous faire avec "
                + controller.getUser().getLastName() + ", " + controller.getUser().getFirstName() + " ?"));
        root.addComponent(createActionListBox());
        root.addComponent(createButton());

        listBox.takeFocus();
    }

    private ActionListBox createActionListBox(){
        listBox = new ActionListBox(new TerminalSize(titre.getText().length(), 4));
        listBox.addItem("Bloquer l'utilisateur", this::block);
        listBox.addItem("Débloquer l'utilisateur", this::unblock);
        listBox.addItem("Voir les détails", this::details);
        listBox.addItem("Annuler", this::close);

        return listBox;
    }

    private void block(){
        controller.block();
        close();
    }

    private void unblock(){
        controller.unblock();
        close();
    }

    private void details(){
        controller.details();
    }

    private Button createButton(){
        return new Button("Cancel",this::close).bottomRight();
    }

}
