package com.freemahn;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Encoder {
    private static final Map<Character, List<Character>> encodingMap = new TreeMap<>();

    private List<String> dictionary;

    private String dictionaryFilepath;

    private String inputFilepath;

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("You should specify two arguments: \n- path to dictionary file\n- path to input file");
            System.exit(1);
        }
        String dictionaryFilePath = args[0];
        String inputFilePath = args[1];
        Encoder obj = new Encoder(dictionaryFilePath, inputFilePath);
        obj.parse();
    }

    public Encoder(String dictionaryFilePath, String inputFilePath) throws IOException {
        this.dictionaryFilepath = dictionaryFilePath;
        this.inputFilepath = inputFilePath;
        initEncodingMap();
        initDictionary();
    }

    private void initDictionary() throws IOException {
        dictionary = Files.readAllLines(Paths.get(this.dictionaryFilepath));
    }

    private void initEncodingMap() {
        encodingMap.put('0', Arrays.asList('e', 'E'));
        encodingMap.put('1', Arrays.asList('j', 'n', 'q', 'J', 'N', 'Q'));
        encodingMap.put('2', Arrays.asList('r', 'w', 'x', 'R', 'W', 'X'));
        encodingMap.put('3', Arrays.asList('d', 's', 'y', 'D', 'S', 'Y'));
        encodingMap.put('4', Arrays.asList('f', 't', 'F', 'T'));
        encodingMap.put('5', Arrays.asList('a', 'm', 'a', 'M'));
        encodingMap.put('6', Arrays.asList('c', 'i', 'v', 'C', 'I', 'V'));
        encodingMap.put('7', Arrays.asList('b', 'k', 'u', 'B', 'K', 'U'));
        encodingMap.put('8', Arrays.asList('l', 'o', 'p', 'L', 'O', 'P'));
        encodingMap.put('9', Arrays.asList('g', 'h', 'z', 'G', 'H', 'Z'));
    }


    /**
     * Parses input file line by line without putting it in memory
     *
     * @throws IOException if something iw wrong while reading
     */
    private void parse() throws IOException {
        Scanner sc;
        try (FileInputStream inputStream = new FileInputStream(this.inputFilepath)) {
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String number = sc.nextLine();
                findEncodings(number);
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        }
    }


    private void findEncodings(String number) {
        String preparedNumber = number
                .replace("-", "")
                .replace("/", "");
        List<PartialEncoding> tempDictionary = getAllPartialEncodings(preparedNumber, new PartialEncoding());

        tempDictionary.forEach(encoding ->
                System.out.println(number + ": " + encoding.getBeautyString())
        );
    }

    /**
     * Recursive searches partial encodings for word
     * @param num number to encode
     * @param currentEncoding current partial encoding of the number
     * @return list of all partial encodings
     */

    List<PartialEncoding> getAllPartialEncodings(String num, PartialEncoding currentEncoding) {
        if (num.length() == currentEncoding.length()) {
            // number is fully encoded by current encoding. Return this encoding
            return Collections.singletonList(currentEncoding);
        }
        List<PartialEncoding> result = new ArrayList<>();
        int numOffset = currentEncoding.length();

        //narrow with first non-encoded letter of word
        List<String> smallDictionary = narrowDict(num, dictionary, numOffset);

        boolean needDigit = true;
        for (String word : smallDictionary) {
            if (isPartiallyEncoding(num, word, currentEncoding.length())) {
                //word can encode some part of number
                PartialEncoding pe = PartialEncoding.from(currentEncoding);
                pe.addWord(word);
                needDigit = false;
                result.addAll(getAllPartialEncodings(num, pe));

            }
        }

        if (needDigit && !currentEncoding.isLastTokenDigit) {
            //can't encode with word, try digit
            currentEncoding.addDigit(num.charAt(numOffset));
            result.addAll(getAllPartialEncodings(num, currentEncoding));
            return result;

        }
        return result;
    }

    /**
     * This function narrows dictionary so it will contain only words
     * which first letter can encode digit
     *
     * @param num       current number
     * @param dict      dictionary to be reduced
     * @param numOffset amount of digits that are already encoded
     * @return reduced dictionary
     */

    List<String> narrowDict(String num, List<String> dict, int numOffset) {
        List<String> result = new ArrayList<>();
        char digit = num.charAt(numOffset);
        for (String word : dict) {
            if (encodingMap.get(digit).contains(word.charAt(0)) && //if first letter of word can encode this digit
                    word.length() <= num.length() - numOffset) { //and word is shorter than remaining digits
                result.add(word);
            }
        }

        return result;
    }

    /**
     * Checks if word can encode number
     *
     * @param num       current number
     * @param word      word to check
     * @param numOffset amount of digits that are already encoded
     * @return true if word can encode remaining part if number, otherwise false
     */
    private boolean isPartiallyEncoding(String num, String word, int numOffset) {
        word = word.replace("\"", "")
                .replace("-", "");
        // if word is longer than part of number to encode
        if (num.length() - numOffset < word.length()) {
            return false;
        }

        //check if all word's letters correspond to digit and can map them
        for (int i = 0; i < word.length(); i++) {
            if (!encodingMap.get(num.charAt(numOffset + i)).contains(word.charAt(i)))
                return false;
        }
        return true;
    }
}
