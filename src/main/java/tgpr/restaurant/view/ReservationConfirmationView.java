package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ReservationConfirmationView extends DialogWindow {


        public ReservationConfirmationView(LocalDate date, int numberOfPeople, String service, String tableInfo) {
            super("Réservation préparée");
            setHints(List.of(Hint.CENTERED));

            Panel content = Panel.verticalPanel(1);

            // title
            content.addComponent(new Label("Réservation préparée:"));

            // details about booked table
            content.addComponent(new Label("Date: " +  date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            content.addComponent(new Label("Nombre de personnes: " + numberOfPeople));
            content.addComponent(new Label("Service: " + service));
            content.addComponent(new Label("Table: " + tableInfo));
            content.addComponent(new Label("Vous allez être redirigé vers la page de connexion pour confirmer votre réservation."));

            // OK button (redirection)
            Button okButton = new Button("OK", this::close).left();
            content.addComponent(okButton);

            setComponent(content);
        }
}
