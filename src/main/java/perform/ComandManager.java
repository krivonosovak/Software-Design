package perform;

import environment.commands.*;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Класс, контролирующий выполнение цепочки команд
 */
public class ComandManager {
    /**
     * Внутренний буффер последовательности команд и аргументов
     */
    ArrayList<String> chain;
    int ind;

    /**
     * Кладёт во внутренний буффер последовательность команд и аргументов
     * @param chain - правильный список команд
     */
    public void setChain(ArrayList<String> chain) {
        this.chain = chain;
        ind = 0;
    }

    private Thread printThread = null;


    /**
     * Запускает и контролирует выполнение команд из внутреннего буффера
     * Выделяет пул потоков, связывает их с помощью PipedStream и запускает команды в этом пуле
     * @throws IOException
     */
    public void manage() throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        PipedInputStream oldInput  = new PipedInputStream();
        final PipedOutputStream errOutput = new PipedOutputStream();
        final PipedInputStream errInput = new PipedInputStream(errOutput);

        while (ind < chain.size()) {
            final PipedOutputStream output = new PipedOutputStream();
            final PipedInputStream input  = new PipedInputStream(output);


            int comInd = ind;
            ind++;

            CommandInterface cmd = getCommand(comInd, getArgs());
            Command command = new Command(cmd,output,oldInput,errOutput);


            executor.execute(command);

            oldInput = input;
        }

        executor.shutdown();

        // Отдельный поток для вывода в консоль результатов цепочек вычислений и ошибок

        final PipedInputStream finalOldInput = oldInput;

        printThread = new Thread(new Runnable() {
            @Override
            public void run() {
                print(finalOldInput);
                print(errInput);
            }
        });
        printThread.start();
    }


    /**
     * Вывод из потока в консоль
     * @param input - поток для считывания
     */
    private void print(PipedInputStream input) {
        try {
            int data = input.read();
            while (data != -1) {
                System.out.print((char) data);
                data = input.read();
            }
        } catch (IOException e) {
        }
    }

    /**
     * Находит в буфферной цепочки аргументы для отдельной команды
     * @return
     */
    private ArrayList<String> getArgs() {
        ArrayList<String> res = new ArrayList<>();
        while (ind < chain.size() && !chain.get(ind).equals("|")) {
            res.add(chain.get(ind));
            ind++;
        }
        ind++;
        return res;
    }


    /**
     * Распознование команд
     * @param comInd - индекс по которому находится имя команды в цепочке
     * @param args - аргументы команды
     * @return - сконтструированый тип команды
     */
    private CommandInterface getCommand (int comInd, ArrayList<String> args) {
        CommandInterface cmd = null;

        switch (chain.get(comInd).toLowerCase()) {
            case "echo":
                cmd = new Echo(args);
                break;
            case "cat":
                if(args.size() > 0) {
                    cmd = new Cat(args);
                } else {
                    cmd = new Cat();
                }
                break;
            case "wc":
                if(args.size() > 0) {
                    cmd = new Wc(args);
                } else {
                    cmd = new Wc();
                }
                break;
            case "pwd":
                cmd = new Pwd();
                break;
            case "exit":
                boolean flag1 = (chain.size() == 1);
                cmd = new Exit(flag1);
                break;
            case "__equal__":
                boolean flag2 = (chain.size() == 3);
                cmd = new Equal(chain.get(1),chain.get(2),flag2);
                break;
            case "":
                cmd = new Empty();
                break;
            default:
                if (args.size() > 0) {
                    cmd = new OtherCommands(chain.get(comInd).toLowerCase(),args);
                } else {
                    cmd = new OtherCommands(chain.get(comInd).toLowerCase());
                }
        }

        return cmd;
    }

    /**
     * Метод для тестов
     * @return поток вывода
     */

    public Thread getPrintThread() {
        return printThread;
    }

}
