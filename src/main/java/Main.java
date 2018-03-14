import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static final Map<Character, List<Character>> map = new TreeMap<>();
    private List<String> dictionary;
    private List<String> input;

    public Main() {
        ClassLoader classLoader = getClass().getClassLoader();
        File inputFile = new File(classLoader.getResource("myinput.txt").getFile());
        File dictionaryFile = new File(classLoader.getResource("mydict.txt").getFile());
//
//        File inputFile = new File(classLoader.getResource("input_small.txt").getFile());
//        File dictionaryFile = new File(classLoader.getResource("dictionary_small.txt").getFile());
        dictionary = readFile(dictionaryFile)
                .stream()
                .map(s -> s.replace("\"", "").replace("-", ""))
//                .map(String::toCharArray)
                .collect(Collectors.toList());
        input = readFile(inputFile);
        map.put('0', Arrays.asList('e', 'E'));
        map.put('1', Arrays.asList('j', 'n', 'q', 'J', 'N', 'Q'));
        map.put('2', Arrays.asList('r', 'w', 'x', 'R', 'W', 'X'));
        map.put('3', Arrays.asList('d', 's', 'y', 'D', 'S', 'Y'));
        map.put('4', Arrays.asList('f', 't', 'F', 'T'));
        map.put('5', Arrays.asList('a', 'm', 'a', 'M'));
        map.put('6', Arrays.asList('c', 'i', 'v', 'C', 'I', 'V'));
        map.put('7', Arrays.asList('b', 'k', 'u', 'B', 'K', 'U'));
        map.put('8', Arrays.asList('l', 'o', 'p', 'L', 'O', 'P'));
        map.put('9', Arrays.asList('g', 'h', 'z', 'G', 'H', 'Z'));
    }

    private void proceed() {
        // clean dictionary from umlauts
        String testNumber = input.get(0);

//        findEncodings(testNumber);
        input.forEach(this::findEncodings);
    }

    private void findEncodings(String number) {
//        //System.out.println("findEncodings( " + number + " ) called");
        String preparedNumber = number
                .replace("-", "")
                .replace("/", "");
        String num = preparedNumber;
        List<PartialEncoding> tempDictionary = getAllPartialEncodings(num, new PartialEncoding());
        tempDictionary.forEach(encoding ->
                System.out.println(number + ": " + encoding.getBeautyString())
        );
    }


    List<PartialEncoding> getAllPartialEncodings(String num, PartialEncoding currentEncoding) {
        if (num.length() == currentEncoding.getString().length()) {
            //System.out.println(num + " is fully encoded by " + currentEncoding.getBeautyString());
            return Collections.singletonList(currentEncoding);
        }
        List<PartialEncoding> result = new ArrayList<>();
        int numOffset = currentEncoding.getString().length();
        //System.out.println("getAllPartialEncodings " + num + ", encoded part: " + currentEncoding.getBeautyString());

        //narrow with first non-encoded letter of word
        List<String> smallDictionary = narrowDict(num, dictionary, numOffset, 0);


        for (String word : smallDictionary) {
            if (isPartiallyEncoding(num, word, currentEncoding.getString().length(), 0)) {
                //System.out.println("Found partially encoding num " + num + "; Already encoded " + currentEncoding.getBeautyString() + " + " + word);
                PartialEncoding pe = PartialEncoding.from(currentEncoding);
                pe.addWord(word);
                pe.isLastTokenDigit = false;
                List<PartialEncoding> r = getAllPartialEncodings(num, pe);
                result.addAll(r);

            }
        }

        if (smallDictionary.isEmpty() && !currentEncoding.isLastTokenDigit) {
            //System.out.println("Try digit " + num.charAt(numOffset));
            currentEncoding.addDigit(num.charAt(numOffset));
            currentEncoding.isLastTokenDigit = true;
            List<PartialEncoding> r = getAllPartialEncodings(num, currentEncoding);
            result.addAll(r);
            return result;

        }
        return result;
    }


    List<String> narrowDict(String num, List<String> dict, int numOffset, int wordOffset) {
        List<String> result = new ArrayList<>();
        char digit = num.charAt(numOffset);
        for (String word : dict) {
            if (map.get(digit).contains(word.charAt(wordOffset)) && //if current letter of word can map this digit
                    word.length() - wordOffset <= num.length() - numOffset) { //and remaining word is shorter than remaining digits
                result.add(word);
            }
        }

        return result;
    }

    boolean isPartiallyEncoding(String num, String word, int numOffset, int wordOffset) {
        if (num.length() - numOffset < word.length() - wordOffset) {
            return false;
        }
        for (int i = 0; i < word.length(); i++) {
            if (!map.get(num.charAt(numOffset + i)).contains(word.charAt(wordOffset + i)))
                return false;
        }
        return true;
    }

//    List<char[]> narrowDict(char[] num, List<char[]> dict) {
//        List<char[]> result = new ArrayList<>();
//        char digit = num[0];
//        for (char[] word : dict) {
//            if (map.get(digit).contains(word[0]) && //if current letter of word can map this digit
//                    word.length < num.length) { //and word is shorter than digits
//                result.add(word);
//            }
//        });
//        return result;
//    }


    {
//            return false;
//        }
//        for (int i = 0; i < num.length; i++) {
//            if (!map.get(num[offset + i]).contains(word[i]))
//                return false;
//        }
//        return true;
    }


    private List<String> readFile(File file) {
        List<String> stringList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while (line != null) {
                stringList.add(line);
                line = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringList;
    }

    public static void main(String[] args) {
        Main obj = new Main();
        obj.proceed();
//        //System.out.println("Bye");
    }

}
