package OtherTests;

import environment.Environment;
import exceptions.BadQuotesException;
import org.junit.jupiter.api.*;

import parse.Lexer;
import prepair.Interpol;
import prepair.Preparater;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PreparaterAndInrerpolTest {

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
