package perform;

import environment.commands.CommandInterface;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Класс, реализующий интерфейс Thread, позволяющий выполнять команды в различных потоках
 */
public class Command extends Thread{

    /**
     * Входной поток команды
     */
    private final PipedInputStream input;
    /**
     * Выходной поток команды
     */
    private final PipedOutputStream output;
    /**
     * Входной поток цепочки ошибок
     */
    private final PipedOutputStream errOutput;
    /**
     * Полиморфный тип команды
     */
    private CommandInterface cmd;

    public Command(CommandInterface cmd, PipedOutputStream output, PipedInputStream input, PipedOutputStream errOutput) {
        this.input = input;
        this.output = output;
        this.errOutput = errOutput;
        this.cmd = cmd;
    }

    /**
     * Запускает выполнение команды
     */
    @Override
    public void run() {
        cmd.eval(output,input, errOutput);
    }

}
