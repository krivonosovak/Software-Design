package Commands;

import exceptions.BadQuotesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CdTest extends TestCommand {

    @DisplayName("test cd")
    @Test
    void test8 () throws IOException, BadQuotesException, InterruptedException {

        runManeger("cd src/main");
        t = m.getPrintThread();
        t.join(mills);
        runManeger("pwd ");
        t = m.getPrintThread();
        t.join(mills);
        Path path = Paths.get(System.getProperty("user.dir") + "/src/main");
        assertEquals(path.normalize().toString() + "\n", outContent.toString());

        runManeger("cd .. ");
        t = m.getPrintThread();
        t.join(mills);
        runManeger("pwd ");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        path = Paths.get(System.getProperty("user.dir") + "/src" );
        assertEquals(path.normalize().toString() + "\n", outContent.toString());

        runManeger("cd ");
        t = m.getPrintThread();
        t.join(mills);
        runManeger("pwd ");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals(System.getProperty("user.home")+ "\n", outContent.toString());

        path = Paths.get(System.getProperty("user.dir") + "/src/test");
        runManeger("cd " + path.toString());
        t = m.getPrintThread();
        t.join(mills);
        runManeger("pwd ");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals(path.normalize().toString() + "\n", outContent.toString());
    }

}
