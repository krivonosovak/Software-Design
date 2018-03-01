import environment.Environment;
import exceptions.BadQuotesException;
import org.junit.jupiter.api.*;

import parse.Lexer;
import parse.tokens.TypeBigToken;
import parse.tokens.TypeSmallToken;
import perform.ComandManager;
import prepair.Interpol;
import prepair.Preparater;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Так как используется многопоточность, то некоторые тесты могут не проходить с первого раза
 */
public class Testing {

    int mills = 500;

    @Nested
    @DisplayName("Lexer")
    public class InnerTest1 {
        Lexer lex = new Lexer();

        ArrayList<String> getValue(String input) throws BadQuotesException {
            lex.setString(input);
            lex.parse();
            ArrayList<String> res = new ArrayList<>();
            for (int i = 0; i < lex.getBigTokens().size(); i++) {
                StringBuilder buf = new StringBuilder();
                for (int j = 0; j < lex.getBigTokens().get(i).getLst().size(); j++) {
                    buf.append(lex.getBigTokens().get(i).getLst().get(j).getValue());
                }
                res.add(buf.toString());
            }
            return res;
        }

        ArrayList<TypeSmallToken> getSmallType(String input) throws BadQuotesException {
            lex.setString(input);
            lex.parse();
            ArrayList<TypeSmallToken> res = new ArrayList<>();
            for (int i = 0; i < lex.getBigTokens().size(); i++) {
                for (int j = 0; j < lex.getBigTokens().get(i).getLst().size(); j++) {
                    res.add(lex.getBigTokens().get(i).getLst().get(j).getType());
                }
            }
            return res;
        }

        ArrayList<TypeBigToken> getBigType(String input) throws BadQuotesException {
            lex.setString(input);
            lex.parse();
            ArrayList<TypeBigToken> res = new ArrayList<>();
            for (int i = 0; i < lex.getBigTokens().size(); i++) {
                res.add(lex.getBigTokens().get(i).getType());
            }
            return res;
        }


        @DisplayName("test token value")
        @Test
        void test() throws BadQuotesException {
            ArrayList<String> val = new ArrayList<>();

            val.add("echo");
            val.add("Hello");
            assertEquals(getValue("echo Hello"), val);

            val.clear();
            val.add("x=44");
            assertEquals(getValue("x=44"), val);

            val.clear();
            val.add("cat");
            val.add("file.txt file2.txt");
            val.add("x");
            val.add("");
            val.add("wc");
            assertEquals(getValue("        cat \"file.txt file2.txt\"   $x            |wc"), val);

            val.clear();
            val.add("x=ech$y");
            val.add("1234");
            val.add("");
            val.add("pwd");
            assertEquals(getValue("x=ec\"h$y\"  \'1234\'|    pwd"), val);
        }

        @DisplayName("test Small token type")
        @Test
        void test2() throws BadQuotesException {
            ArrayList<TypeSmallToken> type = new ArrayList<>();

            type.add(TypeSmallToken.Command);
            type.add(TypeSmallToken.Command);
            assertEquals(getSmallType("echo Hello"), type);

            type.clear();
            type.add(TypeSmallToken.Command);
            type.add(TypeSmallToken.Equal);
            type.add(TypeSmallToken.Command);
            assertEquals(getSmallType("x=44"), type);

            type.clear();
            type.add(TypeSmallToken.Command);
            type.add(TypeSmallToken.DoubleQuotes);
            type.add(TypeSmallToken.Variable);
            type.add(TypeSmallToken.Command);
            assertEquals(getSmallType("        cat \"file.txt file2.txt\"   $x            |wc"),type);

            type.clear();
            type.add(TypeSmallToken.Command);
            type.add(TypeSmallToken.Equal);
            type.add(TypeSmallToken.Command);
            type.add(TypeSmallToken.DoubleQuotes);
            type.add(TypeSmallToken.SingleQuotes);
            type.add(TypeSmallToken.Command);
            assertEquals(getSmallType("x=ec\"h$y\"  \'1234\'|    pwd"),type);
        }

