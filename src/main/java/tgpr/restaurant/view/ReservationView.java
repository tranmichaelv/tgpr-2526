package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import tgpr.restaurant.controller.ReservationController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ReservationView extends BasicWindow {
    public static final String[] DAYS = new String[]{"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
    public static final LocalDate today = LocalDate.now();
    private final ReservationController controller;
    private Label monthLabel;
    private Panel calendarGrid;
    private TextBox nbGuests;
    private ComboBox<String> midiSoir;
    private Label tableLabel;
    private LocalDate selectedDate;
    private Button confirm;

    public ReservationView(ReservationController controller) {

        this.controller = controller;


        setTitle("Réserver une table");
        setHints(List.of(Hint.CENTERED));

        Panel root = Panel.verticalPanel(1);

        setComponent(root);

        root.addComponent(new EmptySpace(new TerminalSize(0,1))); // upper padding

        createHeader().addTo(root);
        createGridLayout().addTo(root);
        createCalendarHeader().addTo(root);
        root.addComponent(createCalendarGridPanel());
        root.addComponent(buttonsAtBottom());

        root.addComponent(new EmptySpace(new TerminalSize(0,1)));
    }

    private Panel createHeader() {
        Panel createHeader = new Panel();
        for (Label label : headerIfGuest()) {
            label.addTo(createHeader);
        }
        return createHeader;// to-do-list for headerIfGuest : conditions (model)
    }

    private List<Label> headerIfGuest() { // List containing two labels composing the header (login reminder)
        List<Label> headerIfGuest = new ArrayList<>();
        headerIfGuest.add(new Label("Réservation en tant qu'invité"));
        headerIfGuest.add(new Label("Vous pourrez confirmer votre réservation après vous être connecté"));
        headerIfGuest.getFirst().setForegroundColor(TextColor.ANSI.BLACK_BRIGHT); // changing color
        return headerIfGuest;
    }

    private Panel createGridLayout() {
        Panel createGridLayout = new Panel();
        createGridLayout.setLayoutManager(new GridLayout(2)); // two columns

        // 1st row
        createGridLayout.addComponent(new Label("Nombre de personnes: "));
        nbGuests = new TextBox();
        nbGuests.setValidationPattern(Pattern.compile("[1-9][0-9]*")); // only integers allowed
        createGridLayout.addComponent(nbGuests);

        createGridLayout.addComponent(new EmptySpace(), GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.CENTER, true, false, 2, 1)); // to skip row

        // 2nd row
        createGridLayout.addComponent(new Label("Service: "));
        midiSoir = new ComboBox<>();
        midiSoir.addItem("midi");
        midiSoir.addItem("soir");
        midiSoir.setSelectedItem("midi");
        createGridLayout.addComponent(midiSoir);

        createGridLayout.addComponent(new EmptySpace(), GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.CENTER, true, false, 2, 1));

        // 3rd row
        createGridLayout.addComponent(new Label("Table assignée: "));
        tableLabel = new Label("(non attribuée)");
        createGridLayout.addComponent(tableLabel);
        return createGridLayout;
    }

    // Calendar's header showing prec and next buttons, alongside the month (adapted with DateTimeFormatter) and the year
    private Panel createCalendarHeader() {
        Panel header = new Panel(new LinearLayout(Direction.HORIZONTAL)).center();

        Button prec = (new Button("précedent", () -> {
            controller.goToPreviousMonth();
            refreshCalendar();
        }));
        Button next = (new Button("suivant", () -> {
            controller.goToNextMonth();
            refreshCalendar();
        }));
        monthLabel = new Label(controller.getCurrentMonth().atDay(1).format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH)));
        // gets current month (uppercase, English) and current year (OCTOBER 2025), then converted (lowercase, French --> octobre 2025)

        header.addComponent(prec);
        header.addComponent(monthLabel);
        header.addComponent(next);

        return header;
    }


    private void buildCalendarGrid() {
        calendarGrid.removeAllComponents();

        // printing each day (lun, mar, ...)
        for (String day : DAYS) {
            calendarGrid.addComponent(new Label(day).centerCenter());
        }

        // printing each day from current and (if needed) previous month
        List<LocalDate> calendarDays = controller.getCalendarDays(controller.getCurrentMonth());
        for (LocalDate date : calendarDays) {
            if (date == null) {
                // skipping empty cases (to reach the day of the week where the month starts)
                Label empty = new Label(" ");
                empty.setPreferredSize(new TerminalSize(8, 1));
                calendarGrid.addComponent(empty);
                continue;
            }

            Button dayButton = new Button(String.valueOf(date.getDayOfMonth()), () -> {
                selectedDate = date;
                controller.assignTable(nbGuests.getText(), midiSoir.getSelectedItem(), selectedDate); // calling assignTable method in ReservationController
            });


            // Overriding default renderer for buttons ("<" + string.content + ">") with setRenderer method below
            dayButton.setRenderer(new Button.DefaultButtonRenderer() {
                @Override
                public void drawComponent(TextGUIGraphics graphics, Button button) {
                    graphics.setBackgroundColor(button.isFocused() ? TextColor.ANSI.BLUE_BRIGHT : TextColor.ANSI.WHITE);
                    graphics.setForegroundColor(TextColor.ANSI.BLACK);
                    graphics.fill(' '); // filling empty spaces

                    String label = button.getLabel();
                    int x = (graphics.getSize().getColumns() - label.length()) / 2; // to fix the text in the center of the button (no matter its area)
                    graphics.putString(x, 0, label);
                }
            });


//            if (current date is before today OR current day of week is monday) {
            if (date.isBefore(today) || date.getDayOfWeek() == DayOfWeek.MONDAY) {
                dayButton.setEnabled(false);
                calendarGrid.addComponent(dayButton.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER))
                        .setPreferredSize(new TerminalSize(5, 1)));
            } else {
                calendarGrid.addComponent(dayButton.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER))
                        .setPreferredSize(new TerminalSize(5, 1))
                        .withBorder(Borders.singleLine()));
            }
        }
    }

    // method refresh to scroll calendar
    private void refreshCalendar() {
        monthLabel.setText(controller.getCurrentMonth().atDay(1).format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH)));
        buildCalendarGrid();
        invalidate();
    }

    // calendar grid (columns : 7)
    private Panel createCalendarGridPanel() {
        calendarGrid = new Panel(new GridLayout(7)).center();
        buildCalendarGrid();
        return calendarGrid;
    }

    // buttons "Annuler" and "Confirmer la réservation". Will be reworked later (to match model's logic)
    private Panel buttonsAtBottom() {
        Panel buttons = new Panel(new LinearLayout(Direction.HORIZONTAL)).center();
        Button cancel = new Button("Annuler");
        confirm = new Button("Confirmer la réservation", () -> {
            ReservationConfirmationView dialog = new ReservationConfirmationView();
            dialog.showDialog(getTextGUI());
        });
        confirm.setEnabled(false);
        buttons.addComponent(cancel);
        buttons.addComponent(confirm);
        return buttons;
    }

    // method allowing dynamic update of tableLabel
    public void updateTableLabel(String text) {
        tableLabel.setText(text);
        confirm.setEnabled(!"(non attribuée)".equals(text) && !"(aucune table disponible)".equals(text));
        invalidate();
    }

    // Method to show errors
    public void showError(String message) {
        MessageDialog.showMessageDialog(getTextGUI(), "Erreur", message);
    }
}