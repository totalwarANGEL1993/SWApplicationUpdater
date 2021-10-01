package twa.siedelwood.updater.controller.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Command Line Controller - macht nicht viel, außer die Anwendung zu starten.
 */
@Component
@Order(value=1)
public class CommandLineRunner implements org.springframework.boot.CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(CommandLineRunner.class);

    @Autowired
    private ApplicationInterfaceController applicationInterfaceController;

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Application startet...");
        applicationInterfaceController.start(args);
    }
}