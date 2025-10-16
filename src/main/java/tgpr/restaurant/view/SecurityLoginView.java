package tgpr.restaurant.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import tgpr.restaurant.controller.SecurityLoginController;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class SecurityLoginView extends BasicWindow{

    private final SecurityLoginController controller;
    private Button btnLogin;

    public SecurityLoginView(SecurityLoginController controller) {
        this.controller = controller;

        //Titre
        setTitle("Restaurant - page d'acceuil");
        setHints(List.of(Hint.CENTERED));


        //Premier panel qui permet de mettre les marges
        Panel margin = Panel.gridPanel(1,Margin.of(1,1,1,0));
        setComponent(margin);

        //Second panel qui va tout aligner
        Panel root = Panel.verticalPanel(1);

        //Rajoute tout les classes crees dans le second panel
        createLoginPanel().addTo(root);
        createReservationPanel().addTo(root);
        createShopPanel().addTo(root);
        createDebugPanel().addTo(root);
        createDatePanel().addTo(root);

        //Rajoute le panel dans la marge
        root.addTo(margin);

        btnLogin.takeFocus();
    }

    /*
        Differentes classes on ete crees pour separer les differentes sections comme
        indiques sur les screenshot pour l'instant juste la vue est faite
     */

    //Se connecter - S'inscrire
    private Panel createLoginPanel() {
        btnLogin = new Button("Se connecter",this::login);
        Button inscription = new Button("S'inscrire",this::register);
        Panel panel = Panel.verticalPanel(LinearLayout.Alignment.Center,
                btnLogin, inscription).center();

        addShortcut(btnLogin, KeyStroke.fromString("<A-l>"));
        addShortcut(inscription, KeyStroke.fromString("<A-i>"));

        return panel;
    }

    private void login() {
        controller.login();
    }

    // Reservation
    private Panel createReservationPanel(){
        Button reservation = new Button("Reserver une table");
        Panel panel = Panel.verticalPanel(LinearLayout.Alignment.Center,
                reservation).center();

        addShortcut(reservation, KeyStroke.fromString("<A-r>"));

        return panel;
    }

    // Panier - Menu
    private Panel createShopPanel() {
        Button carte = new Button("Parcourir la carte");
        Button panier = new Button("Mon panier");
        Panel panel = Panel.verticalPanel(LinearLayout.Alignment.Center,
                carte, panier).center();

        addShortcut(carte, KeyStroke.fromString("<A-c>"));
        addShortcut(panier, KeyStroke.fromString("<A-p>"));

        return panel;
    }

    // Reset - Exit
    private Panel createDebugPanel() {
        Button reset = new Button("Renitialiser la BD", this::seedData);
        Button exit = new Button("Quitter", this::exit);
        Panel panel = Panel.verticalPanel(LinearLayout.Alignment.Center,
                reset,exit).center();

        addShortcut(reset, KeyStroke.fromString("<A-r>"));
        addShortcut(exit, KeyStroke.fromString("<A-e>"));

        return panel;
    }

    // Set Date
    private Panel createDatePanel() {
        // Recupere la date et l'heure
        LocalDateTime localDate = LocalDateTime.now();
        // Formatte l'heure de facon donne
        DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDate = localDate.format(myFormatDate);

        Panel panel = Panel.gridPanel(2,
                Margin.of(1, 1, 1,0),
                Spacing.of(1),
                new Label("Date/Heure simulée : "),
                new TextBox(formattedDate)).center();

        return panel;
    }

    private void seedData() {
        controller.seedData();
    }

    private void exit() {
        controller.exit();
    }

    private void register(){
        controller.register();
    }

}


