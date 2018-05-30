package environment.commands;

import environment.Environment;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


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
            Path resultDirectory = Paths.get(Environment.getCurrentDirectory());
            if (this.args != null) {
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
                output.write(file.getName().getBytes());
                output.flush();
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            try {
                errOutput.write(("ls: " + e.getMessage() + ": No such file or directory\n").getBytes());
                errOutput.flush();
            } catch (IOException e1) {
            }
        }
    }
}
