package environment.commands;

import environment.Environment;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс-обёртка команды cd
 */
public class Cd extends CommandInterface {

    public Cd(ArrayList<String> file) {
        this.args = file;
    }

    public Cd() {
        this.args = null;
    }

    @Override
    public void eval(PipedOutputStream output, PipedInputStream input, PipedOutputStream errOutput) {
        if (args == null) {
            Environment.setCurrentDirectory(System.getProperty("user.home"));
        } else {

            Path currentDirectory = Paths.get(Environment.getCurrentDirectory());
            Path currentFilePath = Paths.get(args.get(0));
            Path newCurDirectory;

            if (currentFilePath.isAbsolute()) {
                newCurDirectory = currentFilePath;

            } else {
                newCurDirectory = currentDirectory.resolve(currentFilePath).normalize();
            }
            try {
                if (Files.exists(newCurDirectory) && Files.isDirectory(newCurDirectory)) {
                    Environment.setCurrentDirectory(newCurDirectory.toString());
                } else {
                    throw new IOException(currentDirectory.toString());
                }
            } catch (IOException e) {
                try {
                    errOutput.write(("cd: " + e.getMessage() + ": No such file or directory\n").getBytes());
                    errOutput.flush();
                } catch (IOException e1) {
                }
            }
        }
    }
}
