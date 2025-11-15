package tgpr.restaurant.view;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.ColumnSpec;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.ObjectTable;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.restaurant.controller.TestController;
import tgpr.restaurant.model.User;

import java.util.List;

public class TestView extends DialogWindow {

    private final TestController controller;

    private ObjectTable<User> tblUsers;

    public TestView(TestController controller) {
        super("Test");

        this.controller = controller;

        setHints(List.of(Hint.EXPANDED));

        var root = createPanel();
        setComponent(root);

        refresh();
    }

    private Panel createPanel() {
        var panel = Panel.gridPanel(1);

        tblUsers = new ObjectTable<>(
                new ColumnSpec<>("Nom", User::getLastName),
                new ColumnSpec<>("Prénom", User::getFirstName),
                new ColumnSpec<>("Email", User::getEmail),
                new ColumnSpec<>("Rôle", User::getRole)
        ).centerCenter()
                .grow(true, true)
                .addTo(panel);

        tblUsers.setSelectAction(this::displayUser);
        return panel;
    }

    private void displayUser() {
        controller.displayUser(tblUsers.getSelected());
    }

    private void refresh() {
        var users = controller.getUsers();
        tblUsers.setItems(users);
    }
}
