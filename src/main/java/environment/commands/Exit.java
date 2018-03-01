package environment.commands;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Класс-обёртка для команды exit
 */
public class Exit extends CommandInterface {
    /**
     * Флаг, отвечающий за то, нужно ли выходить из программы или нет
     * Выход происходит, если пользователь ввёл только команду exit
     */
    boolean flag;

    public Exit (boolean flag) {
        this.flag = flag;
    };

    @Override
    public void eval(PipedOutputStream output, PipedInputStream input, PipedOutputStream errOutput) {
        if (flag == true) {
            System.exit(0);
        }
        try {
            output.close();
        } catch (IOException e) {
        }
    }
}
