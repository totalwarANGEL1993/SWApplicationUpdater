package twa.siedelwood.updater.controller.app;

import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Controller zur Reaktion auf die Aktionen des Benutzers.
 */
@Component
public class ApplicationInterfaceController extends AbstractInterfaceController {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationInterfaceController.class);

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Startet die grafische Oberfläche der Applikation.
     */
    public void start() {
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
