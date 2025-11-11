package tgpr.restaurant.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.graphics.ThemeDefinition;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import tgpr.restaurant.controller.ReservationController;
import tgpr.restaurant.controller.ReservationEditController;
import tgpr.restaurant.model.Reservation;
import tgpr.restaurant.model.Service;
import tgpr.restaurant.model.Table;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ReservationEditView extends BasicWindow {
    public static final String[] DAYS = new String[]{"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
    public static final LocalDate today = LocalDate.now();
    private static final int MONTH_LABEL_WIDTH = 20;
    private final ReservationEditController controller;
    private Reservation reservation;
    private Label monthLabel;
    private Panel calendarGrid;
    private TextBox nbGuests;
    private ComboBox<String> midiSoir;
    private Label tableLabel;
    private LocalDate selectedDate;
    private Button confirm;
    private Button targetDate = null ;

    public ReservationEditView(ReservationEditController controller) {

        this.controller = controller;

        setTitle("Editer une réservation");
        setHints(List.of(Hint.CENTERED));
        Panel root = Panel.verticalPanel();
        setComponent(root);
    }

    // init method gathering all sub-panels
    public void init() {
        this.reservation = controller.getReservation();
        Panel root = (Panel) getComponent();
        root.addComponent(emptyRow());
        root.addComponent(header());
        root.addComponent(emptyRow());
        root.addComponent(currentReservationInfo());
    }

    private Panel emptyRow() {
        Panel empty = new Panel();
        empty.addComponent(new EmptySpace(new TerminalSize(0,1)));
        return empty;
    }

    private Panel header() {
        Panel header = new Panel(new LinearLayout());
        TextColor gray = new TextColor.RGB(80 ,80,80);
        header.addComponent(new Label("Réservation à modifier").setForegroundColor(gray).addStyle(SGR.BOLD));
        return header;
    }

    // Panel gathering information from the selected reservation
    private Panel currentReservationInfo() {
        Panel currentReservation = new Panel();
        currentReservation.setLayoutManager(new GridLayout(2));
        currentReservation.addComponent(new Label("Table: "));
        currentReservation.addComponent(new Label("Table " + reservation.getTable().getId() + " (" + reservation.getTable().getCapacity() + "p)"));
        currentReservation.addComponent(new Label("Date actuelle: "));
        currentReservation.addComponent(new Label(reservation.getReservationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        currentReservation.addComponent(new Label("Service: "));
        currentReservation.addComponent(new Label(reservation.getService().getName()));

        return currentReservation;
    };
}
