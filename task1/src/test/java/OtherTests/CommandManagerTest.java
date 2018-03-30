package OtherTests;

import Commands.TestCommand;
import environment.Environment;
import exceptions.BadQuotesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandManagerTest extends TestCommand{
    @DisplayName("test result")
    @Test
    void test1() throws BadQuotesException, IOException, InterruptedException {
        Environment.set("x","ho");
        Environment.set("y","Hello, World");
        Environment.set("z","wc");
        Environment.set("a","ep");
        Environment.set("b","echo from b");


        runManeger("echo 123 | cat");
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("123\n", outContent.toString());



        runManeger("$b");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("from b\n", outContent.toString());

        runManeger("ec$x $y");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("Hello, World\n", outContent.toString());

        runManeger("\"gr$a\" \'select d\' test.txt");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("select d.GENERATE_SERIES as date, b.joindate\n" +
                "select d.GENERATE_SERIES as date, (\n", outContent.toString());

        runManeger("e\'cho\' '$x' | cat | cat | cat | $z");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("\t\t1  1  3  \n", outContent.toString());
    }

    @DisplayName("test error")
    @Test
    void test2() throws BadQuotesException, IOException, InterruptedException {
        Environment.set("x","\'echo \'");

        runManeger("ech");
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("ech: command not found\n", outContent.toString());

        runManeger("some_command arg1 arg2");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("some_command: command not found\n", outContent.toString());

        runManeger("caT qwerty.txt");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("cat: qwerty.txt: No such file or directory\n", outContent.toString());

        runManeger("gREp somthing qwerty.txt");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("grep: qwerty.txt: No such file or directory\n", outContent.toString());

        runManeger("$x 4444");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("'echo: command not found\n", outContent.toString());
    }

}
