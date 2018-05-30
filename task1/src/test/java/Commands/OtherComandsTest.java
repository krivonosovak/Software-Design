package Commands;

import exceptions.BadQuotesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OtherComandsTest extends TestCommand {

    @DisplayName("test other commands")
    @Test
    void test5() throws BadQuotesException, IOException, InterruptedException {

        runManeger("ls test_dir");
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("test.txt\n" +
                "test2.txt\n", outContent.toString());


        runManeger("echo 123 | ls test_dir");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("test.txt\n" +
                "test2.txt\n", outContent.toString());


    }
}
