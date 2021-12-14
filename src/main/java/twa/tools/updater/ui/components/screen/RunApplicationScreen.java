package twa.tools.updater.ui.components.screen;

import lombok.Getter;
import twa.tools.updater.ui.components.frame.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Dieses Panel wird für die Aktualisierung der Anwendung genutzt.
 *
 * Alle anderen Panel werden ausgeblendet und dieses an ihrer Stelle angezeigt.
 */
public class RunApplicationScreen extends JPanel {
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
    public RunApplicationScreen(MainWindow window, int width, int height) {
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
            "<html><span style=\"text-align: center;\">Die Anwendung ist aktuell und bereit.</span></html>"
        );
        textLabel.setBounds((int) (panelWidth * 0.50 -105), (int) (panelHeight * 0.40 -40), 300, 12);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        add(textLabel);

        JButton confirm = new JButton("<html><span style=\"font-size: 18px;\">Starten</span></html>");
        confirm.setName("RunPage_RunApplication");
        confirm.addActionListener(parentWindow.getApplicationInterfaceController());
        confirm.setBounds((int) (panelWidth * 0.5 -95),  (int) (panelHeight * 0.40 -10),  180, 35);
        add(confirm);

        JButton showlog = new JButton("Changelog");
        showlog.setName("RunPage_ShowChangeLog");
        showlog.addActionListener(parentWindow.getApplicationInterfaceController());
        showlog.setBounds((int) (panelWidth * 0.5 -60), panelHeight -100, 120, 25);
        add(showlog);

        JLabel versionLabel = new JLabel("<html>Version: " +parentWindow.getVersion()+ "</html>");
        versionLabel.setBounds(10, panelHeight - 60, 200, 15);
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        add(versionLabel);
    }
}
