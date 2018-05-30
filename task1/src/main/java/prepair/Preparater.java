package prepair;

import environment.Environment;
import parse.tokens.BigToken;
import parse.tokens.TypeBigToken;
import parse.tokens.TypeSmallToken;

import java.util.ArrayList;


/**
 * Состовляет из списка слов (больших токенов) итоговый список последовательных команд, который будет поступать на вход
 * исполнителя (командного менеджера)
 */
public class Preparater {
    /**
     * Внутрений буффер
     */
    private ArrayList<BigToken> btLst;
    /**
     * Результирующий список
     */
    private ArrayList<String> lst;

    /**
     * Кладёт лист токенов в буффер
     * @param btLst лист токенов, который должен быть получен от лексера
     */
    public void setBigTokens(ArrayList<BigToken> btLst) {
        this.btLst = btLst;
        lst = new ArrayList<>();
    }

    /**
     * Состовляет итоговый список команд в ввиде, который требует командный менеджер (исполнитель)
     * Результат кладёт в внутренний буффер
     */
    public void prepair() {
        for(BigToken bt : btLst) {
            splitWord(bt);
        }
    }


    /**
     * Разбивает слова на команды и аргументы
     * @param bt
     */
    private void splitWord(BigToken bt) {
        if(bt.getType() == TypeBigToken.Pipe) {
            if(lst.get(lst.size()-1).equals("|")) {
                lst.add("");
            }
            lst.add("|");
            return;
        }
        StringBuilder buf = new StringBuilder();
        Interpol interpol = new Interpol();
        int ind = 0;

        // Проверяем, не содержит ли слово валидное приравнивнивание

        if (bt.getType() == TypeBigToken.Equal) {
            if (bt.getLst().size() <2 || bt.getLst().get(0).getType() != TypeSmallToken.Command ||
                    bt.getLst().get(1).getType() != TypeSmallToken.Equal) {
                bt.setType(TypeBigToken.Word);
            } else if (!isValidVariable(bt.getLst().get(0).getValue())) {
                bt.setType(TypeBigToken.Word);
            } else {
                lst.add("__equal__");
                lst.add(bt.getLst().get(0).getValue());
                ind = 2;
            }
        }

        // Разбиваем слова, в зависимости от small tokens

        while (ind < bt.getLst().size()) {
            switch (bt.getLst().get(ind).getType()) {
                case Command:
                    buf.append(bt.getLst().get(ind).getValue());
                    break;
                case Variable:
                    if(!isValidVariable(bt.getLst().get(ind).getValue())) {
                    }
                    if(bt.getType() == TypeBigToken.Word) {
                        splitVariable(Environment.get(bt.getLst().get(ind).getValue()), buf);
                    } else {
                        buf.append(Environment.get(bt.getLst().get(ind).getValue()));
                    }
                    break;
                case SingleQuotes:
                    buf.append(bt.getLst().get(ind).getValue());
                    break;
                case DoubleQuotes:

                    // Интерполируем строку с двойными кавычками и записываем результат в буффер

                    interpol.setString(bt.getLst().get(ind).getValue());
                    interpol.interpolate();
                    buf.append(interpol.getStr());
                    break;
                case Equal:
                    buf.append(bt.getLst().get(ind).getValue());
            }
            ind++;
        }

        lst.add(buf.toString());
    }


    /**
     * Сплитит содержание переменных
     * @param value - значение переменной
     * @param buf - вспомогательный буффер
     */
    private void splitVariable (String value, StringBuilder buf) {
        for(int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == ' ') {
                lst.add(buf.toString());
                buf.setLength(0);
                while (i < value.length()-1 && value.charAt(i+1) == ' ') {
                    i++;
                }
            } else {
                buf.append(value.charAt(i));
            }
        }
    }

    /**
     * Проверяет имя переменной на валидность
     * @param var - имя переменной
     * @return - true, если переменная валидна и false в противном случае
     */
    private boolean isValidVariable(String var) {
        if (var.length() == 0 || !Character.isLetter(var.charAt(0)))
        {
            return false;
        }
        for(int i = 1; i < var.length(); i++) {
            if(!Character.isLetter(var.charAt(i)) && !Character.isDigit(var.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<String> getLst() {
        return lst;
    }


}