        @DisplayName("test Big token type")
        @Test
        void test3() throws BadQuotesException {
            ArrayList<TypeBigToken> type = new ArrayList<>();

            type.add(TypeBigToken.Word);
            type.add(TypeBigToken.Word);
            assertEquals(getBigType("echo Hello"), type);

            type.clear();
            type.add(TypeBigToken.Equal);
            assertEquals(getBigType("x=44"), type);

            type.clear();
            type.add(TypeBigToken.Word);
            type.add(TypeBigToken.Word);
            type.add(TypeBigToken.Word);
            type.add(TypeBigToken.Pipe);
            type.add(TypeBigToken.Word);
            assertEquals(getBigType("        cat \"file.txt file2.txt\"   $x            |wc"),type);

            type.clear();
            type.add(TypeBigToken.Equal);
            type.add(TypeBigToken.Word);
            type.add(TypeBigToken.Pipe);
            type.add(TypeBigToken.Word);
            assertEquals(getBigType("x=ec\"h$y\"  \'1234\'|    pwd"),type);
        }

    }


    @Nested
    @DisplayName("Preparater and Interpol")
    public class InnerTest2 {
        Lexer lex = new Lexer();
        Preparater p = new Preparater();
        Interpol i = new Interpol();

        ArrayList<String> getString(String input) throws BadQuotesException {
            lex.setString(input);
            lex.parse();
            p.setBigTokens(lex.getBigTokens());
            p.prepair();
            return p.getLst();
        }

        String getValue(String input) {
            i.setString(input);
            i.interpolate();
            return i.getStr();
        }

        @DisplayName("test interpol")
        @Test
        void test1() {
            Environment.set("x","Hello, World");
            Environment.set("y"," value from y");
            Environment.set("y123y"," Its a crazy name!");

            assertEquals(getValue("$x"),"Hello, World");
            assertEquals(getValue("x"),"x");
            assertEquals(getValue("$x$y"),"Hello, World value from y");
            assertEquals(getValue("Hello, it\'s $y"),"Hello, it\'s  value from y");
            assertEquals(getValue("y123y$y123y"),"y123y Its a crazy name!");
        }

        @DisplayName("test prepator")
        @Test
        void test2() throws BadQuotesException {
            ArrayList<String> val = new ArrayList<>();
            Environment.set("x","value from x");
            Environment.set("y","o");


            val.add("echo");
            val.add("$x");
            assertEquals(getString("\"echo\" '$x'"), val);

            val.clear();
            val.add("__equal__");
            val.add("x");
            val.add("44");
            assertEquals(getString("x=44"), val);

            val.clear();
            val.add("cat");
            val.add("file.txt file2.txt");
            val.add("value");
            val.add("from");
            val.add("x");
            val.add("|");
            val.add("wc");
            assertEquals(getString("        cat \"file.txt file2.txt\"   $x            |wc"), val);


            val.clear();
            val.add("__equal__");
            val.add("x");
            val.add("echo");
            val.add("1234");
            val.add("|");
            val.add("pwd");
            assertEquals(getString("x=ec\"h$y\"  \'1234\'|    pwd"), val);
        }
    }

    @Nested
    @DisplayName("Commands")
    public class InnerTest3 {
        Lexer lex = new Lexer();
        Preparater p = new Preparater();
        ComandManager m = new ComandManager();
        Thread t;

        private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

        @BeforeEach
        void setUpStreams() {
            System.setOut(new PrintStream(outContent));
            System.setErr(new PrintStream(errContent));
        }

        @AfterEach
        void restoreStreams() {
            System.setOut(System.out);
            System.setErr(System.err);
        }

        void runManeger(String input) throws BadQuotesException, IOException {
            lex.setString(input);
            lex.parse();
            p.setBigTokens(lex.getBigTokens());
            p.prepair();
            m.setChain(p.getLst());
            m.manage();
        }

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

        }

