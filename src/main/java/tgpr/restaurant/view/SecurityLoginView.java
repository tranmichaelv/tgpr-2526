package tgpr.restaurant.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import tgpr.restaurant.controller.SecurityLoginController;
import tgpr.restaurant.model.Time;


import java.util.List;


public class SecurityLoginView extends BasicWindow{

    private final SecurityLoginController controller;
    private Button btnLogin;

    private final TextBox date = new TextBox(Time.getDateChosenToString());

    public SecurityLoginView(SecurityLoginController controller) {
        this.controller = controller;

        // titre
        setTitle("Restaurant - page d'acceuil");
        setHints(List.of(Hint.CENTERED));


        // premier panel qui permet de mettre les marges
        Panel margin = Panel.gridPanel(1,Margin.of(1,1,1,0));
        setComponent(margin);

        // rajoute le panel dans le panel contenant la marge
        margin.addComponent(createFieldsPanel());

        btnLogin.takeFocus();
    }

    /*
        differentes classes ont été crees pour separer les differentes sections comme
        indiques sur les screenshot pour l'instant juste la vue est faite
     */

    // rajoute toutes les classes crees dans le panel
    private Panel createFieldsPanel(){
        Panel root = Panel.verticalPanel(1);

        createLoginPanel().addTo(root);
        createReservationPanel().addTo(root);
        createShopPanel().addTo(root);
        createDebugPanel().addTo(root);
        createDatePanel().addTo(root);

        return root;
    }

    // se connecter - S'inscrire
    private Panel createLoginPanel() {
        btnLogin = new Button("Se connecter",this::login);
        Button inscription = new Button("S'inscrire",this::register);
        Panel panel = Panel.verticalPanel(LinearLayout.Alignment.Center,
                btnLogin, inscription).center();

        addShortcut(btnLogin, KeyStroke.fromString("<A-l>"));
        addShortcut(inscription, KeyStroke.fromString("<A-i>"));

        return panel;
    }

    // reservation
    private Panel createReservationPanel(){
        Button reservation = new Button("Reserver une table", this::reservation);
        Panel panel = Panel.verticalPanel(LinearLayout.Alignment.Center,
                reservation).center();

        addShortcut(reservation, KeyStroke.fromString("<A-r>"));

        return panel;
    }

    // panier - Menu
    private Panel createShopPanel() {
        Button carte = new Button("Parcourir la carte", this::menu); // test a enlever
        Button panier = new Button("Mon panier", this::cart);
        Panel panel = Panel.verticalPanel(LinearLayout.Alignment.Center,
                carte, panier).center();

        addShortcut(carte, KeyStroke.fromString("<A-c>"));
        addShortcut(panier, KeyStroke.fromString("<A-p>"));

        return panel;
    }

    // reset - Exit
    private Panel createDebugPanel() {
        Button reset = new Button("Renitialiser la BD", this::seedData);
        Button exit = new Button("Quitter", this::exit);

        Panel panel = Panel.verticalPanel(LinearLayout.Alignment.Center,
                reset,
                exit
        ).center();

        addShortcut(reset, KeyStroke.fromString("<A-r>"));
        addShortcut(exit, KeyStroke.fromString("<A-e>"));

        return panel;
    }

    private Panel createDatePanel() {
        return Panel.gridPanel(2,
                Margin.of(1, 1, 1,0),
                Spacing.of(1),
                new Label("Date/Heure simulée : "),
                date.setTextChangeListener((txt, byUser) -> checkDate())
                        .center());
    }

    private void login() {
        controller.login();
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

    private void reservation() {controller.reservation();}

    private void cart(){
        controller.cart();
    }

    private void menu(){
        controller.menu();
    }

    private void checkDate(){
        controller.checkDate(date.getText());
    }

}


