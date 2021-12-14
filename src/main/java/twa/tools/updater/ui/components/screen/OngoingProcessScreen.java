package twa.tools.updater.ui.components.screen;

import twa.tools.updater.ui.components.frame.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Dieses Panel wird als allgemeine Anzeige für einen laufenden Prozess genutzt.
 *
 * Alle anderen Panel werden ausgeblendet und dieses an ihrer Stelle angezeigt.
 */
public class OngoingProcessScreen extends JPanel {
    private MainWindow parentWindow;
    private int panelHeight;
    private int panelWidth;

    /**
     * @param window Fenster
     * @param width  Breite
     * @param height Höhe
     */
    public OngoingProcessScreen(MainWindow window, int width, int height) {
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

        JProgressBar progressBar = new JProgressBar();
        add(progressBar);
        progressBar.setIndeterminate(true);
        progressBar.setBounds(
            (int) (panelWidth * 0.2),
            (int) ((panelHeight * 0.40) + 0),
            (int) (panelWidth * 0.6),
            20
        );

        JLabel text = new JLabel("<html><b>Bitte warten...</b></html>");
        add(text);
        text.setBounds(
            (int) (panelWidth * 0.2),
            (int) ((panelHeight * 0.40) -15),
            (int) (panelWidth * 0.6),
            15
        );

        JLabel versionLabel = new JLabel("<html>Version: " +parentWindow.getVersion()+ "</html>");
        versionLabel.setBounds(10, panelHeight - 60, 200, 15);
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        add(versionLabel);
    }
}
