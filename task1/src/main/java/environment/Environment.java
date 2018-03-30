package environment;

import java.util.HashMap;

/**
 * Класс, хранящий окружение переменных
 */
public class Environment {
    /**
     * Таблица, хранящие в качестве ключа - имя переменной окружения, а в качестве значения - значение переменной
     */
    private static final HashMap<String, String> table = new HashMap<>();

    private Environment() {};

    /**
     * Добавляет переменную в окружение
     * @param key - имя переменной
     * @param val - значение переменной
     */
    public static void set(String key, String val) {
        table.put(key,val);
    }

    /**
     * Возвращает значение переменной
     * Возвращает пустую строку, если переменной нет в окружение
     * @param key - имя переменной
     * @return - значение переменной
     */
    public static String get(String key) {
        if(table.containsKey(key)) {
            return table.get(key);
        }
        return "";
    }
}
