package environment.commands;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * Класс-обёртка команды cat
 */
public class Cat extends CommandInterface{
    public Cat(ArrayList<String> file) {
        this.args = file;
    }

    public Cat() {
        this.args = null;
    }

    @Override
    public void eval(PipedOutputStream output, PipedInputStream input, PipedOutputStream errOutput) {
        fork(output, input, errOutput);
    }

    @Override
    protected void fromStream(PipedOutputStream output, PipedInputStream input) {
        try {
            int data = input.read();
            while (data != -1) {
                output.write((char) data);
                data = input.read();
            }
            output.flush();
        } catch (IOException e) {
        }
    }

    @Override
    protected void fromFile(PipedOutputStream output, PipedOutputStream errOutput) {
        for (int i = 0; i < args.size(); i++) {
            List<String> lines = null;
            try {
                lines = Files.readAllLines(Paths.get(args.get(i)), StandardCharsets.UTF_8);
                for (String line : lines) {
                    output.write((line + "\n").getBytes());
                    output.flush();
                }
            } catch (IOException e) {
                try {
                    errOutput.write(("cat: " + e.getMessage() + ": No such file or directory\n").getBytes());
                    errOutput.flush();
                } catch (IOException e1) {
                }
            }
        }
    }
}
