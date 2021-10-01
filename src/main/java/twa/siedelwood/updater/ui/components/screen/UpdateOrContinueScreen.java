package twa.siedelwood.updater.ui.components.screen;

import lombok.Getter;
import twa.siedelwood.updater.ui.components.frame.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Dieses Panel wird für die Aktualisierung der Anwendung genutzt.
 *
 * Alle anderen Panel werden ausgeblendet und dieses an ihrer Stelle angezeigt.
 */
public class UpdateOrContinueScreen extends JPanel {
    private MainWindow parentWindow;
    private int panelHeight;
    private int panelWidth;

    @Getter
    private JTextArea changeLog;

    /**
     * @param window Fenster
     * @param width  Breite
     * @param height Höhe
     */
    public UpdateOrContinueScreen(MainWindow window, int width, int height) {
        super();
        parentWindow = window;
        panelHeight = height;
        panelWidth = width;
    }

    /**
     * {@inheritDoc}
     */
    public void initPanel() {
        setLayout(null);
        setBounds(0, 0, panelWidth, panelHeight);

        createControls();
    }

    private void createControls() {
        JLabel textLabel = new JLabel(
            "<html><span style=\"text-align: center;\">Ein Update steht zum Download bereit!</span></html>"
        );
        textLabel.setBounds((int) (panelWidth * 0.50 -110), (int) (panelHeight * 0.40 -40), 300, 12);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        add(textLabel);

        JButton confirm = new JButton("<html><span style=\"font-size: 18px;\">Aktualisieren</span></html>");
        confirm.setName("UpdatePage_ConfirmUpdate");
        confirm.addActionListener(parentWindow.getApplicationInterfaceController());
        confirm.setBounds((int) (panelWidth * 0.5 -90),  (int) (panelHeight * 0.40 -10),  180, 35);
        add(confirm);

        JButton decline = new JButton("Ignorieren");
        decline.setName("UpdatePage_IgnoreUpdate");
        decline.addActionListener(parentWindow.getApplicationInterfaceController());
        decline.setBounds((int) (panelWidth * 0.5 -60), panelHeight -100, 120, 25);
        add(decline);

        JLabel versionLabel = new JLabel("<html>Version: " +parentWindow.getVersion()+ "</html>");
        versionLabel.setBounds(10, panelHeight - 60, 200, 15);
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        add(versionLabel);
    }
}
