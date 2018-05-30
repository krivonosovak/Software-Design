package Commands;

import exceptions.BadQuotesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LsTest extends TestCommand {

    @DisplayName("test ls")
    @Test
    void test9 () throws IOException, BadQuotesException, InterruptedException {

        runManeger("ls ");
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("build.gradle\n" +
                "file.txt\n" +
                "gradle\n" +
                "gradlew\n" +
                "gradlew.bat\n" +
                "out\n" +
                "readme.txt\n" +
                "settings.gradle\n" +
                "src\n" +
                "test.txt\n" +
                "test2.txt\n" +
                "test_dir\n", outContent.toString());

        runManeger("ls src" );
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("main\n" +
                "test\n", outContent.toString());

        String  absolutePath = System.getProperty("user.dir") + "/src";
        runManeger("ls " + absolutePath);
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("main\n" +
                "test\n", outContent.toString());
    }

}
