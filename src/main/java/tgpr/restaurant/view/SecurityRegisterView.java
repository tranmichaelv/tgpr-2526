package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import tgpr.restaurant.controller.SecurityRegisterController;
import tgpr.restaurant.model.User;

import java.util.List;

public class SecurityRegisterView extends BasicWindow {
    private final SecurityRegisterController controller;
    private Button btnRegistration;

    private final TextBox txtLastName = new TextBox(new TerminalSize(30,1));
    private final TextBox txtFirstName = new TextBox(new TerminalSize(30,1));
    private final TextBox txtEmail = new TextBox(new TerminalSize(30,1));
    private final TextBox txtPassword = new TextBox(new TerminalSize(30,1));
    private final TextBox txtConfirmPassword = new TextBox(new TerminalSize(30,1));
    private final TextBox txtNumber = new TextBox(new TerminalSize(30,1));

    private final Label lblErrorLastName = new Label("Nom requis");
    private final Label lblErrorFirstName = new Label("Prenom requis");
    private final Label lblErrorEmail = new Label("Email requis");
    private final Label lblErrorPassword = new Label("Mot de passe requis");
    private final Label lblErrorPasswordConfirm = new Label("Confirmation requise");
    private final Label lblErrorNumber = new Label("Téléphone requis");


    public SecurityRegisterView(SecurityRegisterController controller) {
        this.controller = controller;

        //Titre
        setTitle("Inscription Restaurant");
        setHints(List.of(Hint.CENTERED));

        Panel root = Panel.verticalPanel(1);
        setComponent(root);

        createFieldsPanel().addTo(root);
        createButtonPanel().addTo(root).center();

        txtLastName.takeFocus();
    }

    // panel du formulaire
    private Panel createFieldsPanel(){
        Panel panel = Panel.gridPanel(2,
                Margin.of(1, 1, 1, 0));

        new Label("Nom :").addTo(panel);
        txtLastName.addTo(panel)
                .setTextChangeListener((txt, byUser) -> check());
        panel.addEmpty();  // Espace vide entre les labels
        lblErrorLastName.setForegroundColor(TextColor.ANSI.RED).addTo(panel);

        new Label("Prénom :").addTo(panel);
        txtFirstName.addTo(panel)
                .setTextChangeListener((txt, byUser) -> check());
        panel.addEmpty();
        lblErrorFirstName.setForegroundColor(TextColor.ANSI.RED).addTo(panel);

        new Label("Email :").addTo(panel);
        txtEmail.addTo(panel)
                .setTextChangeListener((txt, byUser) -> check());
        panel.addEmpty();
        lblErrorEmail.setForegroundColor(TextColor.ANSI.RED).addTo(panel);

        new Label("Mot de passe :").addTo(panel);
        txtPassword.addTo(panel)
                .setMask('*')
                .setTextChangeListener((txt, byUser) -> check());
        panel.addEmpty();
        lblErrorPassword.setForegroundColor(TextColor.ANSI.RED).addTo(panel);

        new Label("Confirmer mot de passe :").addTo(panel);
        txtConfirmPassword.addTo(panel)
                .setMask('*')
                .setTextChangeListener((txt, byUser) -> check());
        panel.addEmpty();
        lblErrorPasswordConfirm.setForegroundColor(TextColor.ANSI.RED).addTo(panel);

        new Label("Téléphone :").addTo(panel);
        txtNumber.addTo(panel)
                .setTextChangeListener((txt, byUser) -> check());
        panel.addEmpty();
        lblErrorNumber.setForegroundColor(TextColor.ANSI.RED).addTo(panel);

        return panel;
    }

    private Panel createButtonPanel(){
        btnRegistration = new Button("S'inscrire", this::validate);
        Panel panel = Panel.horizontalPanel(
                btnRegistration,
                new Button("Retour",this::retour),
                new Button("Quitter",this::exit)
        );

        addShortcut(btnRegistration, KeyStroke.fromString("<A-r>"));

        return panel;
    }

    private void exit() {
        controller.exit();
    }

    private void retour(){
        controller.retour();
    }


    // cree un nouveau user
    private void validate(){
        controller.validate(
                txtLastName.getText(),
                txtFirstName.getText(),
                txtEmail.getText(),
                txtPassword.getText(),
                txtNumber.getText().replaceAll("/|\\s|-","")
        );
    }

    // pour les le code ci-dessous, il serait plus interressant au niveau de la vitesse
    // de faire une fonction pour chaque check pour que le code ne check pas plusieurs fois
    // ce qu'il a deja check
    private void check() {
        // check si les conditions metiers

        var errors = controller.check(
                txtLastName.getText(),
                txtFirstName.getText(),
                txtEmail.getText(),
                txtPassword.getText(),
                txtConfirmPassword.getText(),
                txtNumber.getText().replaceAll("/|\\s|-","")
        );

        // assigne le texte d'erreur à son label erreur respectif
        lblErrorLastName.setText(errors.getFirstErrorMessage(User.Fields.LastName));
        lblErrorFirstName.setText(errors.getFirstErrorMessage(User.Fields.FirstName));
        lblErrorEmail.setText(errors.getFirstErrorMessage(User.Fields.Email));
        lblErrorPassword.setText(errors.getFirstErrorMessage(User.Fields.Password));
        lblErrorPasswordConfirm.setText(errors.getFirstErrorMessage(SecurityRegisterController.Fields.PasswordConfirm));
        lblErrorNumber.setText(errors.getFirstErrorMessage(User.Fields.Phone));

        // permet de rendre le bouton accessible seulement s'il n'y a aucune erreur
        btnRegistration.setEnabled(errors.isEmpty());
    }


}