        @DisplayName("test cat")
        @Test
        void test2() throws BadQuotesException, IOException, InterruptedException {

            runManeger("cat ");
            t = m.getPrintThread();
            t.join(mills);
            assertEquals("", outContent.toString());

            runManeger("cat test2.txt");
            outContent.reset();
            t = m.getPrintThread();
            t.join(mills);
            assertEquals("1\n2 3\n4 5 6\n", outContent.toString());

            runManeger("pwd | cat");
            outContent.reset();
            t = m.getPrintThread();
            t.join(mills);
            assertEquals("/Users/vladislavkalinin/IdeaProjects/SD1\n", outContent.toString());

            runManeger("pwd | cat test2.txt");
            outContent.reset();
            t = m.getPrintThread();
            t.join(mills);
            assertEquals("1\n2 3\n4 5 6\n", outContent.toString());
        }

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
            assertEquals("\t\t33  195  1324  test.txt\n", outContent.toString());

            runManeger("wc test.txt test2.txt");
            outContent.reset();
            t = m.getPrintThread();
            t.join(mills);
            assertEquals("\t\t33  195  1324  test.txt\n" +
                    "\t\t3  6  12  test2.txt\n" +
                    "\t\t36  201  1336  total\n", outContent.toString());

            runManeger("echo 123 | wc");
            outContent.reset();
            t = m.getPrintThread();
            t.join(20);
            assertEquals("\t\t1  1  4  \n", outContent.toString());
        }

        @DisplayName("test pwd")
        @Test
        void test4() throws BadQuotesException, IOException, InterruptedException {

            runManeger("pwd");
            t = m.getPrintThread();
            t.join(mills);
            assertEquals("/Users/vladislavkalinin/IdeaProjects/SD1\n", outContent.toString());

            runManeger("echo 123 | pwd");
            outContent.reset();
            t = m.getPrintThread();
            t.join(mills);
            assertEquals("/Users/vladislavkalinin/IdeaProjects/SD1\n", outContent.toString());

            runManeger("pwd arg1 arg 2");
            outContent.reset();
            t = m.getPrintThread();
            t.join(mills);
            assertEquals("/Users/vladislavkalinin/IdeaProjects/SD1\n", outContent.toString());
        }

        @DisplayName("test other commands")
        @Test
        void test5() throws BadQuotesException, IOException, InterruptedException {

            runManeger("ls test_dir");
            t = m.getPrintThread();
            t.join(mills);
            assertEquals("test.txt\n" +
                    "test2.txt\n", outContent.toString());


            runManeger("echo 123 | grep 1 ");
            outContent.reset();
            t = m.getPrintThread();
            t.join(mills);
            assertEquals("123\n", outContent.toString());
        }

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
        }

        @DisplayName("test exit and empty")
        @Test
        void test7() throws BadQuotesException, IOException, InterruptedException {

            runManeger("");
            t = m.getPrintThread();
            t.join(mills);
            assertEquals("", outContent.toString());

            runManeger("echo 123 | ");
            outContent.reset();
            t = m.getPrintThread();
            t.join(mills);
            assertEquals("", outContent.toString());

            runManeger("echo 123 | exit");
            outContent.reset();
            t = m.getPrintThread();
            t.join(mills);
            assertEquals("", outContent.toString());

        }

    }


    @Nested
    @DisplayName("Command Manager")
    public class InnerTest4 {
        Lexer lex = new Lexer();
        Preparater p = new Preparater();
        ComandManager m = new ComandManager();
        Thread t;

        private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

        @BeforeEach
        void setUpStreams() {
            System.setOut(new PrintStream(outContent));
            System.setErr(new PrintStream(errContent));
        }

        @AfterEach
        void restoreStreams() {
            System.setOut(System.out);
            System.setErr(System.err);
        }

        void runManeger(String input) throws BadQuotesException, IOException {
            lex.setString(input);
            lex.parse();
            p.setBigTokens(lex.getBigTokens());
            p.prepair();
            m.setChain(p.getLst());
            m.manage();
        }

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


}
