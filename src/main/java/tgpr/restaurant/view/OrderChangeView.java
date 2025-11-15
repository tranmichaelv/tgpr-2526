package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import tgpr.restaurant.controller.OrderAddController;
import tgpr.restaurant.controller.OrderChangeController;
import tgpr.restaurant.controller.OrderCreateController;
import tgpr.restaurant.controller.ProductCheckController;
import tgpr.restaurant.model.*;
import com.googlecode.lanterna.gui2.ComboBox;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class OrderChangeView extends BasicWindow {

    private final OrderChangeController controller;

    private ObjectTable<OrderItem> tableProduct;
    protected ComboBox<String> clientBox;
    protected   List<User> clients;
    protected ComboBox<String> tableBox;
    protected   List<Table> tables;
    protected ComboBox<String> serviceBox;
    protected List<Service> services;
    protected CheckBox takeAwayBox;
    protected TextBox nbPersonsBox;
    protected TextBox reductionBox;
    protected Label priceSummaryLabel;
    protected Panel articlesPanel;
    protected boolean creationFinalisee = false;

    public OrderChangeView(OrderChangeController controller) {
        this.controller = controller;

        setTitle("Modifier une commande");
        Panel root = new Panel();

        root.addComponent(new EmptySpace());
        root.addComponent(new EmptySpace());

        Panel infoSection = new Panel(new LinearLayout(Direction.VERTICAL));
        root.addComponent(sectionHeader("Informations de la commande:"));
        infoSection.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        Panel clientLine = new Panel(new LinearLayout(Direction.HORIZONTAL));
        clientLine.addComponent(new Label("Client : "));
        clientBox = new ComboBox<>();
        clientBox.setPreferredSize(new TerminalSize(30, 1));

        clients = controller.getClients();
        for (User u : clients) {
            clientBox.addItem(u.getLastName() + ", " + u.getFirstName());
        }

        // Sélectionne le client de la commande comme affichage de base
        int currentClientId = controller.getOrder().getClientId();
        int idx = IntStream.range(0, clients.size())
                .filter(i -> clients.get(i).getId() == currentClientId)
                .findFirst()
                .orElse(0); // si pas trouvé, on prend le premier client

        if (!clients.isEmpty()) {
            clientBox.setSelectedIndex(idx);
        }

         // Utilisateur connecté
        User logged = Security.getLoggedUser();

        if (logged != null
                && logged.getRole() == User.Role.Client
                && controller instanceof OrderCreateController) {

            // forcer l'order à utiliser le client connecté
            controller.getOrder().setClientId(logged.getId());

            // s'il n'est pas dans la liste, on l'ajoute
            int loggedIdx = IntStream.range(0, clients.size())
                    .filter(i -> clients.get(i).getId() == logged.getId())
                    .findFirst()
                    .orElse(-1);

            if (loggedIdx == -1) {
                clients.add(logged);
                clientBox.addItem(logged.getLastName() + ", " + logged.getFirstName());
                loggedIdx = clients.size() - 1;
            }
            clientBox.setSelectedIndex(loggedIdx);

            // on n'affiche pas le combo, on affiche un Label fixe
            Label clientNameLabel =
                    new Label(logged.getLastName() + ", " + logged.getFirstName());
            clientLine.addComponent(clientNameLabel);

        } else {
            // cas employé / boss / modification, on affiche la combo normale
            clientLine.addComponent(clientBox);
        }

        clientBox.addListener(new ComboBox.Listener() {
            @Override
            public void onSelectionChanged(int selectedIndex, int previousSelection, boolean fromUser) {
                if (selectedIndex >= 0 && selectedIndex < clients.size()) {
                    controller.getOrder().setClientId(clients.get(selectedIndex).getId());
                }
            }
        });

        infoSection.addComponent(clientLine);

        Panel tableLine = new Panel(new LinearLayout(Direction.HORIZONTAL));
        tableLine.addComponent(new Label("Table:"));

        tableBox = new ComboBox<>();
        tableBox.setPreferredSize(new TerminalSize(28, 1));



        Panel serviceLine = new Panel(new LinearLayout(Direction.HORIZONTAL));
        serviceLine.addComponent(new Label("Service:"));
        serviceBox = new ComboBox<>();
        serviceBox.setPreferredSize(new TerminalSize(26, 1));
        services = controller.getAvailableServices();
        for (Service s : services) {
            serviceBox.addItem(s.getName());
        }

        // sélectionner le service courant (celui de la commande)
        int currentServiceId = controller.getOrder().getServiceId();
        int selectedIdx = -1;
        for (int k = 0; k < services.size(); k++) {
            if (services.get(k).getId() == currentServiceId) {
                selectedIdx = k;
                break;
            }
        }

        if (!services.isEmpty() && selectedIdx >= 0) {
            serviceBox.setSelectedIndex(selectedIdx);
        } else if (!services.isEmpty()) {
            serviceBox.setSelectedIndex(0);
        } else {
            serviceBox.addItem("(aucun service disponible)");
            serviceBox.setSelectedIndex(0);
            serviceBox.setEnabled(false);
        }

        //  Maintenant que le service courant est bien défini,
        // charger les tables disponibles pour ce service
        List<Table> newTables = controller.getAvailableTables();
        tables = newTables;
        tableBox.clearItems();
        tableBox.addItem("Aucune");
        for (Table t : newTables) {
            tableBox.addItem("Table " + t.getId() + " (" + t.getCapacity() + "p)");
        }

        //  Sélectionner la table de la commande comme affichage de base
        Integer currentTableId = controller.getOrder().getTableId();
        if (currentTableId == null) {
            tableBox.setSelectedIndex(0); // à emporter
        } else {
            int idx2 = 0; // à emporter
            for (int i = 0; i < tables.size(); i++) {
                if (tables.get(i).getId() == currentTableId) {
                    idx2 = i + 1;
                    break;
                }
            }
            tableBox.setSelectedIndex(idx2);
        }


        serviceBox.addListener(new ComboBox.Listener() {
            @Override
            public void onSelectionChanged(int selectedIndex, int previousSelection, boolean fromUser) {
                if (selectedIndex < 0 || selectedIndex >= services.size()) return;

                //  mettre à jour le service choisi sur la commande
                controller.getOrder().setServiceId(services.get(selectedIndex).getId());

                //  mémoriser l’ID de la table actuellement sélectionnée dans l’UI
                Integer uiSelectedTableId = null;
                int currentIdx = tableBox.getSelectedIndex(); // 0 = "Aucune"
                if (currentIdx > 0 && currentIdx - 1 < tables.size()) {
                    uiSelectedTableId = tables.get(currentIdx - 1).getId();
                }

                //  recharger les tables disponibles pour CE service
                List<Table> newTables = controller.getAvailableTables();
                tables = newTables;

                //  rebâtir la combo
                tableBox.clearItems();
                tableBox.addItem("Aucune");
                for (Table t : newTables) {
                    tableBox.addItem("Table " + t.getId() + " (" + t.getCapacity() + "p)");
                }

                //  essayer de re-sélectionner la table que l’utilisateur avait choisie (si encore disponible)
                int idxToSelect = 0; // "Aucune"
                if (uiSelectedTableId != null) {
                    for (int j = 0; j < newTables.size(); j++) {
                        if (newTables.get(j).getId() == uiSelectedTableId) { idxToSelect = j + 1; break; }
                    }
                } else {
                    // sinon, tenter de garder la table déjà enregistrée sur la commande
                    Integer currentTableIdInModel = controller.getOrder().getTableId();
                    if (currentTableIdInModel != null) {
                        for (int j = 0; j < newTables.size(); j++) {
                            if (newTables.get(j).getId() == currentTableIdInModel) { idxToSelect = j + 1; break; }
                        }
                    }
                }
                tableBox.setSelectedIndex(idxToSelect);

                invalidate();
            }
        });

        tableLine.addComponent(tableBox);
        infoSection.addComponent(tableLine);
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
        nbPersonsBox.setValidationPattern(java.util.regex.Pattern.compile("\\d*"));
        peopleLine.addComponent(nbPersonsBox);
        infoSection.addComponent(peopleLine);

        // si c'est une commande à emporter alors,
        // on affiche 'Aucune' pour Table et 1 pour nb personne
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
        reductionBox.setPreferredSize(new TerminalSize(10, 1));
        Double d = controller.getOrder().getDiscount();
        reductionBox.setText(d == null ? "" : String.format("%.2f", d));

        reductionLine.addComponent(reductionBox);
        infoSection.addComponent(reductionLine);
        // Mise à jour dynamique du prix quand on tape dans "réduction"
        reductionBox.setTextChangeListener((newText, changedByUser) -> {
            // Nettoyer le texte
            String clean = newText.replace(',', '.').trim();

            // Vérifie et limite à 2 décimales
            if (clean.matches("\\d*\\.?\\d{0,2}")) {
                // Si ok, on accepte et met à jour le modèle
                Double value = parseReduction(clean);
                controller.getOrder().setDiscount(value);
                rebuildArticlesSection();
                try { getTextGUI().updateScreen(); } catch (Exception ignored) {}
            } else {
                // Trop de décimales, on tronque
                if (clean.contains(".")) {
                    int dotIndex = clean.indexOf(".");
                    if (dotIndex + 3 < clean.length()) {
                        clean = clean.substring(0, dotIndex + 3);
                    }
                }
                // Applique la valeur corrigée dans la TextBox
                reductionBox.setText(clean);
            }
        });

        infoSection.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        Label serviceLabel = new Label("Services disponibles:");
        serviceLabel.addStyle(SGR.ITALIC);
        List<Service> servicesDisponibles = controller.getAvailableServices();
        String nomsServices = servicesDisponibles.stream()
                .map(Service::getName)
                .collect(Collectors.joining(", "));

        serviceLabel.setText("Services disponibles : " + nomsServices);
        infoSection.addComponent(serviceLabel);
        infoSection.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        Panel productsSection = new Panel(new LinearLayout(Direction.VERTICAL));
        productsSection.addComponent(sectionHeader("Produits disponibles:"));
        productsSection.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        // Récupère la vue du contrôleur ProductCheck
        ProductCheckController pcc = new ProductCheckController(controller.getOrder().getId());
        pcc.setOnOrderChanged(this::refreshArticles);
        ProductCheckView productCheckView = pcc.getView();
        productsSection.addComponent(productCheckView);
        productsSection.addComponent(new EmptySpace(new TerminalSize(0,1)));

        articlesPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        // construit la section à partir de l'état courant
        rebuildArticlesSection();

        Panel ligneArticles = new Panel(new LinearLayout(Direction.HORIZONTAL));
        tableProduct = new ObjectTable<>(
                new ColumnSpec<>("Type     ", OrderItem::getProductType),
                new ColumnSpec<>("Produit         ", OrderItem::getProductName),
                new ColumnSpec<>("Quantité", OrderItem::getQuantity),
                new ColumnSpec<>("Prix unitaire", OrderItem::getPrice),
                new ColumnSpec<>("Total      ", OrderItem::getTotal));

        ligneArticles.addComponent(tableProduct);

        for (var item : controller.getOrder().getOrderItems()) {
            Panel ligne = new Panel(new LinearLayout(Direction.HORIZONTAL));
            var product = item.getProduct();
            ligne.addComponent(new Label(String.format("%-10s", product.getType().toString())));
            ligne.addComponent(new Label(String.format("%-18s", product.getName())));
            ligne.addComponent(new Label(String.format("%-10d", item.getQuantity())));
            ligne.addComponent(new Label(String.format("%-10.2f €", item.getPrice())));
            ligne.addComponent(new Label(String.format("%.2f €", item.getQuantity() * item.getPrice())));
        }

        Panel buttons = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttons.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        String label = (controller instanceof OrderCreateController)
                ? "Créer la commande"
                : "Modifier la commande";
        buttons.addComponent(new Button(label, () -> {
            try {
                   // Client
                int cIdx = clientBox.getSelectedIndex();
                if (cIdx >= 0 && cIdx < clients.size()) {
                    controller.getOrder().setClientId(clients.get(cIdx).getId());
                }
                // Table sélectionnée
                int tIdx = tableBox.getSelectedIndex(); // 0 = "Aucune"
                if (tIdx <= 0) {
                    controller.getOrder().setTableId(null);
                } else {
                    controller.getOrder().setTableId(tables.get(tIdx - 1).getId()); // ← tables mise à jour
                }
                //  Service sélectionné
                int sIdx = serviceBox.getSelectedIndex();
                if (sIdx >= 0 && sIdx < services.size()) {
                    controller.getOrder().setServiceId(services.get(sIdx).getId());
                }

                //  Nombre de personnes
                String txt = nbPersonsBox.getText().trim();
                if (txt.isEmpty()) throw new IllegalArgumentException("Le nombre de personnes est obligatoire.");
                int nb = Integer.parseInt(txt);
                controller.getOrder().setEffectivePeopleCount(nb); // ou setNumberOfPeople(nb)

                if (controller.getOrder().getTableId() != null) {
                    // Recalcule les tables réellement disponibles maintenant
                    List<Table> avail = controller.getAvailableTables();
                    Integer tid = controller.getOrder().getTableId();
                    boolean stillFree = avail.stream().anyMatch(t -> t.getId() == tid);
                    if (!stillFree) {
                        throw new IllegalArgumentException(
                                "La table choisie n'est plus disponible pour ce service à cette date."
                        );
                    }
                }
                if (controller instanceof OrderCreateController occ) {
                    occ.validateForCreation();
                }
                //  Valider
                controller.getOrder().validate();

                controller.getOrder().save();

                creationFinalisee = true;

                // Affiche vue OrderAdd
                BasicWindow win = getWindow(controller);
                getTextGUI().addWindowAndWait(win);

                this.close();
                // ne ferme plus la page apres avoir commandé avec un client
                // redirect vers la liste des commandes
                if (controller instanceof OrderCreateController occ && Security.isClient()) {
                    occ.goBackToList();
                }

            } catch (NumberFormatException e) {
                MessageDialog.showMessageDialog(getTextGUI(), "Erreur",
                        "Veuillez entrer un nombre de personnes valide.", MessageDialogButton.OK);
            } catch (IllegalArgumentException e) {
                MessageDialog.showMessageDialog(getTextGUI(), "Erreur de validation",
                        e.getMessage(), MessageDialogButton.OK);
            }
        }));
        buttons.addComponent(new EmptySpace(new TerminalSize(1, 0)));
        buttons.addComponent(new Button("Annuler", () -> {
            controller.resetToOriginal();
            Order o = controller.getOrder();

            int ci = 0;
            for (int i = 0; i < clients.size(); i++) {
                if (clients.get(i).getId() == o.getClientId()) { ci = i; break; }
            }
            clientBox.setSelectedIndex(ci);

            // Refléter l’état dans l’UI (takeAway/nb/service/tables/réduc)
            takeAwayBox.setChecked(o.getTakeAway());
            nbPersonsBox.setEnabled(!o.getTakeAway());
            tableBox.setEnabled(!o.getTakeAway());
            nbPersonsBox.setText(String.valueOf(o.getEffectivePeopleCount()));

            Double disc = o.getDiscount();
            reductionBox.setText(disc == null ? "" : String.format("%.2f", disc));

            // Services (jour de la commande)
            services = controller.getAvailableServices();
            serviceBox.clearItems();
            for (Service s : services) serviceBox.addItem(s.getName());
            int si = 0; for (int i = 0; i < services.size(); i++) if (services.get(i).getId() == o.getServiceId()) { si = i; break; }
            if (!services.isEmpty()) serviceBox.setSelectedIndex(si);

            // Tables (date + service remis)
            tables = controller.getAvailableTables();
            tableBox.clearItems();
            tableBox.addItem("Aucune");
            for (Table t : tables) tableBox.addItem("Table " + t.getId() + " (" + t.getCapacity() + "p)");
            Integer tId = o.getTableId();
            int ti = 0; if (tId != null) for (int i = 0; i < tables.size(); i++) if (tables.get(i).getId() == tId) { ti = i + 1; break; }
            tableBox.setSelectedIndex(ti);

            // Articles : totaux + lignes
            rebuildArticlesSection();

            try { getTextGUI().updateScreen(); } catch (Exception ignored) {}
        }));
        buttons.addComponent(new EmptySpace(new TerminalSize(1, 0)));
        buttons.addComponent(new Button("Retour", () -> {
            if (controller instanceof OrderCreateController occ && Security.isClient()) {

                // 1) Si jamais la commande a déjà été créée en BD (id != 0),
                //    on la supprime explicitement
                Order o = occ.getOrder();
                if (o.getId() != 0) {
                    o.delete();
                }

                // 2) On met creationFinalisee à true pour que close()
                //    ne rappelle PAS resetToOriginal() (et ne retente
                //    pas un delete ou autre chose)
                creationFinalisee = true;

                // 3) On navigue vers la liste client
                occ.goBackToList();   // navigateTo(new OrderListClientController());

            } else {
                // Employé / boss : simple fermeture (fenêtre modale)
                this.close();
            }
        }));



        infoSection.addTo(root);
        productsSection.addTo(root);
        articlesPanel.addTo(root);
        buttons.addTo(root);

        setComponent(root);

    }
        // méthode pour mettre la vue OrderAdd au milieu quand on crée la commande modifiée
    protected static BasicWindow getWindow(OrderChangeController controller) {
        OrderAddController nextController = new OrderAddController(controller.getOrder());
        BasicWindow win = nextController.getView();
        win.setFixedSize(new TerminalSize(60, 20));
        win.setHints(java.util.Arrays.asList(
                Hint.CENTERED,
                Hint.MODAL
        ));
        return win;
    }


    // méthode couleur + style css
    protected Component sectionHeader(String title) {
        Label header = new Label(title);
        header.addStyle(SGR.BOLD);
        header.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        return header;
    }

    // méthode qui souligne + style css
    protected Component emphasize(String title) {
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
    protected Component priceSummary(double sousTotal, double reduction, double total) {
        Panel summary = new Panel(new LinearLayout(Direction.HORIZONTAL));

       priceSummaryLabel = new Label(String.format(
               "Sous-total: %.2f €  -  Réduction: %.2f €  =  Total: %.2f €",
               sousTotal, reduction, total
       ));

        priceSummaryLabel.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        priceSummaryLabel.addStyle(SGR.BOLD);

        summary.addComponent(priceSummaryLabel);
        return summary;
    }
    // convertit le texte dans Réduction en décimal valide
    private Double parseReduction(String s) {
        if (s == null) return null;
        s = s.trim();
        // champ vide, pas de réduction (null)
        if (s.isEmpty()) return 0.00;
        // accepter la virgule
        s = s.replace(',', '.');
        try {
            double v = Double.parseDouble(s);
            // borne inférieure
            if (v < 0) v = 0;
            //  éviter réduc > sous-total
            double st = controller.getSousTotal();
            if (v > st) v = st;
            return v;
        } catch (NumberFormatException e) {
            // invalide, ignorer (affiché 0.00 en bas)
            return 0.00;
        }
    }

        // Affichage article
        private void rebuildArticlesSection() {
            if (articlesPanel == null) return;
            articlesPanel.removeAllComponents();

            articlesPanel.addComponent(sectionHeader("Articles de la commande:"));
            articlesPanel.addComponent(new EmptySpace(new TerminalSize(0,1)));

            // Totaux à jour
            double sousTotal = controller.getSousTotal();
            double reduction = controller.getReduction();
            double total = controller.getTotal();
            articlesPanel.addComponent(priceSummary(sousTotal, reduction, total));

            final int W_TYPE  = 13;
            final int W_PROD  = 28;
            final int W_QTY   = 10;
            final int W_PRICE = 14;
            final int W_TOTAL = 12;

            Panel ligneArticles = new Panel(new LinearLayout(Direction.HORIZONTAL));
            ligneArticles.addComponent(emphasize(String.format("%-" + W_TYPE  + "s", "Type")));
            ligneArticles.addComponent(emphasize(String.format("%-" + W_PROD  + "s", "Produit")));
            ligneArticles.addComponent(emphasize(String.format("%-" + W_QTY   + "s", "Quantité")));
            ligneArticles.addComponent(emphasize(String.format("%-" + W_PRICE + "s", "Prix unitaire")));
            ligneArticles.addComponent(emphasize(String.format("%-" + W_TOTAL + "s", "Total")));
            articlesPanel.addComponent(ligneArticles);

            // on n'affiche plus les items dont la quantité vaut 0.
            for (var item : controller.getOrder().getOrderItems()) {

                if (item.getQuantity() == 0) {
                    // quantité 0 = on considère que l’article est "supprimé" visuellement
                    continue;
                }

                Panel ligne = new Panel(new LinearLayout(Direction.HORIZONTAL));
                var product = item.getProduct();

                String name = product.getName();
                if (name.length() > W_PROD) name = name.substring(0, W_PROD);

                ligne.addComponent(new Label(String.format("%-" + W_TYPE + "s", product.getType().toString())));
                ligne.addComponent(new Label(String.format("%-" + W_PROD + "s", name)));
                ligne.addComponent(new Label(String.format("%-"  + W_QTY  + "d", item.getQuantity())));
                ligne.addComponent(new Label(String.format("%" + (W_PRICE - 2) + ".2f\u00A0€", item.getPrice())));
                ligne.addComponent(new Label(String.format("%" + (W_TOTAL - 2) + ".2f\u00A0€", item.getQuantity() * item.getPrice())));

                articlesPanel.addComponent(ligne);
            }

            articlesPanel.addComponent(new EmptySpace(new TerminalSize(0, 4)));
        }

    private void refreshArticles() {
        //  Save la réduction tapée dans le modèle avant de reconstruire
        Double value = parseReduction(reductionBox.getText());
        controller.getOrder().setDiscount(value);

        //  Reconstruit la section Articles (totaux + lignes)
        Order fresh = Order.getByKey(controller.getOrder().getId());
        controller.getOrder().getOrderItems().clear();
        controller.getOrder().getOrderItems().addAll(fresh.getOrderItems());
        rebuildArticlesSection();
        try {
            getTextGUI().updateScreen();
        } catch (Exception ignored) {
        }
    }
    @Override
    public void close() {
        if (controller instanceof OrderCreateController occ && !creationFinalisee) {
            occ.resetToOriginal();
        }
        super.close();
    }
}