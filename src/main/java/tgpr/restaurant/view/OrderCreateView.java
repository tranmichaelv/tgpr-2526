package tgpr.restaurant.view;

import com.googlecode.lanterna.gui2.BasicWindow;
import tgpr.restaurant.controller.OrderCreateController;
import tgpr.restaurant.model.Order;

public class OrderCreateView extends OrderChangeView {

    public OrderCreateView(OrderCreateController controller) {
        super(controller);                  // construit l'UI complète
        setTitle("Nouvelle commande");      // titre création


        //  Effacer les présélections de l’UI
        if (clientBox != null)     clientBox.setSelectedIndex(-1);
        if (serviceBox != null)    serviceBox.setSelectedIndex(-1);
        if (tableBox != null) {
            // laisse "Aucune" sélectionnée si présente
            if (tableBox.getItemCount() > 0) tableBox.setSelectedIndex(0);
        }
        if (takeAwayBox != null)   takeAwayBox.setChecked(false);
        if (nbPersonsBox != null)  nbPersonsBox.setText("");
        if (reductionBox != null)  reductionBox.setText("");

        //  Nettoyer le modèle (pour éviter des valeurs héritées)
        Order o = controller.getOrder();
        o.setClientId(0);          // ou 0 selon ton modèle
        o.setTableId(null);
        o.setTakeAway(false);
        o.setEffectivePeopleCount(0);
        o.setDiscount(null);



    }
}