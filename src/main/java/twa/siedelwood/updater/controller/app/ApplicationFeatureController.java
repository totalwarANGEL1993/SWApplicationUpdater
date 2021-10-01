package twa.siedelwood.updater.controller.app;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Controller zur Steuerung der Prozesse der Anwendung.
 */
@Component
public class ApplicationFeatureController {
    private static final Logger LOG = LoggerFactory.getLogger(CommandLineRunner.class);

    @Setter
    @Getter
    private Exception lastException;
}
