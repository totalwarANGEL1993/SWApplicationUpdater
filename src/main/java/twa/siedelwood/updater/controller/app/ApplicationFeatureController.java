package twa.siedelwood.updater.controller.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import twa.siedelwood.updater.controller.git.GitController;
import twa.siedelwood.updater.controller.git.GitException;

/**
 * Controller zur Steuerung der Prozesse der Anwendung.
 */
@Component
public class ApplicationFeatureController {
    private static final Logger LOG = LoggerFactory.getLogger(CommandLineRunner.class);

    @Value("${target.repository}")
    private String targetRepositoryUrl;
    @Value("${target.branch}")
    private String targetBranch;
    @Value("${target.directory}")
    private String targetDirectory;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private GitController gitController;

    @Setter
    @Getter
    private String[] parameter;
    @Setter
    @Getter
    private Exception lastException;

    public int updateTargetApplication() {
        final String gitDir = System.getProperty("user.dir") + File.separator + targetDirectory + File.separator + ".git";
        if (!new File(gitDir).exists()) {
            return restoreRepository();
        }
        try
        {
            if (gitController.isCurrentVersion()) {
                return 1;
            }
            gitController.rebaseRepository();
        }
        catch (GitException e)
        {
            lastException = e;
            return -1;
        }
        return 0;
    }

    public int restoreRepository() {
        try
        {
            gitController.purgeRepository();
            gitController.cloneRepository();
        }
        catch (GitException e)
        {
            lastException = e;
            return -1;
        }
        return 0;
    }

    public String getChangelog() {
        String content = null;
        try
        {
            final ByteArrayOutputStream data = (ByteArrayOutputStream) gitController.getFile("changelog.txt");
            content = new String(data.toByteArray(), StandardCharsets.UTF_8);
        }
        catch (GitException e)
        {
            lastException = e;
        }
        return content;
    }
}
