package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderChangeController;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.model.Service;
import tgpr.restaurant.model.Table;
import tgpr.restaurant.model.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class OrderChangeView extends BasicWindow {

    private final OrderChangeController controller;

    private ComboBox<String> clientBox;
    private final List<User> clients;
    private ComboBox<String> tableBox;
    private final List<Table> tables;
    private ComboBox<String> serviceBox;
    private List<Service> services;
    private CheckBox takeAwayBox;
    private TextBox nbPersonsBox;
    private TextBox reductionBox;

    Order order;

    public OrderChangeView(OrderChangeController controller) {
        this.controller = controller;


        setTitle("Modifier une commande");

        Panel root = new Panel();

        root.addComponent(new EmptySpace());
        root.addComponent(new EmptySpace());

        Panel infoSection = new Panel(new LinearLayout(Direction.VERTICAL));
        root.addComponent(sectionHeader("Informations de la commande:"));
        infoSection.addComponent(new EmptySpace(new TerminalSize(0,1)));

        Panel clientLine = new Panel(new LinearLayout(Direction.HORIZONTAL));
        clientLine.addComponent(new Label("Client : "  ));
        clientBox = new ComboBox<>();
        clientBox.setPreferredSize(new TerminalSize(30,1));

        clients = controller.getClients();
        for(User u : clients){
            clientBox.addItem(u.getLastName() +", " + u.getFirstName());
        }
        // sélectionne le client de la commande choisie comme affichage de base
        int currentClientId = controller.getOrder().getClientId();
        int idx = IntStream.range(0, clients.size())
                .filter(i -> clients.get(i).getId() == currentClientId)
                .findFirst().orElse(-1);
        if (idx >= 0) clientBox.setSelectedIndex(idx);

        clientLine.addComponent(clientBox);
        infoSection.addComponent(clientLine);

        Panel tableLine = new Panel(new LinearLayout(Direction.HORIZONTAL));
        tableLine.addComponent(new Label("Table:"));

        tableBox = new ComboBox<>();
        tableBox.setPreferredSize(new TerminalSize(28,1));

        tables = controller.getAvailableTables();
        tableBox.addItem("Aucune");
        for (Table t : tables){
            tableBox.addItem("Table" +" " + t.getId() + " (" + t.getCapacity() + "p)");
        }
        // sélectionne la table de la commande choisie comme affichage de base
        Integer currentTableId = controller.getOrder().getTableId();
        if (currentTableId == null) {
            tableBox.setSelectedIndex(0);            // a emporter
        } else {
            int idx2 = 0; // a emporter
            for (int i = 0; i < tables.size(); i++) {
                if (tables.get(i).getId() == currentTableId) { idx2 = i + 1; break; }
            }
            tableBox.setSelectedIndex(idx2);
        }
        tableLine.addComponent(tableBox);
        infoSection.addComponent(tableLine);

        Panel serviceLine = new Panel(new LinearLayout(Direction.HORIZONTAL));
        serviceLine.addComponent(new Label("Service:"));
        serviceBox = new ComboBox<>();
        serviceBox.setPreferredSize(new TerminalSize(28, 1));

        services = controller.getAvailableServices();
        for(Service s : services){
            serviceBox.addItem(s.getName());
        }
        // sélectionne le service de la commande choisie comme affichage de base
        int currentServiceId = controller.getOrder().getServiceId();
        for (int i = 0; i < services.size(); i++) {
            if (services.get(i).getId() == currentServiceId) {
                serviceBox.setSelectedIndex(i);
                break;
            }
        }

        serviceLine.addComponent(serviceBox);
        infoSection.addComponent(serviceLine);

        Panel takeAwayLine = new Panel(new LinearLayout(Direction.HORIZONTAL));
        takeAwayLine.addComponent(new Label("A emporter : "));
        takeAwayBox = new CheckBox();
        takeAwayBox.setChecked(controller.getOrder().getTakeAway());
        takeAwayLine.addComponent(takeAwayBox);

        infoSection.addComponent(takeAwayLine);


        Panel peopleLine = new Panel(new LinearLayout(Direction.HORIZONTAL));
        peopleLine.addComponent(new Label("Nombre de personnes :"));
        nbPersonsBox = new TextBox(String.valueOf(controller.getOrder().getEffectivePeopleCount()));
        peopleLine.addComponent(nbPersonsBox);
        infoSection.addComponent(peopleLine);

        // si c'est une commande à emporter alors
        // on affiche Aucune pour Table et 1 pour nb personne
        takeAwayBox.addListener(checked -> {
            boolean takeAway = checked;
            controller.getOrder().setTakeAway(takeAway);

            if (takeAway) {
                tableBox.setEnabled(false);
                tableBox.setSelectedIndex(0); // aucune
                nbPersonsBox.setText("1");
                nbPersonsBox.setEnabled(false);
            } else {
                tableBox.setEnabled(true);
                nbPersonsBox.setEnabled(true);
            }
        });

        // applique l’état initial selon la commande
        if (controller.getOrder().getTakeAway()) {
            tableBox.setEnabled(false);
            nbPersonsBox.setText("1");
            nbPersonsBox.setEnabled(false);
        }


        Panel reductionLine = new Panel(new LinearLayout(Direction.HORIZONTAL));
        reductionLine.addComponent(new Label("Réduction (€): "));

        reductionBox = new TextBox();
        reductionBox.setPreferredSize(new TerminalSize(10,1));
        Double d = controller.getOrder().getDiscount();
        reductionBox.setText(d == null ? "" : String.format("%.2f", d));

        reductionLine.addComponent(reductionBox);
        infoSection.addComponent(reductionLine);

        infoSection.addComponent(new EmptySpace(new TerminalSize(0,1)));

        Label serviceLabel = new Label("Services disponibles:");
        serviceLabel.addStyle(SGR.ITALIC);
        List<Service> servicesDisponibles = controller.getAvailableServices();
        String nomsServices = servicesDisponibles.stream()
                .map(Service::getName)
                .collect(Collectors.joining(", "));

        serviceLabel.setText("Services disponibles : " + nomsServices);
        infoSection.addComponent(serviceLabel);
        infoSection.addComponent(new EmptySpace(new TerminalSize(0,1)));

        Panel productsSection = new Panel(new LinearLayout(Direction.VERTICAL));
        // Ajouter ProductCheck de Michael
        productsSection.addComponent(sectionHeader("Produits disponibles:"));
        productsSection.addComponent(new EmptySpace(new TerminalSize(0,1)));

        Panel ligneProducts = new Panel(new LinearLayout(Direction.HORIZONTAL));
        ligneProducts.addComponent(emphasize("Nom              "));
        ligneProducts.addComponent(emphasize("Type      "));
        ligneProducts.addComponent(emphasize("Prix       "));
        ligneProducts.addComponent(emphasize("Description         "));

        productsSection.addComponent(ligneProducts);

        productsSection.addComponent(new EmptySpace(new TerminalSize(0,1)));

        Panel articles = new Panel(new LinearLayout(Direction.VERTICAL));
        articles.addComponent(sectionHeader("Articles de la commande:"));
        articles.addComponent(new EmptySpace(new TerminalSize(0,1)));


        double sousTotal = controller.getSousTotal();
        double reduction = controller.getReduction();
        double total = controller.getTotal();

        articles.addComponent(priceSummary(sousTotal, reduction, total));

        Panel ligneArticles = new Panel(new LinearLayout(Direction.HORIZONTAL));
        ligneArticles.addComponent(emphasize("Type     "));
        ligneArticles.addComponent(emphasize("Produit         "));
        ligneArticles.addComponent(emphasize("Quantité"));
        ligneArticles.addComponent(emphasize("Prix unitaire"));
        ligneArticles.addComponent(emphasize("Total"));

        articles.addComponent(ligneArticles);


        articles.addComponent(new EmptySpace(new TerminalSize(0, 4)));

        Panel buttons = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttons.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        buttons.addComponent(new Button("Modifier la commande")); // ouvre vue de confirmation screenshot 23
        buttons.addComponent(new EmptySpace(new TerminalSize(1, 0)));
        buttons.addComponent(new Button("Annuler")); // dirige vers la vue order_delete
        buttons.addComponent(new EmptySpace(new TerminalSize(1, 0)));
        buttons.addComponent(new Button("Retour", this::close));


        infoSection.addTo(root);
        productsSection.addTo(root);
        articles.addTo(root);
        buttons.addTo(root);

        setComponent(root);

    }
    // méthode couleur + style css
    private Component sectionHeader(String title) {
        Label header = new Label(title);
        header.addStyle(SGR.BOLD);
        header.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        return header;
    }

    // méthode qui souligne + style css
    private Component emphasize(String title) {
        Panel headerPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        // Label principal (gras + couleur)
        Label header = new Label(title);
        header.addStyle(SGR.BOLD);
        header.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);

        // Soulignement
        String underline = title.replaceAll(".", "─");

        Label underlineLabel = new Label(underline);

        // Assemblage vertical
        headerPanel.addComponent(header);
        headerPanel.addComponent(underlineLabel);

        return headerPanel;
    }
    // méthode qui fait le calcul du prix de la commande + style css
    private Component priceSummary(double sousTotal, double reduction, double total) {
        Panel summary = new Panel(new LinearLayout(Direction.HORIZONTAL));

        String texte = String.format(
                "Sous-total: %.2f €  -  Réduction: %.2f €  =  Total: %.2f €",
                sousTotal, reduction, total
        );

        Label lbl = new Label(texte);
        lbl.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        lbl.addStyle(SGR.BOLD);

        summary.addComponent(lbl);
        return summary;
    }

}