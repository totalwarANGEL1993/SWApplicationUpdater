package twa.siedelwood.updater.controller.git;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(value = "/application-test.properties")
@ContextConfiguration(
    classes = {
        GitController.class,
    },
    loader = AnnotationConfigContextLoader.class
)
public class GitControllerTest {
    @Autowired
    private GitController gitController;

    @BeforeEach
    public void before() {

    }

    @AfterEach
    public void after() {

    }

    @Test
    public void dummyTest() {
        assertTrue(true);
    }
}
