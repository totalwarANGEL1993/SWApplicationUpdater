package twa.siedelwood.updater.ui.components.frame;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

/**
 * Fenster der Applikation.
 */
public class MainWindow extends AbstractWindow {
    private final int windowHeight = 300;
    private final int windowWidth = 400;

    private JPanel mainPanel;

    @Setter
    @Getter
    private String windowTitle;

    @Setter
    @Getter
    private String version;

    @Setter
    @Getter
    private String targetAppName;

    public MainWindow() {
        super();
    }

    @Override
    public void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(0, 0, windowWidth, windowHeight);
        setTitle(windowTitle);
        setLocationRelativeTo(null);
        setResizable(false);

        mainPanel = new JPanel(null);
        mainPanel.setBounds(0, 0, windowWidth, windowHeight);
        add(mainPanel);

        setVisible(true);
    }
}
