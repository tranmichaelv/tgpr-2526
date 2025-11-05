package tgpr.framework;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.gui2.Window;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class ViewManager {
    private static MultiWindowTextGUI gui;
    private static final Stack<Window> windowStack = new Stack<>();

    // ==================== INITIALISATION ====================
    static {
        try {
            DefaultTerminalFactory factory = new DefaultTerminalFactory();
            factory.setForceTextTerminal(false);
            factory.setPreferTerminalEmulator(true);

            factory.setInitialTerminalSize(new TerminalSize(
                    Configuration.getInt("terminal.width"),
                    Configuration.getInt("terminal.height")
            ));

            factory.setTerminalEmulatorFontConfiguration(
                    SwingTerminalFontConfiguration.newInstance(
                            new Font(getFontName(), Font.PLAIN, Configuration.getInt("fontsize"))
                    )
            );

            SwingTerminalFrame terminal = (SwingTerminalFrame) factory.createTerminal();
            terminal.setResizable(false);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            terminal.setLocation(
                    (screenSize.width - terminal.getWidth()) / 2,
                    (screenSize.height - terminal.getHeight()) / 2
            );
            terminal.setTitle(Configuration.get("title"));

            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();
            gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(),
                    new EmptySpace(TextColor.ANSI.valueOf(Configuration.get("backgroundColor"))));

        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    // ==================== SYSTEM METHODS ====================
    private static String getFontName() {
        switch (OSType.DETECTED) {
            case Windows: return Configuration.get("fontname.windows");
            case Linux: return Configuration.get("fontname.linux");
            case MacOS: return Configuration.get("fontname.mac");
            default: return Configuration.get("fontname.other");
        }
    }

    // ==================== NAVIGATION ====================
    public static void navigateTo(Window window) {
        if (!windowStack.isEmpty()) {
            windowStack.peek().close();
        }
        windowStack.push(window);
        gui.addWindowAndWait(window);
    }

    public static void navigateBack() {
        if (!windowStack.isEmpty()) {
            Window current = windowStack.pop();
            current.close();
            if (!windowStack.isEmpty()) {
                gui.addWindowAndWait(windowStack.peek());
            }
        }
    }

    public static void showModal(DialogWindow window) {
        window.showDialog(gui);
    }



    public static void showModal(BasicWindow window) {
        gui.addWindowAndWait(window);
    }

    // ==================== MESSAGES ====================
    public static void showErrors(List<Error> errors) {
        showErrors(errors, "Erreur");
    }

    public static void showErrors(List<Error> errors, String title) {
        if (errors == null || errors.isEmpty()) return;
        String message = errors.stream()
                .map(Error::getMessage)
                .collect(Collectors.joining("\n"));
        MessageDialog.showMessageDialog(gui, title, message, MessageDialogButton.OK);
    }

    public static MessageDialogButton showMessage(String message, String title, MessageDialogButton... buttons) {
        return MessageDialog.showMessageDialog(gui, title, message, buttons);
    }

    public static String showMessage(String message, String title, String... buttons) {
        // fallback simple message
        MessageDialog.showMessageDialog(gui, title, message, MessageDialogButton.OK);
        return "OK";
    }
}
