package Commands;

import exceptions.BadQuotesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PwdTest extends TestCommand {

    @DisplayName("test pwd")
    @Test
    void test4() throws BadQuotesException, IOException, InterruptedException {

        runManeger("pwd");
        t = m.getPrintThread();
        t.join(mills);
        assertEquals(System.getProperty("user.dir") + "\n", outContent.toString());

        runManeger("echo 123 | pwd");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals(System.getProperty("user.dir") + "\n", outContent.toString());

        runManeger("pwd arg1 arg 2");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals(System.getProperty("user.dir")+ "\n", outContent.toString());
    }
}
