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
        try {
            output.close();
            if (args == null) {
                Environment.setCurrentDirectory(System.getProperty("user.home"));
            } else if (args.size() == 1) {
                Path currentDirectory = Paths.get(Environment.getCurrentDirectory());
                Path currentFilePath = Paths.get(args.get(0));
                Path newCurDirectory = null;
                if (currentFilePath.isAbsolute()) {
                    newCurDirectory = currentFilePath;
                } else {
                    newCurDirectory = currentDirectory.resolve(currentFilePath).normalize();
                }
                if (Files.exists(newCurDirectory) && Files.isDirectory(newCurDirectory)) {
                    Environment.setCurrentDirectory(newCurDirectory.toString());
                } else {
                    errOutput.write(("cd: " + newCurDirectory.toString() +": No such directory\n").getBytes());
                    errOutput.flush();
                }
            } else {
                errOutput.write(("cd: too many arguments\n").getBytes());
                errOutput.flush();
            }
        } catch (IOException e) {
        }
    }
}
