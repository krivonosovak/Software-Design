package parse;

import exceptions.*;
import parse.tokens.*;

import java.util.ArrayList;

/**
 * Класс, реализующий разбиение вводимой строки на малые и большие токены
 */
public class Lexer {
    /**
     * Внутрений буфер
     */
    private String str;

    private int ind;

    /**
     * Результирующий список больших токенов
     */
    public ArrayList<BigToken> lst;

    /**
     * Кладёт строку в буффер
     * @param str вводимая строка
     */
    public void setString(String str)
    {
        this.str = str;
        ind = 0;
        lst = new ArrayList<>();
    }

    /**
     * Парсит строку на слова (просто слово, слово с равенством и пайп)
     * Результат кладёт в внутрений буффер
     * @throws BadQuotesException
     */
    public void parse() throws BadQuotesException {
        while (ind < str.length()) {
            switch(str.charAt(ind)) {
                case '|':
                    lst.add(new BigToken(new ArrayList<SmallToken>(), TypeBigToken.Pipe));
                    break;
                case ' ':
                    skipSpace();
                    break;
                default:
                    setToken();
                    break;
            }
            ind++;
        }
        if (lst.size() > 0 && lst.get(lst.size()-1).getType() == TypeBigToken.Pipe) {
            ArrayList<SmallToken> last = new ArrayList<>();
            last.add(new SmallToken("", TypeSmallToken.Command));
            lst.add(new BigToken(last,TypeBigToken.Word));
        }
    }

    /**
     * Пропуск пробелов
     */
    private void skipSpace() {
        while (ind < str.length() && str.charAt(ind) == ' ') {
            ind++;
        }
        ind--;
    }

    /**
     * Расщипляет слова на мелкие токены, типизирует их и упаковывает в результирующий список
     * @throws BadQuotesException
     */
    private void setToken() throws BadQuotesException {
        TypeBigToken type = TypeBigToken.Word;
        ArrayList<SmallToken> lst = new ArrayList<>();
        while (ind < str.length() && str.charAt(ind) != ' ' && str.charAt(ind) != '|' ) {
            switch (str.charAt(ind)) {
                case '=':
                    if(type == TypeBigToken.Word) {
                        type = TypeBigToken.Equal;
                        lst.add(new SmallToken("=",TypeSmallToken.Equal));
                    }
                    break;
                case '\'':
                    ind++;
                    lst.add(new SmallToken(getQuotedString('\''),TypeSmallToken.SingleQuotes));
                    break;
                case '\"':
                    ind++;
                    lst.add(new SmallToken(getQuotedString('\"'),TypeSmallToken.DoubleQuotes));
                    break;
                case '$':
                    ind++;
                    lst.add(new SmallToken(getWord(),TypeSmallToken.Variable));
                    break;
                default:
                    lst.add(new SmallToken(getWord(),TypeSmallToken.Command));
            }
            ind++;
        }
        ind--;
        this.lst.add(new BigToken(lst,type));
    }

    /**
     * Выделяет токен команды
     * @return строка, соответствующая команде
     */
    private String getWord() {
        int begInd = ind;
        while (ind < str.length()) {
            if(str.charAt(ind) == '\'' || str.charAt(ind) == '\"' || str.charAt(ind) == '$' || str.charAt(ind) == '|'
                    || str.charAt(ind) == ' ' || str.charAt(ind) == '=') {
                ind--;
                return str.substring(begInd,ind+1);
            }
            ind++;
        }
        return str.substring(begInd,ind);
    }


    /**
     * Выделяет часть слова под кавычками
     * @param type тип кавычек
     * @return строка под кавычками
     * @throws BadQuotesException если кавычки не закрыты
     */
    private String getQuotedString(char type) throws BadQuotesException {
        int begInd = ind;
        while (ind < str.length()) {
            if(str.charAt(ind) == type) {
                return str.substring(begInd,ind);
            }
            ind++;
        }
        throw new BadQuotesException();
    }

    public String getStr() {
        return str;
    }

    public ArrayList<BigToken> getBigTokens() {
        return lst;
    }
}
