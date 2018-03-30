package OtherTests;

import exceptions.BadQuotesException;
import org.junit.jupiter.api.*;

import parse.Lexer;
import parse.tokens.TypeBigToken;
import parse.tokens.TypeSmallToken;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LexerTest {

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

}
