package Commands;

import environment.Environment;
import exceptions.BadQuotesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LsTest extends TestCommand {

    private File createDirectoryWithFiles(ArrayList<String> filenames) throws IOException {
        File file = File.createTempFile("o93tuhigp", "");
        file.delete();
        file.mkdir();

        for (String subpath : filenames) {
            Path p = Paths.get(file.getAbsolutePath(), subpath);
            Files.createFile(p);
        }
        return file;
    }

    @Test
    void testNoArguments() throws IOException, BadQuotesException, InterruptedException {
        File tmp = createDirectoryWithFiles(new ArrayList<String>(Arrays.asList("foo.txt", "bar", "foobar")));
        Environment.setCurrentDirectory(tmp.getAbsolutePath());

        runManeger("ls ");
        t = m.getPrintThread();
        t.join(mills);

        assertEquals("bar\nfoo.txt\nfoobar\n", outContent.toString());
        outContent.reset();

        tmp.delete();
    }

    @Test
    void testAbsolutePath() throws IOException, BadQuotesException, InterruptedException {
        File tmp = createDirectoryWithFiles(new ArrayList<String>(Arrays.asList("foo.txt", "bar", "foobar")));
        Environment.setCurrentDirectory(System.getProperty("user.home"));

        runManeger("ls " + tmp.getAbsolutePath());
        t = m.getPrintThread();
        t.join(mills);

        assertEquals("bar\nfoo.txt\nfoobar\n", outContent.toString());
        outContent.reset();

        tmp.delete();
    }

    @Test
    void testRelativePath() throws IOException, BadQuotesException, InterruptedException {
        File tmp = createDirectoryWithFiles(new ArrayList<String>(Arrays.asList("foo.txt", "bar", "foobar")));
        Environment.setCurrentDirectory(tmp.getParent());

        runManeger("ls " + tmp.getName());
        t = m.getPrintThread();
        t.join(mills);

        assertEquals("bar\nfoo.txt\nfoobar\n", outContent.toString());
        outContent.reset();

        tmp.delete();
    }
}
