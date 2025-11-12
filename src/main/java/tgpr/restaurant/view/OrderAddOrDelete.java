package tgpr.restaurant.view;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

public abstract class OrderAddOrDelete<T> extends BasicWindow {

    protected final T controller;

    protected double sousTotal;
    protected double reduction;
    protected double total;

    protected OrderAddOrDelete(T controller, String title, String text, String buttonYes){
        this.controller = controller;
        setTitle(title);

        Panel root = new Panel(new BorderLayout());

        //  Contenu principal
        Panel content = new Panel(new LinearLayout(Direction.VERTICAL));
        content.addComponent(new Label(text));
        content.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        // Infos commande
        content.addComponent(new Label("Client:"));
        content.addComponent(new Label("Table:"));
        content.addComponent(new Label("Service:"));
        content.addComponent(new Label("Personnes effectives:"));
        content.addComponent(new EmptySpace(new TerminalSize(0, 1)));


        content.addComponent(new Label(buildRecap()));
        content.addComponent(new EmptySpace());

        root.addComponent(content, BorderLayout.Location.CENTER);

        // bouttons yes no
        Panel bottomPanel = new Panel(new BorderLayout());
        Panel rightButtons = new Panel(new LinearLayout(Direction.HORIZONTAL));

        rightButtons.addComponent(new Button(buttonYes, this::onYes));
        rightButtons.addComponent(new Button("No", this::close));

        bottomPanel.addComponent(rightButtons, BorderLayout.Location.RIGHT);
        root.addComponent(bottomPanel, BorderLayout.Location.BOTTOM);

        setComponent(root);
    }

    // Affichage pour le prix
    protected String buildRecap() {
        return String.format(
                "Sous-total: %.2f €  -  Réduction: %.2f €  =  Total: %.2f €",
                sousTotal, reduction, total
        );
    }

    // méthode pour mon boutton Yes
    protected abstract void onYes();
}