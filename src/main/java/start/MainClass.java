package start;

import interpreter.Interpreter;
import perform.ComandManager;
import prepair.Preparater;
import exceptions.BadQuotesException;
import parse.Lexer;


import java.io.*;


public class MainClass {

//    public static void printType(Lexer l) {
//        for(int i = 0; i < l.lst.size(); i++) {
//            System.out.println(l.lst.get(i).getType());
//        }
//    }
//    public static void printStr(Preparater p) {
//        for(int i = 0; i < p.getLst().size(); i++) {
//            System.out.println(p.getLst().get(i));
//        }
//    }

    public static void main(String[] args) throws BadQuotesException, IOException {
//        String str = "c=$y\"echo     \"$x$x1$x | $x $x$y $X| \'$x$x\' | \"echo 123\" | \"sfds\'  123123 \' fsf\"";
//        String str = "cat /Users/vladislavkalinin/start.bash | grep a | wc  ";
//        String str = "echo \"sdfs               adasda\"";
//        String str = "ec\"ho\" 123\" zdgdfg   $x   sdfgsdf\" | ec\"ho 123\" | $y sdfs | \"grep \" zdfsdf sdfsaf | ec$e 44 | \"echo$z\" | echo$z'b' "  ;
//        String str = "echo huy s toboy, zolotaya rybka | wc /Users/vladislavkalinin/start.bash | cat";
//        String str = "cat /Users/vladislavkalinin/start.bash | wc /Users/vladislavkalinin/test.txt | pWd | wc";
//        String str = "ec\"ho\" 42234";
        String str = "echo 12e3 | sdc |";
        Lexer l = new Lexer();
        l.setString(str);
        l.parse();
//        printType(l);
        Preparater p =new Preparater();
        p.setBigTokens(l.getBigTokens());
        p.prepair();
//        printStr(p);
//
//        ComandManager manager = new ComandManager();
//        manager.setChain(p.getLst());
//        manager.manage();


        Interpreter.run();

    }
}
