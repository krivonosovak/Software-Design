package Commands;

import exceptions.BadQuotesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WcTest extends TestCommand{

    @DisplayName("test wc")
    @Test
    void test3() throws BadQuotesException, IOException, InterruptedException {

        runManeger("wc ");
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("", outContent.toString());

        runManeger("wc test.txt");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("\t\t37  205  1365  test.txt\n", outContent.toString());

        runManeger("wc test.txt test2.txt");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("\t\t37  205  1365  test.txt\n" +
                "\t\t3  6  12  test2.txt\n" +
                "\t\t40  211  1377  total\n", outContent.toString());

        runManeger("echo 123 | wc");
        outContent.reset();
        t = m.getPrintThread();
        t.join(20);
        assertEquals("\t\t1  1  4  \n", outContent.toString());
    }

}
