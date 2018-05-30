package environment.commands;

import environment.Environment;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Класс-обёртка команды присваивания
 */
public class Equal extends CommandInterface{
    /**
     * Имя переменной
     */
    String variable;
    /**
     * Присваиваемое значение
     */
    String value;
    /**
     * Флаг, отвечающий за то, должно ли проходить присваивания
     * Нужен для имитации присваивания в bash
     */
    boolean flag;


    public Equal(String variable, String value, boolean flag) {
        this.variable = variable;
        this.value = value;
        this.flag = flag;
    }


    @Override
    public void eval(PipedOutputStream output, PipedInputStream input, PipedOutputStream errOutput) {
        if (flag == true) {
            Environment.set(variable,value);
        }
        try {
            output.close();
        } catch (IOException e) {
        }
    }
}
