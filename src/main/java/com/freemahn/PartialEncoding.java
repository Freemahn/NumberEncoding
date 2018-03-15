package com.freemahn;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents partial encoding of number
 */
class PartialEncoding {

    /**
     * List of tokens in current encoding state
     * Token can be word or digit
     */
    private List<String> tokens;

    boolean isLastTokenDigit;

    /**
     * Length of current encoding in chars
     */
    private int length;

    PartialEncoding() {
        tokens = new ArrayList<>();
        length = 0;
        isLastTokenDigit = false;
    }


    PartialEncoding addWord(String token) {
        tokens.add(token);
        length += token.length();
        isLastTokenDigit = false;
        return this;
    }

    PartialEncoding addDigit(char token) {
        tokens.add(String.valueOf(token));
        isLastTokenDigit = true;
        length++;
        return this;
    }

    /**
     * Factory method to create copy of encoding
     * @param partialEncoding encoding to create copy from
     * @return full copy of partialEncoding
     */
    public static PartialEncoding from(PartialEncoding partialEncoding) {
        PartialEncoding copy = new PartialEncoding();
        copy.tokens = new ArrayList<>(partialEncoding.tokens);
        copy.length = partialEncoding.length;
        copy.isLastTokenDigit = partialEncoding.isLastTokenDigit;
        return copy;
    }


    String getBeautyString() {
        return String.join(" ", tokens);
    }

    int length() {
        return this.length;
    }


    @Override
    public String toString() {
        return "[com.freemahn.PartialEncoding: " + getBeautyString() + "]";
    }

}