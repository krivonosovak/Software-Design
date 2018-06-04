package environment.commands;

import environment.Environment;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс-обёртка команды ls. Допускает только один параметр.
 */
public class Ls extends CommandInterface {

    public Ls(ArrayList<String> file) {
        this.args = file;
    }

    public Ls() {
        this.args = null;
    }

    @Override
    public void eval(PipedOutputStream output, PipedInputStream input, PipedOutputStream errOutput) {
        try {
            if (args != null && args.size() > 1) {
                output.close();
                errOutput.write(("ls: too many arguments\n").getBytes());
                errOutput.flush();
                return;
            }
            Path resultDirectory = Paths.get(Environment.getCurrentDirectory());
            if (args != null) {
                Path currentDirectory = Paths.get(args.get(0));
                resultDirectory = resultDirectory.resolve(currentDirectory);
                if (currentDirectory.isAbsolute()) {
                    resultDirectory = currentDirectory;
                }
            }

            File file = new File(resultDirectory.toString());

            if (file.isDirectory()) {
                List<String> filesname = new ArrayList<>();
                File[] files = file.listFiles();
                for (File f : files) {
                    if (!f.isHidden())
                        filesname.add(f.getName());
                }
                filesname.sort(String::compareTo);
                for (String f: filesname) {
                    output.write((f + "\n").getBytes());
                    output.flush();
                }
            } else if (file.isFile() && file.exists()) {
                output.write((file.getName() + "\n").getBytes());
                output.flush();
            } else {
                output.close();
                errOutput.write(("ls: " + file.getName() + ": No such file or directory\n").getBytes());
                errOutput.flush();
            }
        } catch (IOException e) {
        }
    }
}
