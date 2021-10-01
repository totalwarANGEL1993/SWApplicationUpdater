package twa.siedelwood.updater.controller.app;

import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import twa.siedelwood.updater.controller.git.GitController;
import twa.siedelwood.updater.ui.SwingMessageService;
import twa.siedelwood.updater.ui.components.frame.MainWindow;

/**
 * Controller zur Reaktion auf die Aktionen des Benutzers.
 */
@Component
public class ApplicationInterfaceController extends AbstractInterfaceController {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationInterfaceController.class);

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ApplicationFeatureController applicationFeatureController;
    @Autowired
    private SwingMessageService swingMessageService;

    @Value("${app.version}")
    private String appVersion;
    @Value("${app.name}")
    private String appName;

    @Value("${target.app.name}")
    private String targetAppName;

    @Setter
    @Getter
    private String[] parameter;

    private MainWindow windowFrame;

    /**
     * Startet die grafische Oberfläche der Applikation.
     * @param args
     */
    public void start(String[] args) {
        parameter = args;
        applicationFeatureController.setParameter(args);
        SwingUtilities.invokeLater(() -> {
            windowFrame = new MainWindow();
            windowFrame.setWindowTitle(appName);
            windowFrame.setVersion(appVersion);
            windowFrame.setTargetAppName(targetAppName);
            windowFrame.setApplicationInterfaceController(ApplicationInterfaceController.this);
            windowFrame.setApplicationFeatureController(applicationFeatureController);
            windowFrame.initComponents();
            checkForUpdates();
        });
    }

    private void checkForUpdates() {
        windowFrame.getOngoingProcessScreen().setVisible(true);
        Thread t = new Thread(() -> {
            int result = applicationFeatureController.checkForUpdates();
            if (result == -1) {
                swingMessageService.displayErrorMessage(
                    "Fehler",
                    "Es konnte nicht auf den Remote zugegriffen und nach einem Update gesucht werden!"
                );
                windowFrame.getOngoingProcessScreen().setVisible(false);
                windowFrame.getRunApplicationScreen().setVisible(true);
            }
            else if (result == 0) {
                windowFrame.getOngoingProcessScreen().setVisible(false);
                windowFrame.getRunApplicationScreen().setVisible(true);
            }
            else {
                windowFrame.getOngoingProcessScreen().setVisible(false);
                windowFrame.getUpdateOrContinueScreen().setVisible(true);
            }
        });
        t.start();
    }

    private void onInpendingUpdateAccepted() {
        LOG.info("DEBUG: Update is accepted.");
        windowFrame.getUpdateOrContinueScreen().setVisible(false);
        windowFrame.getOngoingProcessScreen().setVisible(true);
        Thread t = new Thread(() -> {
            String content = applicationFeatureController.getChangelog();
            windowFrame.getUpdateApplicationScreen().getChangeLog().setText(content);
            if (StringUtils.isBlank(content)) {
                swingMessageService.displayErrorMessage(
                    "Fehler",
                    "Die Versionshistorie konnte nicht geladen werden!"
                );
            }
            windowFrame.getOngoingProcessScreen().setVisible(false);
            windowFrame.getUpdateApplicationScreen().setVisible(true);
        });
        t.start();
    }

    private void onInpendingUpdateIgnored() {
        LOG.info("DEBUG: Update is rejected.");
        windowFrame.getUpdateOrContinueScreen().setVisible(false);
        onApplicationStarted();
    }

    private void onUpdateStarted() {
        LOG.info("DEBUG: Update is started.");
        windowFrame.getUpdateApplicationScreen().setVisible(false);
        windowFrame.getOngoingProcessScreen().setVisible(true);
        Thread t = new Thread(() -> {

        });
        t.start();
    }

    private void onUpdateCanceled() {
        LOG.info("DEBUG: Update is canceled.");
        windowFrame.getUpdateOrContinueScreen().setVisible(true);
        windowFrame.getUpdateApplicationScreen().setVisible(false);
    }

    private void onApplicationStarted() {
        LOG.info("DEBUG: Application is starded.");
        windowFrame.getRunApplicationScreen().setVisible(false);
        Thread t = new Thread(() -> {

        });
        t.start();
    }

    // Listeners //

    @Override
    protected void onActionPerformed(ActionEvent event, JComponent source) {
        if ("UpdatePage_ConfirmUpdate".equals(source.getName())) {
            onInpendingUpdateAccepted();
        }
        if ("UpdatePage_IgnoreUpdate".equals(source.getName())) {
            onInpendingUpdateIgnored();
        }
        if ("UpdateDetailsPage_ConfirmUpdate".equals(source.getName())) {
            onUpdateStarted();
        }
        if ("UpdateDetailsPage_CancleUpdate".equals(source.getName())) {
            onUpdateCanceled();
        }
        if ("RunPage_RunApplication".equals(source.getName())) {
            onApplicationStarted();
        }
    }

    @Override
    protected void onValueChanged(ListSelectionEvent event, JComponent source) {

    }
}
