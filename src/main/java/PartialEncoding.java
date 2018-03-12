import java.util.ArrayList;
import java.util.List;

class PartialEncoding {
    public static PartialEncoding EMPTY = new PartialEncoding();
    List<String> words = new ArrayList<>();

    String getString() {
        return String.join("", words);
    }

    String getBeautyString() {
        return String.join(" ", words);
    }

    PartialEncoding() {

    }

    PartialEncoding addWord(String word) {
        words.add(word);
        return this;
    }
    void clear(){
        words = new ArrayList<>();
    }
}