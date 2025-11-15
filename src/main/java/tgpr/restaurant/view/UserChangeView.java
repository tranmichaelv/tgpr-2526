package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.restaurant.controller.UserChangeController;
import tgpr.restaurant.model.User;

import java.util.List;

public class UserChangeView extends DialogWindow {
    private final UserChangeController controller;
    private final User userToFocus;

    private ObjectTable<User> table;

    public UserChangeView(UserChangeController controller, User userToFocus) {
        super("Gestion des utilisateurs");
        this.controller = controller;
        this.userToFocus = userToFocus;

        setHints(List.of(Hint.CENTERED, Hint.MODAL));
        setCloseWindowWithEscape(true);

        Panel root = Panel.gridPanel(1,
                Margin.of(1,0,5,0),
                Spacing.of(1));
        setComponent(root);

        root.addComponent(createTitre());
        root.addComponent(createTable());
        root.addComponent(createButton());

        table.takeFocus();
    }

    private Panel createTitre(){
        Panel panel = new Panel();
        panel.addComponent(new Label("GESTION DES UTILISATEURS")
                .addStyle(SGR.UNDERLINE).addStyle(SGR.BORDERED).setForegroundColor(TextColor.ANSI.BLUE_BRIGHT));

        return panel;
    }

    public Component createTable() {
        table = new ObjectTable<>(
                new ColumnSpec<>("Nom", User::getLastName).setWidth(20),
                new ColumnSpec<>("Prenom", User::getFirstName).setWidth(20),
                new ColumnSpec<>("Email", User::getEmail).setWidth(30),
                new ColumnSpec<>("Role", User::getRole).setWidth(10),
                new ColumnSpec<>("Statut", (User user) -> user.getBlocked() ? "Désactivé" : "Actif").setWidth(10),
                new ColumnSpec<>("Téléphone", User::getPhone).setWidth(15)
        );
        table.sizeTo(115, 15);

        // spécifie l'action à exécuter quand on presse Enter ou la barre d'espace
        table.setSelectAction(() -> {
            var user = table.getSelected();
            controller.editUser(user);
            reloadData();
            table.setSelected(user);
        });

        // charge les données dans la table
        reloadData();

        return table;
    }

    private void reloadData(){
        // vide le tableau
        table.clear();
        // demande au contrôleur la liste des membres
        var users = controller.getAllUsers();
        // ajoute l'ensemble des membres au tableau
        table.add(users);
        // requis à cause d'un bug quand on ferme la fenêtre popup avec la touche Escape
        table.invalidate();

        if (userToFocus != null) {
//            table.takeFocus();
            table.setSelected(userToFocus);
        }
    }

    private Panel createButton(){
        Panel panel = new Panel();
        panel.addComponent(new Button("Fermer",this::close));
        return panel;
    }
}
