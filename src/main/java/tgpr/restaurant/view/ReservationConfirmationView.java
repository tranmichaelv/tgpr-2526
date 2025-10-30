package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;

import java.util.List;

public class ReservationConfirmationView extends DialogWindow {


        public ReservationConfirmationView() {
            super("Réservation préparée");
            setHints(List.of(Hint.CENTERED));

            Panel content = Panel.verticalPanel(1);

            // Titolo
            content.addComponent(new Label("Réservation préparée:"));
            content.addComponent(new EmptySpace(new TerminalSize(0, 1)));

            // Dettagli prenotazione (inizialmente placeholder)
            content.addComponent(new Label("Date: "));
            content.addComponent(new Label("Nombre de personnes: "));
            content.addComponent(new Label("Service: "));
            content.addComponent(new Label("Table: "));
            content.addComponent(new EmptySpace(new TerminalSize(0, 1)));

            // Messaggio finale
            content.addComponent(new Label("Vous allez être redirigé vers la page de connexion pour confirmer votre réservation."));
            content.addComponent(new EmptySpace(new TerminalSize(0, 1)));

            // Pulsante OK
            Button okButton = new Button("OK", this::close);
            content.addComponent(okButton);

            setComponent(content);
        }
}
