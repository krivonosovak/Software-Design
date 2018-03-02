package start;

import interpreter.Interpreter;
import perform.ComandManager;
import prepair.Preparater;
import exceptions.BadQuotesException;
import parse.Lexer;


import java.io.*;


public class MainClass {


    public static void main(String[] args) throws BadQuotesException, IOException {
        Interpreter.run();

    }
}
