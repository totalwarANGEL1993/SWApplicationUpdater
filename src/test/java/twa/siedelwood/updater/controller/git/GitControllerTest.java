package twa.siedelwood.updater.controller.git;

import ch.qos.logback.core.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.assertj.core.util.Arrays;
import org.eclipse.jgit.api.Git;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(value = "/application-test.properties")
@ContextConfiguration(
    classes = {
        GitController.class,
    },
    loader = AnnotationConfigContextLoader.class
)
@EnableAutoConfiguration
public class GitControllerTest {
    @Autowired
    private GitController gitController;

    @BeforeEach
    public void before() {}

    @AfterEach
    public void after() throws IOException {
        FileUtils.deleteDirectory(new File("var/orthus"));
    }

    @Test
    public void fullProcessTest() throws Exception {
        // Clone
        gitController.cloneRepository();
        assertTrue(FileUtils.getFile("var/orthus").exists());
        // Alter
        FileUtils.write(new File("var/orthus/readme.md"), "Bockwurst", StandardCharsets.UTF_8);
        // Reset
        gitController.rebaseRepository();
        final String content = FileUtils.readFileToString(
            new File("var/orthus/readme.md"),
            StandardCharsets.UTF_8
        );
        assertFalse(content.contains("Bockwurst"));
    }

    @Test
    public void follProcessForceTest() throws Exception {
        // Clone
        gitController.cloneRepository();
        assertTrue(FileUtils.getFile("var/orthus").exists());
        // Alter
        FileUtils.write(new File("var/orthus/readme.md"), "Bockwurst", StandardCharsets.UTF_8);
        // Reset the hard way ;)
        gitController.rebaseRepositoryForceRemoteBranch();
        final String content = FileUtils.readFileToString(
            new File("var/orthus/readme.md"),
            StandardCharsets.UTF_8
        );
        assertFalse(content.contains("Bockwurst"));
    }
}
