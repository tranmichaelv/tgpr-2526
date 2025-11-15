package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.restaurant.controller.ClientRegisterController;
import tgpr.restaurant.controller.SecurityRegisterController;
import tgpr.restaurant.model.User;

import java.util.List;

public class ClientRegisterView extends DialogWindow {
    private ClientRegisterController controller;

    private Button btnRegister;

    private final TextBox txtLastName = new TextBox(new TerminalSize(35,1));
    private final TextBox txtFirstName = new TextBox(new TerminalSize(35,1));
    private final TextBox txtEmail = new TextBox(new TerminalSize(35,1));
    private final TextBox txtPhone = new TextBox(new TerminalSize(35,1));

    private final Label lblErrorLastName = new Label("Nom requis").setForegroundColor(TextColor.ANSI.RED);
    private final Label lblErrorFirstName = new Label("Prenom requis").setForegroundColor(TextColor.ANSI.RED);
    private final Label lblErrorEmail = new Label("Email requis").setForegroundColor(TextColor.ANSI.RED);
    private final Label lblErrorPhone = new Label("Téléphone requis").setForegroundColor(TextColor.ANSI.RED);

    public ClientRegisterView(ClientRegisterController controller) {
        super("Enregister un client");
        this.controller = controller;

        setHints(List.of(Hint.MODAL, Hint.CENTERED));
        setCloseWindowWithEscape(true);

        Panel root = Panel.gridPanel(1,
                Margin.of(3,1,1,0),
                Spacing.of(1));
        setComponent(root);

        root.addComponent(createFieldsPanel());
        createButtonPanel().addTo(root).centerCenter();
    }

    private Panel createFieldsPanel(){
        Panel panel = Panel.gridPanel(2);

        panel.addComponent(new Label("Nom :"));
        txtLastName.addTo(panel)
                .setTextChangeListener((txt, byUser) -> check());
        panel.addEmpty();
        lblErrorLastName.addTo(panel);

        panel.addComponent(new Label("Prenom :"));
        txtFirstName.addTo(panel)
                .setTextChangeListener((txt, byUser) -> check());
        panel.addEmpty();
        lblErrorFirstName.addTo(panel);

        panel.addComponent(new Label("Email :"));
        txtEmail.addTo(panel)
                .setTextChangeListener((txt, byUser) -> check());
        panel.addEmpty();
        lblErrorEmail.addTo(panel);

        panel.addComponent(new Label("Telephone :"));
        txtPhone.addTo(panel)
                .setTextChangeListener((txt, byUser) -> check());
        panel.addEmpty();
        lblErrorPhone.addTo(panel);

        return panel;
    }

    private Panel createButtonPanel(){
        btnRegister = new Button("Enregistrer",this::validate);
        btnRegister.setEnabled(false);

        Panel panel = Panel.horizontalPanel(
                btnRegister,
                new Button("Retour", this::close)
        );

        return panel;
    }

    private void check() {
        // check si les conditions metiers

        var errors = controller.check(
                txtLastName.getText(),
                txtFirstName.getText(),
                txtEmail.getText(),
                txtPhone.getText().replaceAll("/|\\s|-","")
        );

        // assigne le texte d'erreur à son label erreur respectif
        lblErrorLastName.setText(errors.getFirstErrorMessage(User.Fields.LastName));
        lblErrorFirstName.setText(errors.getFirstErrorMessage(User.Fields.FirstName));
        lblErrorEmail.setText(errors.getFirstErrorMessage(User.Fields.Email));
        lblErrorPhone.setText(errors.getFirstErrorMessage(User.Fields.Phone));

        // permet de rendre le bouton accessible seulement s'il n'y a aucune erreur
        btnRegister.setEnabled(errors.isEmpty());
    }

    private void validate(){
        controller.validate(
                txtLastName.getText(),
                txtFirstName.getText(),
                txtEmail.getText(),
                txtPhone.getText().replaceAll("/|\\s|-","")
        );
    }

}
