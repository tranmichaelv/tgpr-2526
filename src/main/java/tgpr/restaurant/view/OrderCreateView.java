package tgpr.restaurant.view;

import tgpr.restaurant.controller.OrderCreateController;

public class OrderCreateView extends OrderChangeView {

    public OrderCreateView(OrderCreateController controller) {
        super(controller);
        setTitle("Nouvelle commande");


        //  Effacer les présélections de l’UI
        if (clientBox != null) clientBox.setSelectedIndex(-1);
        if (serviceBox != null) serviceBox.setSelectedIndex(-1);
        if (tableBox != null) {
            // laisse "Aucune" sélectionnée si présente
            if (tableBox.getItemCount() > 0) tableBox.setSelectedIndex(0);
        }
        if (takeAwayBox != null) takeAwayBox.setChecked(false);
        if (nbPersonsBox != null) nbPersonsBox.setText("");
        if (reductionBox != null) reductionBox.setText("");


    }


}