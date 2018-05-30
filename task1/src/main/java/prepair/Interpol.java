package prepair;

import environment.Environment;


/**
 * Класс для интерполяции строк
 */
public class Interpol {
    /**
     * Внутренний буффер, в который записывается результат
     */
    private String str;
    private int ind;

    public Interpol() {
        this.str = "";
        ind = 0;
    }

    /**
     * Кладёт в буффер строку для интерполяции
     * @param str
     */
    public void setString(String str) {
        this.str = str;
        ind = 0;
    }

    /**
     * Интерполирует строку
     * Перезаписывает буффер результируюзей строкой
     */
    public void interpolate() {
        StringBuilder buf = new StringBuilder();
        while (ind < str.length()) {
            if (str.charAt(ind) == '$') {
                ind++;
                buf.append(Environment.get(getVariable()));
            } else {
                buf.append(str.charAt(ind));
                ind++;
            }
        }
        str=buf.toString();
    }

    /**
     * Ищет максимально возможное валидное имя переменной в строке после знака $
     * @return - имя переменной
     */
    private String getVariable() {
        int begInd = ind;
        if(!Character.isLetter(str.charAt(ind))) {
            return "";
        }
        while (ind < str.length() && (Character.isLetter(str.charAt(ind)) || Character.isDigit(str.charAt(ind)))) {
            ind++;
        }
        return str.substring(begInd,ind);
    }

    public String getStr() {
        return str;
    }
}
