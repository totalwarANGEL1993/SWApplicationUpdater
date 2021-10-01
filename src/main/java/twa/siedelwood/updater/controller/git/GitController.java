package twa.siedelwood.updater.controller.git;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Controller für Clone und Rebase des referenzierten Repository.
 */
@Component
public class GitController {
    private static final Logger LOG = LoggerFactory.getLogger(GitController.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${target.repository}")
    private String targetRepositoryUrl;
    @Value("${target.branch}")
    private String targetBranch;
    @Value("${target.directory}")
    private String targetDirectory;

    @Getter
    @Setter
    private volatile Status status = Status.READY;

    public void resetStatus() {
        status = Status.READY;
    }

    public void cloneRepository() throws GitException {
        try
        {
            status = Status.CLONING;
            Git.cloneRepository()
               .setURI(targetRepositoryUrl)
               .setDirectory(new File(System.getProperty("user.dir") + File.separator + targetDirectory))
               .setBranchesToClone(Collections.singletonList("refs/heads/" + targetBranch))
               .setBranch("refs/heads/" +targetBranch)
               .call();
            status = Status.READY;
        }
        catch (GitAPIException e)
        {
            status = Status.ERROR;
            throw new GitException("Failed to clone repository!", e);
        }
    }

    public void rebaseRepository() throws GitException {
        try
        {
            status = Status.REBASING;
            final File directory = new File(System.getProperty("user.dir") + File.separator + targetDirectory);
            final Git repository = Git.open(directory);
            repository.reset().setMode(ResetCommand.ResetType.HARD).call();
            repository.pull().setRebase(true).call();
            status = Status.READY;
        }
        catch (IOException e) {
            status = Status.ERROR;
            LOG.error("Failed to open repository!", e);
        }
        catch (GitAPIException e) {
            status = Status.ERROR;
            LOG.error("Failed to pull changes on branch {}!", targetBranch, e);
            throw new GitException("Failed to pull changes on branch " +targetBranch+ "!", e);
        }
    }

    public enum Status {
        READY,
        CLONING,
        REBASING,
        ERROR
    }
}
