package Commands;

import exceptions.BadQuotesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EchoTest extends TestCommand{

    @DisplayName("test echo")
    @Test
    void test1() throws BadQuotesException, IOException, InterruptedException {

        runManeger("echo 123");
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("123\n", outContent.toString());

        runManeger("echo ");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("", outContent.toString());

        runManeger("pwd | echo");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("", outContent.toString());

        runManeger("echo \"Hello \n World \n !\"");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("Hello \n" +
                " World \n" +
                " !\n", outContent.toString());
    }
}
