package twa.siedelwood.updater.controller.app;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
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
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationFeatureController.class);

    @Value("${target.repository}")
    private String targetRepositoryUrl;
    @Value("${target.branch}")
    private String targetBranch;
    @Value("${target.directory}")
    private String targetDirectory;
    @Value("${target.app.jar}")
    private String targetJarName;
    @Value("${target.app.run}")
    private String targetJarMode;
    @Value("${target.app.dir}")
    private String targetDirName;

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
            final Status status = getRepositoryStatus();
            if (status.hasUncommittedChanges()) {
                gitController.resetRepository();
            }
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
        LOG.info("Reading changelog...");
        String content = null;
        try
        {
            final ByteArrayOutputStream data = (ByteArrayOutputStream) gitController.getFile("changelog.txt");
            content = new String(data.toByteArray(), StandardCharsets.UTF_8);
            if (StringUtils.isBlank(content)) {
                LOG.warn("Changelog not found!");
            }
            else {
                LOG.info("Changelog loaded!");
            }
        }
        catch (GitException e)
        {
            LOG.error("Error loading changelog!");
            lastException = e;
        }
        return content;
    }

    public int checkForUpdates() {
        try
        {
            if (gitController.isCurrentVersion()) {
                return 0;
            }
        }
        catch (GitException e)
        {
            lastException = e;
            return -1;
        }
        return 1;
    }

    public Status getRepositoryStatus() throws GitException {
        try {
            return gitController.getRepository().status().call();
        }
        catch (IOException | GitAPIException e) {
            throw new GitException("Unable to read status!", e);
        }
    }

    public void runTargetApplication() {
        try {
            String pwd = System.getProperty("user.dir");
            pwd = pwd.replaceAll(" ", "^ ");
            if (targetJarMode.equals("normal")) {
                String execution = "" +
                    "jre/bin/java -jar -Dfile.encoding=UTF8 " +
                    targetDirectory +
                    File.separator +
                    targetDirName +
                    File.separator +
                    targetJarName;
                execution = execution.replaceAll("\\\\", "/");
                LOG.info("Executing runtime: " +execution);
                Runtime.getRuntime().exec(execution);
            }
            else {
                String path = "" +
                    pwd +
                    File.separator +
                    targetDirectory +
                    File.separator +
                    targetDirName;
                path = path.replaceAll("\\\\", "/");
                path = path.replaceAll(" ", "^ ");
                String execution = "cmd /c /b start & cd " + path + " & " + pwd + "/jre/bin/java -jar -Dfile.encoding=UTF8 " + targetJarName;
                LOG.info("Executing runtime: " +execution);
                Runtime.getRuntime().exec(execution);
            }
        }
        catch (Exception e) {
            LOG.error("Failed to start application!", e);
        }
    }
}
