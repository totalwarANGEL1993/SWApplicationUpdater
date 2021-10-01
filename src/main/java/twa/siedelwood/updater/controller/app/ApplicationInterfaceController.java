package twa.siedelwood.updater.controller.app;

import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import twa.siedelwood.updater.controller.git.GitController;

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

    @Setter
    @Getter
    private String[] parameter;

    /**
     * Startet die grafische Oberfläche der Applikation.
     * @param args
     */
    public void start(String[] args) {
        parameter = args;
        applicationFeatureController.setParameter(args);
        SwingUtilities.invokeLater(() -> {
            // ...
        });
    }

    // Listeners //

    @Override
    protected void onActionPerformed(ActionEvent event, JComponent source) {

    }

    @Override
    protected void onValueChanged(ListSelectionEvent event, JComponent source) {

    }
}
