package twa.tools.updater.controller.app;

import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import twa.tools.updater.controller.git.GitException;
import twa.tools.updater.ui.SwingMessageService;
import twa.tools.updater.ui.components.frame.MainWindow;

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
            try {
                Status status = applicationFeatureController.getRepositoryStatus();
                if (status.hasUncommittedChanges()) {
                    result = 1;
                }
                if (result == -1) {
                    swingMessageService.displayErrorMessage(
                        "Fehler",
                        "Es konnte nicht auf den Remote zugegriffen und nach einem Update gesucht werden!\n\n" +
                        applicationFeatureController.getLastException().getMessage()
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
            }
            catch (GitException e) {
                e.printStackTrace();
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
                    "Die Versionshistorie konnte nicht geladen werden!\n\n" +
                    applicationFeatureController.getLastException().getMessage()
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
            final int result = applicationFeatureController.updateTargetApplication();
            if (result == -1) {
                swingMessageService.displayErrorMessage(
                    "Fehler",
                    "Beim Update ist ein Fehler aufgetreten!\n\n" +
                    applicationFeatureController.getLastException().getMessage()
                );
                ((ConfigurableApplicationContext) applicationContext).close();
                System.exit(1);
            }
            else if (result == 1) {
                swingMessageService.displayWarningMessage(
                    "Achtung",
                    targetAppName+ " war bereits auf dem neusten Stand! Es wurden allerdings fehlende Dateien" +
                    " festgestellt und behoben!"
                );
                windowFrame.getOngoingProcessScreen().setVisible(false);
                windowFrame.getRunApplicationScreen().setVisible(true);
            }
            else {
                swingMessageService.displayInfoMessage(
                    "Erfolg",
                    "Alle Updates wurden heruntergeladen. " +targetAppName+ " kann jetzt verwendet werden."
                );
                windowFrame.getOngoingProcessScreen().setVisible(false);
                windowFrame.getRunApplicationScreen().setVisible(true);
            }
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
            applicationFeatureController.runTargetApplication();
            ((ConfigurableApplicationContext) applicationContext).close();
            System.exit(0);
        });
        t.start();
    }

    private void onChangeLogShown() {
        LOG.info("DEBUG: Changelog is shown.");
        windowFrame.getRunApplicationScreen().setVisible(false);
        windowFrame.getOngoingProcessScreen().setVisible(true);
        Thread t = new Thread(() -> {
            String content = applicationFeatureController.getLocalChangelog();
            windowFrame.getJustShowChangeLogScreen().getChangeLog().setText(content);
            if (StringUtils.isBlank(content)) {
                swingMessageService.displayErrorMessage(
                    "Fehler",
                    "Die Versionshistorie konnte nicht geladen werden!\n\n" +
                    applicationFeatureController.getLastException().getMessage()
                );
            }
            windowFrame.getOngoingProcessScreen().setVisible(false);
            windowFrame.getJustShowChangeLogScreen().setVisible(true);
        });
        t.start();
    }

    private void onBackToStartPage() {
        LOG.info("DEBUG: Returned to start page.");
        windowFrame.getJustShowChangeLogScreen().setVisible(false);
        windowFrame.getRunApplicationScreen().setVisible(true);
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
        if ("RunPage_ShowChangeLog".equals(source.getName())) {
            onChangeLogShown();
        }
        if ("ShowChangeLogPage_Back".equals(source.getName())) {
            onBackToStartPage();
        }
    }

    @Override
    protected void onValueChanged(ListSelectionEvent event, JComponent source) {

    }
}
