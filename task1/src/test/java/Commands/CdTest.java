package Commands;

import environment.Environment;
import exceptions.BadQuotesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CdTest extends TestCommand {

    @Test
    void testRelativePath() throws IOException, BadQuotesException, InterruptedException {
        Environment.setCurrentDirectory(System.getProperty("user.dir"));

        runManeger("cd src/main");
        t = m.getPrintThread();
        t.join(mills);

        Path path = Paths.get(System.getProperty("user.dir"), "src", "main");
        assertEquals(path.normalize().toString(), Environment.getCurrentDirectory());
    }

    @Test
    void testAbsolutePath() throws IOException, BadQuotesException, InterruptedException {
        Environment.setCurrentDirectory(System.getProperty("user.dir"));
        Path expectedPath = Paths.get(Environment.getCurrentDirectory(), "src", "main");

        runManeger("cd " + expectedPath.normalize().toString());
        t = m.getPrintThread();
        t.join(mills);

        assertEquals(expectedPath.normalize().toString(), Environment.getCurrentDirectory());
    }

    @Test
    void testParent() throws IOException, BadQuotesException, InterruptedException {
        Environment.setCurrentDirectory(System.getProperty("user.dir"));

        runManeger("cd ..");
        t = m.getPrintThread();
        t.join(mills);

        Path path = Paths.get(System.getProperty("user.dir"), "..");
        assertEquals(path.normalize().toString(), Environment.getCurrentDirectory());
    }

    @Test
    void testDefaultsToHome() throws IOException, BadQuotesException, InterruptedException {
        Environment.setCurrentDirectory(System.getProperty("user.dir"));

        runManeger("cd ");
        t = m.getPrintThread();
        t.join(mills);

        Path path = Paths.get(System.getProperty("user.home"));
        assertEquals(path.normalize().toString(), Environment.getCurrentDirectory());
    }
}
