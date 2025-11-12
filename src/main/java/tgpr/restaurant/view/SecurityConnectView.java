package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import tgpr.framework.Configuration;
import tgpr.restaurant.controller.SecurityConnectController;

import java.util.List;

public class SecurityConnectView extends BasicWindow {
    private Button btnLogin;
    private final SecurityConnectController controller;
    private final TextBox txtEmail = new TextBox(new TerminalSize(20,1));
    private final TextBox txtPassword = new TextBox(new TerminalSize(20,1));

    public SecurityConnectView(SecurityConnectController controller){
        this.controller = controller;

        // titre
        setTitle("Connexion Restaurant");
        setHints(List.of(Hint.CENTERED));


        Panel root = Panel.verticalPanel(1);
        setComponent(root);


        createFieldsPanel().addTo(root);
        createButtonPanel().addTo(root).center();

        Panel debug = Panel.verticalPanel(1);
        createDebugPanel().addTo(debug);
        debug.addTo(root).center();

        btnLogin.takeFocus();
    }

    // panel de connexion
    private Panel createFieldsPanel() {
        Panel panel = Panel.gridPanel(2,
                Margin.of(1, 1, 1, 0),
                Spacing.of(1));

        new Label("Email :").addTo(panel);
        txtEmail.addTo(panel).takeFocus();

        new Label("Mot de passe :").addTo(panel);
        txtPassword.setMask('*').addTo(panel);

        return panel;
    }

    // panel de buttons
    private Panel createButtonPanel(){
        btnLogin = new Button("Connexion",this::login);
        Panel panel = Panel.horizontalPanel(LinearLayout.Alignment.Center,
                btnLogin,
                new Button("Retour",this::back),
                new Button("Quitter",this::exit)
        );

        addShortcut(btnLogin, KeyStroke.fromString("<A-l>"));
        addShortcut(btnLogin, KeyStroke.fromString("<A-r>"));
        addShortcut(btnLogin, KeyStroke.fromString("<A-q>"));

        return panel;
    }

    // panel pour debug
    private Border createDebugPanel() {
        Panel panel = Panel.verticalPanel(LinearLayout.Alignment.Center,
                new Button("Connexion admin par defaut",this::logAsDefaultAdmin),
                new Button("Connexion employe par defaut",this::logAsDefaultEmploye),
                new Button("Connexion client par defaut",this::logAsDefaultClient)
        );

        return panel.withBorder(Borders.singleLine(" A des fins de debogage "));
    }

    private void login() {
        var errors = controller.login(txtEmail.getText(), txtPassword.getText());
        // s'il y a une erreur le curseur va se focus sur le label email
        if (!errors.isEmpty()) {
            txtEmail.takeFocus();
        }
    }

    private void exit() {
        controller.exit();
    }

    private void back(){
        controller.back();
    }

    private void logAsDefaultAdmin(){
        controller.login(Configuration.get("default.admin.mail"), Configuration.get("default.admin.password"));
    }

    private void logAsDefaultClient(){
        controller.login(Configuration.get("default.client.mail"), Configuration.get("default.client.password"));
    }

    private void logAsDefaultEmploye(){
        controller.login(Configuration.get("default.user.mail"), Configuration.get("default.user.password"));
    }
}
