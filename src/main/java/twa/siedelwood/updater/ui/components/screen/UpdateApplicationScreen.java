package twa.siedelwood.updater.ui.components.screen;

import lombok.Getter;
import twa.siedelwood.updater.ui.components.frame.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Dieses Panel wird für die Aktualisierung der Quelldateien genutzt.
 *
 * Alle anderen Panel werden ausgeblendet und dieses an ihrer Stelle angezeigt.
 */
public class UpdateApplicationScreen extends JPanel {
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
    public UpdateApplicationScreen(MainWindow window, int width, int height) {
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

        createUpdateGoup();
        createControlGoup();
    }

    /**
     * Zeigt Zustimmen Button und Ablehnen Button an.
     */
    private void createControlGoup() {
        JButton decline = new JButton("Abbrechen");
        decline.setName("UpdateDetailsPage_CancleUpdate");
        decline.addActionListener(parentWindow.getApplicationInterfaceController());
        decline.setBounds(10, panelHeight -100, 120, 25);
        add(decline);

        JButton confirm = new JButton("Aktualisieren");
        confirm.setName("UpdateDetailsPage_ConfirmUpdate");
        confirm.addActionListener(parentWindow.getApplicationInterfaceController());
        confirm.setBounds(panelWidth -145, panelHeight -100, 120, 25);
        confirm.requestFocus();
        add(confirm);

        JLabel versionLabel = new JLabel("<html>Version: " +parentWindow.getVersion()+ "</html>");
        versionLabel.setBounds(10, panelHeight - 60, 200, 15);
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        add(versionLabel);
    }

    /**
     * Zeigt den Viewer für die Änderungen an-
     */
    private void createUpdateGoup() {
        String debugText = ""+
            "Version: 2.7.3\nÄnderungen:\n- Bugfix: Timing-Problem beim direkten Bezahlen von interaktiven" +
            " Objekten gefixt.\n\n------------------------------\n\nVersion: 2.7.2\nÄnderungen:\n- Bugfix: " +
            "Automatische Behavior werden nun hinzugefügt.\n\n------------------------------\n\nVersion: " +
            "2.7.1\nÄnderungen:\n- Bugfix: Funktionsreferenzen in *_MapScriptFunction korrigiert.\n- " +
            "Bugfix: :CountSoldiers() gibt nun korrekte Werte zurück.\n";

        changeLog = new JTextArea();
        changeLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        changeLog.setDisabledTextColor(new Color(40, 40, 40));
        changeLog.setWrapStyleWord(true);
        changeLog.setEnabled(false);
        changeLog.setVisible(true);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(changeLog);
        scrollPane.setVisible(true);
        scrollPane.setBounds(10, 45, panelWidth -35, panelHeight -150);
        changeLog.setSize(panelWidth -35, panelHeight -150);
        add(scrollPane);

        String text = "" +
            "Ein Update steht zur Installation bereit.<br>" +
            "Folgend siehst du die letzten Änderungen. <b>Hinweis:</b> Du kannst das Update auch überspringen.";
        JLabel description = new JLabel("<html>" +text+ "</html>");
        description.setFont(new Font("Arial", Font.PLAIN, 12));
        description.setBounds(10, 10, panelWidth -20, 30);
        description.setVisible(true);
        add(description);
    }
}
