package twa.siedelwood.updater.controller.git;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;

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
    private static final String TEST_REPO_PATH = "src/test/var/orthus";

    @Autowired
    private GitController gitController;

    @BeforeEach
    public void before() throws Exception {
        FileUtils.deleteDirectory(new File(TEST_REPO_PATH));
    }

    @AfterEach
    public void after() throws Exception {
        FileUtils.deleteDirectory(new File(TEST_REPO_PATH));
    }

    @Test
    public void fullProcessTest() throws Exception {
        try {
            // Clone
            gitController.cloneRepository();
            assertTrue(FileUtils.getFile(TEST_REPO_PATH).exists());
            // Run update check
            // (No assertion because we can not know if this repo is updated)
            // TODO replace with actual test repository
            final boolean current = gitController.isCurrentVersion();
            // Get a file
            ByteArrayOutputStream file = (ByteArrayOutputStream) gitController.getFile("readme.md");
            assertTrue(new String(file.toByteArray(), StandardCharsets.UTF_8).startsWith("# Introduction"));
            // Alter
            FileUtils.write(new File(TEST_REPO_PATH + File.separator + "readme.md"), "Bockwurst", StandardCharsets.UTF_8);
            // Reset
            gitController.rebaseRepository();
            final String content = FileUtils.readFileToString(
                new File(TEST_REPO_PATH + File.separator + "readme.md"),
                StandardCharsets.UTF_8
            );
            assertFalse(content.contains("Bockwurst"));
        }
        catch (Exception e) {
            FileUtils.deleteDirectory(new File(TEST_REPO_PATH));
            throw e;
        }
    }
}
