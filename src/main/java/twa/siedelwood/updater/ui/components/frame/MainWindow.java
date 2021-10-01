package twa.siedelwood.updater.ui.components.frame;

import lombok.Getter;
import lombok.Setter;
import twa.siedelwood.updater.ui.components.screen.OngoingProcessScreen;
import twa.siedelwood.updater.ui.components.screen.RunApplicationScreen;
import twa.siedelwood.updater.ui.components.screen.UpdateApplicationScreen;
import twa.siedelwood.updater.ui.components.screen.UpdateOrContinueScreen;

import javax.swing.*;

/**
 * Fenster der Applikation.
 */
public class MainWindow extends AbstractWindow {
    private final int windowHeight = 450;
    private final int windowWidth = 600;

    @Setter
    @Getter
    private String windowTitle;

    @Setter
    @Getter
    private String version;

    @Setter
    @Getter
    private String targetAppName;

    private JPanel mainPanel;
    @Getter
    private OngoingProcessScreen ongoingProcessScreen;
    @Getter
    private UpdateOrContinueScreen updateOrContinueScreen;
    @Getter
    private UpdateApplicationScreen updateApplicationScreen;
    @Getter
    private RunApplicationScreen runApplicationScreen;

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

        ongoingProcessScreen = new OngoingProcessScreen(this, windowWidth, windowHeight);
        mainPanel.add(ongoingProcessScreen);
        ongoingProcessScreen.initPanel();

        updateOrContinueScreen = new UpdateOrContinueScreen(this, windowWidth, windowHeight);
        mainPanel.add(updateOrContinueScreen);
        updateOrContinueScreen.initPanel();

        updateApplicationScreen = new UpdateApplicationScreen(this, windowWidth, windowHeight);
        mainPanel.add(updateApplicationScreen);
        updateApplicationScreen.initPanel();

        runApplicationScreen = new RunApplicationScreen(this, windowWidth, windowHeight);
        mainPanel.add(runApplicationScreen);
        runApplicationScreen.initPanel();

        setVisible(true);
        setOngoingProcessPanelVisibility(true);
        setUpdateOrContinueScreen(false);
        setUpdateApplicationScreen(false);
        setRunApplicationScreen(false);
    }

    public void setOngoingProcessPanelVisibility(boolean flag) {
        ongoingProcessScreen.setVisible(flag);
    }

    public void setUpdateOrContinueScreen(boolean flag) {
        updateOrContinueScreen.setVisible(flag);
    }

    public void setUpdateApplicationScreen(boolean flag) {
        updateApplicationScreen.setVisible(flag);
    }

    public void setRunApplicationScreen(boolean flag) {
        runApplicationScreen.setVisible(flag);
    }
}
