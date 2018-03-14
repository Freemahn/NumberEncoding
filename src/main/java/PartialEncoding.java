import java.util.ArrayList;
import java.util.List;

class PartialEncoding {

    List<String> tokens = new ArrayList<>();
    boolean isLastTokenDigit = false;

    PartialEncoding() {

    }

    PartialEncoding addWord(String token) {
        tokens.add(token);
//        isLastTokenDigit = false;
        return this;
    }

    PartialEncoding addDigit(char token) {
        tokens.add(String.valueOf(token));
//        isLastTokenDigit = true;
        return this;
    }

    public static PartialEncoding from(PartialEncoding partialEncoding) {
        PartialEncoding copy = new PartialEncoding();
        copy.tokens = new ArrayList<>(partialEncoding.tokens);
        return copy;
    }


    PartialEncoding removeLastToken() {
        if (!tokens.isEmpty())
            tokens.remove(tokens.size() - 1);
        return this;
    }

    void clear() {
        tokens = new ArrayList<>();
    }

    String getString() {
        return String.join("", tokens);
    }

    String getBeautyString() {
        return String.join(" ", tokens);
    }

    @Override
    public String toString() {
        return "[PartialEncoding: " + getBeautyString() + "]";
    }
}