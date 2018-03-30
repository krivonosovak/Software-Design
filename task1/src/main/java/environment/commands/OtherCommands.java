package environment.commands;

import java.io.*;
import java.util.ArrayList;

/**
 * Класс-обёртка для вспомогательной команды otherCommand
 * Запускает команду из системы, если такая отсутсвует в данном окружение
 */
public class OtherCommands extends CommandInterface {
    /**
     * Название команды
     */
    private String cmd;

    public OtherCommands(String cmd, ArrayList<String> args) {
        this.cmd = cmd;
        this.args = args;
    }

    public OtherCommands(String cmd) {
        this.cmd = cmd;
        this.args = new ArrayList<>();
    }

    @Override
    public void eval(PipedOutputStream output, PipedInputStream input, PipedOutputStream errOutput) {


        String[] command = new String[args.size()+1];
        command[0] = cmd;
        for (int i = 0; i < args.size(); i++) {
            command[i + 1] = args.get(i);
        }


        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);

        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            try {
                errOutput.write((command[0] + ": command not found\n").getBytes());
                errOutput.flush();
                output.close();
            } catch (IOException e1) {
            }
            return;
        }
        OutputStream stdin = process.getOutputStream ();
        InputStream stdout = process.getInputStream ();
        BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

        pipeFromStream(input,writer);

        try {
            int data = reader.read();
            while (data != -1) {
                output.write(data);
                output.flush();
                data = reader.read();
            }
            output.close();
        } catch (IOException e) {
        }


    }

    /**
     * Записывает предаваемый поток в stdin буффер запускаемой из системы команды
     * @param input - Входной поток
     * @param writer - stdin буффер запускаемой команды
     */
    private void pipeFromStream(PipedInputStream input, BufferedWriter writer) {
        try {
            int data = input.read();
            while (data != -1) {
                writer.write((char) data);
                writer.flush();
                data = input.read();
            }
            input.close();
            writer.close();
        } catch (IOException e) {
        }
    }



}
