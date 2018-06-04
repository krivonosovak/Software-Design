package start;

import interpreter.Interpreter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainClass {

    public static void main(String[] args) throws IOException {
    //        System.out.println(System.getProperty("user.dir"));
        Path path = Paths.get(System.getProperty("user.dir") + "/..");
        System.out.println(path.normalize().toString());
//"`"
//        System.out.println(path.normalize());
////        List<String> line = Files.readAllLines(path, StandardCharsets.UTF_8);
////        System.out.println(line);
//        Path path = Paths.get("/Users/rodionovd/Downloads/../Desktop/t.txt");
//        System.out.println(path.normalize());
//        File file = new File("/Users/rodionovd/Downloads/../Desktop/t.txt");
//        File[] filesList = file.listFiles();
//        List<String> fileNames = new ArrayList<>();
//        for (File f : filesList) {
//           if ((f.isDirectory() || f.isFile()) && !f.isHidden()) {
//               fileNames.add(f.getName());
//           }
//        }
//        fileNames.sort(String::compareTo);
//        System.out.println(fileNames);
//        System.out.println(System.getProperty("user.home"));
        Interpreter.run();
    }
}
