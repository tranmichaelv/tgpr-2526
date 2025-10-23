package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderPayController;

public class OrderPayView extends BasicWindow {

    private final OrderPayController controller;

    public OrderPayView(OrderPayController controller) {
        this.controller = controller;

        setTitle("Enregistrer le paiement");

        Panel root = new Panel(new LinearLayout(Direction.VERTICAL));
        root.addComponent(new EmptySpace());
        root.addComponent(new EmptySpace());

        root.addComponent(sectionHeader("Informations de la commande:"));
        root.addComponent(new EmptySpace());
        Panel info = new Panel(new GridLayout(2));
        // ajout des informations de la commande
        info.addComponent(new Label("Client:"));
        info.addComponent(new Label(""));

        info.addComponent(new Label("Date:"));
        info.addComponent(new Label(""));

        info.addComponent(new Label("Table:"));
        info.addComponent(new Label(""));

        info.addComponent(new Label("Type:"));
        info.addComponent(new Label(""));

        root.addComponent(info);
        root.addComponent(new EmptySpace());

        root.addComponent(sectionHeader("Articles:"));

        // prix des articles
        Panel totals = new Panel(new GridLayout(2));
        Label sousTotalLbl = new Label("Sous-total:");
        sousTotalLbl.setForegroundColor(TextColor.ANSI.BLACK);
        Label sousTotalVal = new Label("");
        sousTotalVal.setForegroundColor(TextColor.ANSI.BLACK);
        totals.addComponent(sousTotalLbl);

        totals.addComponent(sousTotalVal);


        // remise de la commande
        Label remiseLbl = new Label("Remise");
        Label remiseVal = new Label("");
        remiseLbl.setForegroundColor(TextColor.ANSI.RED);
        totals.addComponent(remiseLbl);

        totals.addComponent(remiseVal);

        // paiement totale
        Label totalLbl = new Label("Total:");
        totalLbl.addStyle(SGR.BOLD);
        Label totalVal = new Label("");
        totalVal.addStyle(SGR.BOLD);
        totalLbl.setForegroundColor(TextColor.ANSI.GREEN);
        totals.addComponent(totalLbl);
        totals.addComponent(totalVal);


        root.addComponent(totals);
        root.addComponent(new EmptySpace());

        // le client choisi sa méthode de paiement
        root.addComponent(sectionHeader("Méthode de paiement:"));
        root.addComponent(new EmptySpace());
        Panel pay = new Panel(new GridLayout(2));
        pay.addComponent(new Label("Méthode:"));
        ComboBox<String> cbMethod = new ComboBox<>("Cash", "Card", "Other");
        cbMethod.setPreferredSize(new TerminalSize(16, 1));
        pay.addComponent(cbMethod);
        root.addComponent(pay);

        root.addComponent(new EmptySpace(new TerminalSize(1,1)));

        Panel buttons = new Panel(new GridLayout(2));
        Button confirm = new Button("Confirmer le paiement", () -> {});
        Button cancel = new Button("Annuler",this::close);
        buttons.addComponent(confirm);
        buttons.addComponent(cancel);

        root.addComponent(buttons);

        setComponent(root);
    }
        // méthode pour ne pas dupliquer mon code
    private Component sectionHeader(String title) {
        Label header = new Label(title);
        header.addStyle(SGR.BOLD);
        header.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        return header;
    }
}