package tgpr.restaurant.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.restaurant.model.User;

import java.util.List;

public class UserDetailsView extends DialogWindow {
    private final User user;

    public UserDetailsView(User user) {
        super("Voir les details de " + user.getLastName() + " " + user.getFirstName());
        this.user = user;

        setHints(List.of(Hint.MODAL, Hint.CENTERED));
        setCloseWindowWithEscape(true);

        createView();
    }

    private void createView(){
        Panel root = Panel.gridPanel(1,
                Margin.of(1,1,1,0),
                Spacing.of(1));
        setComponent(root);

        root.addComponent(createBody());
        createButton().addTo(root)
                .centerRight().takeFocus();
    }

    private Panel createBody(){
        return Panel.gridPanel(2,
                new Label("Id: "), new Label(String.valueOf(user.getId())),
                new Label("Nom: "), new Label(user.getLastName()),
                new Label("Nom: "), new Label(user.getFirstName()),
                new Label("Email: "), new Label(user.getEmail()),
                new Label("Mot de passe: "), new Label("*".repeat(user.getPassword().length())),
                new Label("Téléphone: "), new Label(String.valueOf(user.getPhone())),
                new Label("Role: "), new Label(String.valueOf(user.getRole())),
                new Label("Bloqué: "), new Label(user.getBlocked() ? "oui" : "non")
        );
    }

    private Button createButton(){
        return new Button("Fermer", this::close);
    }


}
