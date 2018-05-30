package parse.tokens;

/**
 * Токены, из которых составлены слова в вводимой строке
 */
public class SmallToken {
    /**
     * Значение токена
     */
    private String value;
    /**
     * Тип токена
     */
    private TypeSmallToken type;

    public SmallToken(String str, TypeSmallToken t) {
        value = str;
        type = t;
    }

    public TypeSmallToken getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
