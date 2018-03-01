package parse.tokens;

import java.util.ArrayList;

/**
 * Токены, которые соответствут словам, разделённым пробелами в водимой строке
 */
public class BigToken {
    /**
     * Список частей из которых составлено слово
     */
    private ArrayList<SmallToken> lst;
    /**
     * Тип токена
     */
    private TypeBigToken type;

    public BigToken(ArrayList<SmallToken> lst, TypeBigToken type) {
        this.lst = new ArrayList<SmallToken>(lst);
        this.type = type;
    }

    public ArrayList<SmallToken> getLst() {
        return lst;
    }

    public TypeBigToken getType() {
        return type;
    }

    public void setType(TypeBigToken type) {
        this.type = type;
    }
}
