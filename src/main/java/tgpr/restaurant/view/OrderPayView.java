package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderPayController;
import tgpr.restaurant.model.Order;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.apache.commons.text.WordUtils.capitalize;

public class OrderPayView extends BasicWindow {
    private final OrderPayController controller;

    public OrderPayView(OrderPayController controller) {
        this.controller = controller;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Order order = controller.getOrder(); // on récupère la commande sélectionnée
        setTitle("Enregistrer le paiement");

        Panel root = new Panel(new LinearLayout(Direction.VERTICAL));
        root.addComponent(new EmptySpace());
        root.addComponent(new EmptySpace());

        root.addComponent(sectionHeader("Informations de la commande:"));
        root.addComponent(new EmptySpace());
        Panel info = new Panel(new GridLayout(2));
        // ajout des informations de la commande

        String client = (order != null && order.getClient() != null)
                ? order.getClient().getFirstName() + " " + order.getClient().getLastName()
                : "(inconnu)";
        String date = (order != null && order.getOrderDate() != null)
                ? order.getOrderDate().format(formatter) : "-";
        String table = (order != null && order.getTable() != null)
                ? "Table " + order.getTable().getId() : "Aucune";
        String type = (order != null && order.getTakeAway()) ? "À emporter" : "Sur place";

        info.addComponent(new Label("Client:" + client));
        info.addComponent(new Label("Date:" + date));
        info.addComponent(new Label("Table:" + table));
        info.addComponent(new Label("Type:" + type));


        root.addComponent(info);
        root.addComponent(new EmptySpace());

        root.addComponent(sectionHeader("Articles:"));
        if (order != null && order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            for (var item : order.getOrderItems()) {
                String name = item.getProduct().getName();
                int qty = item.getQuantity();
                double unit = item.getProduct().getPrice();
                double line = unit * qty;

                // pour l'affichage du prix
                String lineText = String.format(Locale.FRANCE,
                        "- %s x%d = %.2f €", name, qty, line);

                root.addComponent(new Label(lineText));
            }
        } else {
            root.addComponent(new Label("(Aucun article)"));
        }

        double sousTotal = (order != null)
                ? order.getOrderItems().stream()
                .mapToDouble(oi -> oi.getProduct().getPrice() * oi.getQuantity())
                .sum()
                : 0.0;
        double remise = (order != null && order.getDiscount() != null) ? order.getDiscount() : 0.0;
        double total = sousTotal - remise;

        Panel totals = new Panel(new GridLayout(2));

        // Sous-total (noir)
        Label lblSousTotal = new Label("Sous-total:");
        lblSousTotal.setForegroundColor(TextColor.ANSI.BLACK);
        Label valSousTotal = new Label(String.format(Locale.FRANCE, "%.2f €", sousTotal));
        valSousTotal.setForegroundColor(TextColor.ANSI.BLACK);

        // Remise (rouge)
        Label lblRemise = new Label("Remise:");
        lblRemise.setForegroundColor(TextColor.ANSI.RED);
        Label valRemise = new Label(String.format(Locale.FRANCE, "-%.2f €", Math.abs(remise)));
        valRemise.setForegroundColor(TextColor.ANSI.RED);

        // Total (vert)
        Label lblTotal = new Label("Total:");
        lblTotal.addStyle(SGR.BOLD);
        lblTotal.setForegroundColor(TextColor.ANSI.GREEN);
        Label valTotal = new Label(String.format(Locale.FRANCE, "%.2f €", total));
        valTotal.addStyle(SGR.BOLD);
        valTotal.setForegroundColor(TextColor.ANSI.GREEN);

        totals.addComponent(lblSousTotal); totals.addComponent(valSousTotal);
        totals.addComponent(lblRemise);    totals.addComponent(valRemise);
        totals.addComponent(lblTotal);     totals.addComponent(valTotal);


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

        // pré-sélection si déjà défini
        if (order != null && order.getPaymentMethod() != null) {
            String preset = capitalize(order.getPaymentMethod().name()); // Cash/Card/Other
            cbMethod.setSelectedItem(preset);
        }
        Panel buttons = new Panel(new GridLayout(2));

        // bouton confirm
        Button confirm = new Button("Confirmer le paiement", () -> {
            String selected = cbMethod.getSelectedItem();
            if (selected != null && order != null) {
                controller.confirmPayment(Order.PaymentMethod.valueOfIgnoreCase(selected));
            }
            close();
        });
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