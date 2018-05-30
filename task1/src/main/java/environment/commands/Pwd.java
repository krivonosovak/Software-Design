package environment.commands;

import environment.Environment;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Класс-обёртка для команды pwd
 */
public class Pwd extends CommandInterface{

    public Pwd () {};

    @Override
    public void eval(PipedOutputStream output, PipedInputStream input, PipedOutputStream errOutput) {
        try {
            String currentDirectory = Environment.getCurrentDirectory();
            output.write((currentDirectory + "\n").getBytes());
            output.flush();
        } catch (IOException e) {
        }
        try {
            output.close();
        } catch (IOException e) {
        }
    }
}
