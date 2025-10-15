package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.ReservationController;

import java.util.ArrayList;
import java.util.List;

 /*
        Planning pour la réalisation de cette view (ref.: screenshots --> diapositives 4 et 5)
        PHASE 1
            a) réalisation des 3 lignes suivants:
                - fields nb. personnes
                - service
                - Table assignée
            * Utilisation du gridPanel pour la disposition en deux colonnes (à tester)
        PHASE 2
            a) réalisation du calendrier (a chercher s'il existe dans la librairie Lanterna)
                * implémentation des buttons pour "précedent" et "suivant"
        PHASE 3
            a) Buttons "Annuler" et "Confirmer la réservation"

        N.B.: le planning pourrait être sujet à changement après consultation avec les dévs s'occupant de la partie "Reservation" (dans ce cas çi Maddy)
*/

public class ReservationView extends BasicWindow {
    private final ReservationController controller;

    public ReservationView(ReservationController controller) {

        this.controller = controller;

        setTitle("Réserver une table");
        setHints(List.of(Hint.CENTERED));

        Panel root = Panel.verticalPanel(1);
        setComponent(root);
        empty().addTo(root); // tool d'éspacement vertical
        for (Label label : headerIfGuest()) {
            label.addTo(root);
        }  // à réaliser pour headerIfGuest : implémentation sur les conditions d'affichage (sur le modèle)
    }

    private List<Label> headerIfGuest() { // création List des deux labels composant le "header" (reminder du login à effectuér)
        List<Label> headerIfGuest = new ArrayList<>();
        headerIfGuest.add(new Label("Réservation en tant qu'invité"));
        headerIfGuest.add(new Label("Vous pourrez confirmer votre réservation après vous être connecté"));
        headerIfGuest.get(0).setForegroundColor(TextColor.ANSI.BLACK_BRIGHT); // changement de la couleur, conformément au screenshot fourni par le prof
        return headerIfGuest;
    }

    private Label empty() {
        Label inf = new Label("");
        return inf;
    }
}
