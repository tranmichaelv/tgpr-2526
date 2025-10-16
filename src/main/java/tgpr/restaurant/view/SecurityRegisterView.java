package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import tgpr.framework.Error;
import tgpr.restaurant.controller.SecurityRegisterController;

import java.util.List;

public class SecurityRegisterView extends BasicWindow {
    private final SecurityRegisterController controller;

    private final TextBox txtLastName = new TextBox(new TerminalSize(30,1));
    private final TextBox txtSurname = new TextBox(new TerminalSize(30,1));
    private final TextBox txtEmail = new TextBox(new TerminalSize(30,1));
    private final TextBox txtPassword = new TextBox(new TerminalSize(30,1));
    private final TextBox txtConfirmPassword = new TextBox(new TerminalSize(30,1));
    private final TextBox txtNumber = new TextBox(new TerminalSize(30,1));
    private Button btnRegistration;

    private final Label lblErrorLastName = new Label("Nom requis");
    private final Label lblErrorSurname = new Label("Prenom requis");
    private final Label lblErrorEmail = new Label("Email requis");
    private final Label lblErrorPassword = new Label("Mot de passe requis");
    private final Label lblErrorConfirmPassword = new Label("Confirmation requise");
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
    }

    private Panel createFieldsPanel(){
        Panel panel = Panel.gridPanel(2,
                Margin.of(1, 1, 1, 0)).center();

        new Label("Nom:").addTo(panel);
        txtLastName.addTo(panel);
        new EmptySpace().addTo(panel);  // Espace vide dans la 1re colonne
        lblErrorLastName.setForegroundColor(TextColor.ANSI.RED).addTo(panel);

        new Label("Prénom:").addTo(panel);
        txtSurname.addTo(panel);
        new EmptySpace().addTo(panel);
        lblErrorSurname.setForegroundColor(TextColor.ANSI.RED).addTo(panel);

        new Label("Email:").addTo(panel);
        txtEmail.addTo(panel);
        new EmptySpace().addTo(panel);
        lblErrorEmail.setForegroundColor(TextColor.ANSI.RED).addTo(panel);

        new Label("Mot de passe:").addTo(panel);
        txtPassword.setMask('*').addTo(panel);
        new EmptySpace().addTo(panel);
        lblErrorPassword.setForegroundColor(TextColor.ANSI.RED).addTo(panel);

        new Label("Confirmer mot de passe:").addTo(panel);
        txtConfirmPassword.setMask('*').addTo(panel);
        new EmptySpace().addTo(panel);
        lblErrorConfirmPassword.setForegroundColor(TextColor.ANSI.RED).addTo(panel);

        new Label("Téléphone:").addTo(panel);
        txtNumber.addTo(panel);
        new EmptySpace().addTo(panel);
        lblErrorNumber.setForegroundColor(TextColor.ANSI.RED).addTo(panel);

        return panel;
    }

    private Panel createButtonPanel(){
        btnRegistration = new Button("Connexion",this::register);
        Panel panel = Panel.gridPanel(3, Margin.of(0,1,0,0),
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

    private void register() {
        // On vide d’abord tous les anciens messages
        clearErrors();

        var errors = controller.Register(
                txtLastName.getText(),
                txtSurname.getText(),
                txtEmail.getText(),
                txtPassword.getText(),
                txtConfirmPassword.getText(),
                txtNumber.getText()
        );


        // On affiche les nouveaux
        if (!errors.isEmpty()) {
            showErrors(errors);
            txtLastName.takeFocus();
        }
    }

    private void clearErrors() {
        lblErrorLastName.setText("");
        lblErrorSurname.setText("");
        lblErrorEmail.setText("");
        lblErrorPassword.setText("");
        lblErrorConfirmPassword.setText("");
        lblErrorNumber.setText("");
    }

    private void showErrors(List<Error> errors) {

        for (Error error : errors) {
            String message = error.getMessage(); // ou .message() selon ta classe Error

            if (message.contains("Nom minimun 2 lettres"))
                lblErrorLastName.setText(message);

            else if (message.contains("Prenom minimun 2 lettres"))
                lblErrorSurname.setText(message);

            else if (message.contains("email"))
                lblErrorEmail.setText(message);

            else if (message.contains("Mot de passe incorrect"))
                lblErrorPassword.setText(message);

            else if (message.contains("Mot de passe pas pareil"))
                lblErrorConfirmPassword.setText(message);

            else if (message.contains("téléphone"))
                lblErrorNumber.setText(message);
        }
    }

}
