package environment.commands;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс-обёртка для комманды grep
 */
public class Grep extends CommandInterface{

    /**
     * Поля, необходимые для парсинга аргументов
     */
    private final static Options posixOptions;
    private final static HelpFormatter helpFormatter;
    private final static CommandLineParser cmdLinePosixParser;

    /**
     * Аргументы grep без ключей
     */
    private String[] grepArgs;

    /**
     * Поля, реализующие состояния grep для ключей i, A, w соответсвеннно
     */
    private UnaryOperator<String> ignoreCase;
    private int lineCount = 0;
    private final StringBuilder errBuilder = new StringBuilder();

    static {
        Option optionI = new Option("i", "ignore-case", false, "Perform case insensitive matching. " +
                "By default, grep is case sensitive.");;
        Option optionW = new Option("w", "word-regexp", false, "The expression is searched for as " +
                "a word (as if surrounded by `[[:<:]]' and `[[:>:]]'; see re_format(7)).");;
        Option optionA = new Option("A", "after-context", true, "Print num lines of trailing context " +
                "after each match. See also the -B and -C options.");;
        optionA.setArgs(1);
        optionA.setArgName("num");
        posixOptions = new Options();
        posixOptions.addOption(optionI);
        posixOptions.addOption(optionW);
        posixOptions.addOption(optionA);
        helpFormatter = new HelpFormatter();
        cmdLinePosixParser = new PosixParser();
    }

    public Grep(ArrayList<String> file) {
        lineCount = 0;
        ignoreCase = x -> x;
        this.args = file;
    }

    /**
     * Препроцессинг, инициализурующий функцианальные поля grep для различных ключей
     * @throws ParseException
     * @throws NumberFormatException
     */
    private void preprocessing() throws ParseException, NumberFormatException {
        String[] arrArgs = new String[args.size()];
        args.toArray(arrArgs);

        CommandLine commandLine = null;
        try {
            commandLine = cmdLinePosixParser.parse(posixOptions, arrArgs);
        } catch (ParseException e) {
            throw new ParseException("Parse error");
        }
        grepArgs = commandLine.getArgs();

        if (commandLine.hasOption("i")) {
            grepArgs[0] = grepArgs[0].toLowerCase();
            ignoreCase = String::toLowerCase;
        }
        if (commandLine.hasOption("w")) {
            grepArgs[0] = "(\\W|^)" + grepArgs[0] + "(\\W|$)";
        }
        if (commandLine.hasOption("A")) {
            if (StringUtils.isNumeric(commandLine.getOptionValues("A")[0])) {
                lineCount = Integer.parseInt(commandLine.getOptionValues("A")[0]);
            } else {
                throw new NumberFormatException();
            }
        }
    }

    @Override
    public void eval(PipedOutputStream output, PipedInputStream input, PipedOutputStream errOutput) {
        if (args.size() != 0) {
            try {
                preprocessing();
                evalGrep(grepArgs, output, input, errOutput);
            } catch (ParseException e) {
                printHelp(errOutput);
            } catch (NumberFormatException e) {
                errBuilder.append("grep: Invalid argument\n");
            }
        } else {
            printHelp(errOutput);
        }
        try {
            errOutput.write(errBuilder.toString().getBytes());
            errOutput.flush();
            output.close();
        } catch (IOException e) {
        }

    }

    private void evalGrep (String[] args, PipedOutputStream output, PipedInputStream input, PipedOutputStream errOutput) {
        if (args.length > 1) {
            fromFile(output, errOutput);
        } else {
            fromStream(output,input, errOutput);
        }
    }

    @Override
    protected void fromStream (PipedOutputStream output, PipedInputStream input, PipedOutputStream errOutput) {
        String pattern = grepArgs[0];
        Pattern r = Pattern.compile(pattern);
        Matcher m = null;
        String prefix = "";
        int data = 0;
        try {
            data = input.read();
            StringBuilder buf = new StringBuilder();
            while (data != -1) {
                buf.append((char) data);
                data = input.read();
            }
            input.close();
            List<String> lines = new ArrayList(Arrays.asList(buf.toString().split("\n")));
            fillStream(lines,prefix,output,m,r);
        } catch (IOException e) {
        }
    }

    @Override
    protected  void fromFile (PipedOutputStream output, PipedOutputStream errOutput)  {
        String pattern = grepArgs[0];
        Pattern r = Pattern.compile(pattern);
        Matcher m = null;
        String prefix = "";
        for (int i = 1; i < grepArgs.length; i++) {
            if (grepArgs.length > 2) {
                prefix = grepArgs[i] + ":";
            }
            List<String> lines = null;
            try {
                lines = Files.readAllLines(Paths.get(grepArgs[i]), StandardCharsets.UTF_8);
                fillStream(lines,prefix,output,m,r);
            } catch (IOException e) {
                errBuilder.append("grep: " + e.getMessage() + ": No such file or directory\n");
            }
        }
    }

    /**
     * Заполнение выходного потока grep
     * @param lines - текущая строка
     * @param prefix - вспомогательный аргумент для красивого вывода
     * @param output - поток вывода
     * @param m - матчер регулярных выражений
     * @param r - паттерн регулярного выражения
     * @throws IOException
     */
    private void fillStream (List<String> lines, String prefix, PipedOutputStream output,
                             Matcher m, Pattern r) throws IOException {
        int counter = lineCount + 1;
        for (String line : lines) {
            m = r.matcher(ignoreCase.apply(line));
            if (m.find()) {
                counter = 0;
                output.write((prefix + line + "\n").getBytes());
            } else if (counter <= lineCount) {
                output.write((prefix + line + "\n").getBytes());
            }
            counter ++;
            output.flush();
        }
    }

    /**
     * Красивый вывод ошибок
     * @param errOutput
     */
    private void printHelp (PipedOutputStream errOutput) {
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        helpFormatter.printHelp(writer,80,"grep",null,posixOptions,1,4,null,true);
        writer.flush();
        errBuilder.append(out.toString());
    }

}
