package Commands;

import exceptions.BadQuotesException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import parse.Lexer;
import perform.ComandManager;
import prepair.Preparater;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public abstract class TestCommand {
    public int mills = 500;
    public Lexer lex = new Lexer();
    public Preparater p = new Preparater();
    public ComandManager m = new ComandManager();
    public Thread t;

    public final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    public final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(System.out);
        System.setErr(System.err);
    }

    public void runManeger(String input) throws BadQuotesException, IOException {
        lex.setString(input);
        lex.parse();
        p.setBigTokens(lex.getBigTokens());
        p.prepair();
        m.setChain(p.getLst());
        m.manage();
    }
}
