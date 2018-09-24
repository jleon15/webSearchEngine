package indexer;

import org.jsoup.Jsoup;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 *
 */
public class HTMLParser {

    private static final String URLS_REGEX = "(http|ftp|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?";
    private static final String STOPWORDS_FILE_PATH = "./src/main/java/resources/stopwords.txt";
    private static final String NUMBERS_WORDS_REGEX = "([A-Za-z]+[\\d~`!@#$%^&*()-+=:;'\",<.>/?¿@]+[\\w@]*|[\\d~`!@#$%^&*()-+=:;'\",<.>/?¿@]+[A-Za-z]+[\\w@]*)";
    private static final String SPECIAL_SYMBOLS_REGEX = "[~`!@#$%^&*()-+=:;'\",<.>/?¿]";

    private Map<String,Map<String, Double>> documents;
    private Map<String, Double> vocabulary;
    private List<String> stopWords;

    HTMLParser(Map<String,Map<String, Double>> documents, Map<String, Double> vocabulary){
        this.documents = documents;
        this.vocabulary = vocabulary;
        this.stopWords = new LinkedList<>();
        this.loadStopWords();
    }

    public Map<String,Map<String, Double>> getDocuments() {
        return documents;
    }

    public void setDocuments(Map<String,Map<String, Double>> documents) {
        this.documents = documents;
    }

    public Map<String, Double> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(Map<String, Double> vocabulary) {
        this.vocabulary = vocabulary;
    }

    private void loadStopWords(){
        try (Stream<String> stream = Files.lines(Paths.get(HTMLParser.STOPWORDS_FILE_PATH))) {
            stream.forEach(this.stopWords::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseFile(String fileName, String filePath) {
        File inputFile = new File(filePath + fileName);
        String doc = "";
        try {
            doc = Jsoup.parse(inputFile, "UTF-8").text();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc = doc.toLowerCase();
        doc = doc.replaceAll(HTMLParser.URLS_REGEX, "");
        doc = doc.replaceAll(HTMLParser.NUMBERS_WORDS_REGEX,"");
        doc = doc.replaceAll(HTMLParser.SPECIAL_SYMBOLS_REGEX,"");

        String[] text = doc.split(" ");
        Map<String,Double> words = new HashMap<String,Double>();
        for (String aText : text) {
            aText = aText.trim();
            if (!aText.equals("") && !aText.equals(" ") && aText.length() < 30 && !stopWords.contains(aText)) {
                //System.out.println("*"+aText+"*" + aText.length());
                if (!words.containsKey(aText)) {
                    words.put(aText, 1.0);
                } else {
                    words.put(aText, words.get(aText) + 1);
                }
                if (!vocabulary.containsKey(aText)) {
                    vocabulary.put(aText, 1.0);
                } else {
                    vocabulary.put(aText, vocabulary.get(aText) + 1);
                }
            }
        }
        this.documents.putIfAbsent(fileName,words);
    }

}
