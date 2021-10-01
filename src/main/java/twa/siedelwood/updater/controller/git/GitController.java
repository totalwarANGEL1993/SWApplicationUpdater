package twa.siedelwood.updater.controller.git;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

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
    private volatile Status status = Status.WAIT;

    /**
     * Resets the controller status.
     */
    public void resetStatus() {
        status = Status.WAIT;
    }

    /**
     * Returns a new Git instance with the repository loaded.
     * @return Git instance with repository
     * @throws IOException
     */
    public Git getRepository() throws IOException {
        final File directory = new File(System.getProperty("user.dir") + File.separator + targetDirectory);
        return Git.open(directory);
    }

    /**
     * Loads the latest version of the specified file.
     * @param path Path of file in project
     * @return File content
     * @throws GitException File not found
     */
    public OutputStream getFile(final String path) throws GitException {
        OutputStream os = new ByteArrayOutputStream();
        try (Git git = getRepository())
        {
            final ObjectId lastCommitId = git.getRepository().resolve(Constants.HEAD);

            try (RevWalk revWalk = new RevWalk(git.getRepository()))
            {
                RevCommit commit = revWalk.parseCommit(lastCommitId);
                RevTree tree = commit.getTree();
                try (TreeWalk treeWalk = new TreeWalk(git.getRepository()))
                {
                    treeWalk.addTree(tree);
                    treeWalk.setRecursive(true);
                    treeWalk.setFilter(PathFilter.create(path));
                    if (!treeWalk.next())
                    {
                        throw new GitException("Did not find expected file '" +path+ "'!");
                    }
                    ObjectId objectId = treeWalk.getObjectId(0);
                    ObjectLoader loader = git.getRepository().open(objectId);

                    loader.copyTo(os);
                }
                revWalk.dispose();
            }
        }
        catch (Exception e)
        {
            status = Status.ERROR;
            throw new GitException("Failed to obtain file!", e);
        }
        return os;
    }

    /**
     * Clones the repository from the remote.
     * @throws GitException Cloning has failed
     */
    public void cloneRepository() throws GitException {
        try
        {
            status = Status.CLONE;
            Git.cloneRepository()
                .setURI(targetRepositoryUrl)
                .setDirectory(new File(System.getProperty("user.dir") + File.separator + targetDirectory))
                .setBranchesToClone(Collections.singletonList("refs/heads/" + targetBranch))
                .setBranch("refs/heads/" +targetBranch)
                .call()
                .close();
            status = Status.WAIT;
        }
        catch (GitAPIException e)
        {
            status = Status.ERROR;
            throw new GitException("Failed to clone repository!", e);
        }
    }

    /**
     * Checks if the currently checked out version is the current version.
     * @return Local version is current version
     * @throws GitException Checking changes failed
     */
    public boolean isCurrentVersion() throws GitException {
        try (Git git = getRepository())
        {
            status = Status.FETCH;
            FetchResult result = git
                .fetch()
                .setRefSpecs(new RefSpec("refs/heads/master"))
                .call();
            git.close();
            status = Status.WAIT;
            return result.getTrackingRefUpdates().isEmpty();
        }
        catch (IOException | GitAPIException e)
        {
            status = Status.ERROR;
            LOG.error("Failed to fetch changes on branch {}!", targetBranch, e);
            throw new GitException("Failed to fetch changes on branch " +targetBranch+ "!", e);
        }
    }

    /**
     * Rebases the directory to the tip of tbe branch.
     * @throws GitException Rebasing has failed
     */
    public void rebaseRepository() throws GitException {
        try (Git git = getRepository())
        {
            status = Status.REBASE;
            git.reset().setMode(ResetCommand.ResetType.HARD).call();
            git.pull().setRebase(true).call();
            git.close();
            status = Status.WAIT;
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

    /**
     * Deletes the directory the repository is in.
     * @throws GitException Deletion has failed
     */
    public void purgeRepository() throws GitException {
        try
        {
            status = Status.PURGE;
            final File directory = new File(System.getProperty("user.dir") + File.separator + targetDirectory);
            FileUtils.deleteDirectory(directory);
            status = Status.WAIT;
        }
        catch (Exception e) {
            status = Status.ERROR;
            LOG.error("Failed to purge repository!", e);
            throw new GitException("\"Failed to purge repository!", e);
        }
    }

    /**
     * Status of repository interactions.
     */
    public enum Status {
        WAIT,
        FETCH,
        CLONE,
        REBASE,
        PURGE,
        ERROR
    }
}
