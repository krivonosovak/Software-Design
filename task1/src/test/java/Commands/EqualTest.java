package Commands;

import exceptions.BadQuotesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EqualTest extends TestCommand{
    @DisplayName("test Equal")
    @Test
    void test6() throws BadQuotesException, IOException, InterruptedException {

        runManeger("x=\"Hello, World\"");
        runManeger("echo $x");
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("Hello, World\n", outContent.toString());

        runManeger("y=");
        runManeger("echo $y");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("\n", outContent.toString());

        runManeger("x=\"$x, I love AU!\"");
        runManeger("echo $x");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("Hello, World, I love AU!\n", outContent.toString());

        runManeger("z=44 | echo 123");
        runManeger("echo $z");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("\n", outContent.toString());


        runManeger("z=5678 | echo 123 | b=\'AU\'");
        runManeger("echo $z $b");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals(" AU\n", outContent.toString());
    }
}
