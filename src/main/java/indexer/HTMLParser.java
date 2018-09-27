package indexer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.mozilla.universalchardet.UniversalDetector;

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

    private static final String URLS_REGEX = "(http|ftp|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#\\-]*[\\w@?^=%&/~+#\\-])?";
    private static final String STOPWORDS_FILE_PATH = "./src/main/java/resources/stopwords.txt";
    private static final String NUMBERS_WORDS_REGEX = "([a-z]+[\\d]+[\\w@]*|[\\d]+[a-z]+[\\w@]*)";
    private static final String SPECIAL_SYMBOLS_REGEX = "[^a-z0-9ñáéíóú\\s]";

    private Map<String, Map<String, Double>> documents;
    private Map<String, Double> vocabulary;
    private List<String> stopWords;

    HTMLParser(Map<String, Map<String, Double>> documents, Map<String, Double> vocabulary) {
        this.documents = documents;
        this.vocabulary = vocabulary;
        this.stopWords = new LinkedList<>();
        this.loadStopWords();
    }

    public Map<String, Map<String, Double>> getDocuments() {
        return documents;
    }

    public void setDocuments(Map<String, Map<String, Double>> documents) {
        this.documents = documents;
    }

    public Map<String, Double> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(Map<String, Double> vocabulary) {
        this.vocabulary = vocabulary;
    }

    private void loadStopWords() {
        try (Stream<String> stream = Files.lines(Paths.get(HTMLParser.STOPWORDS_FILE_PATH))) {
            stream.forEach(this.stopWords::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseFile(String fileName, String filePath) {
        File inputFile = new File(filePath + fileName);
        String charset = "UTF-8";

        UniversalDetector universalDetector = new UniversalDetector(null);
        byte[] buf = new byte[4096];
        try {
            FileInputStream fileInputStream = new FileInputStream(inputFile);

            int keepReading;
            while ((keepReading = fileInputStream.read(buf)) > 0 && !universalDetector.isDone()) {
                universalDetector.handleData(buf, 0, keepReading);
            }

            universalDetector.dataEnd();

            String encoding = universalDetector.getDetectedCharset();
            if (encoding != null) {
                charset = encoding;
            }
            universalDetector.reset();

        } catch (IOException e) {
            e.printStackTrace();
        }

        String doc = "";
        try {
            Document document = Jsoup.parse(inputFile, charset);
            document.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
            doc = document.text();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc = doc.toLowerCase();
        doc = doc.replaceAll(HTMLParser.URLS_REGEX, "");
        doc = doc.replaceAll(HTMLParser.SPECIAL_SYMBOLS_REGEX, "");
        doc = doc.replaceAll(HTMLParser.NUMBERS_WORDS_REGEX, "");

        String[] text = doc.split(" ");
        Map<String, Double> words = new HashMap<String, Double>();
        for (String aText : text) {
            aText = aText.trim();
            if (!aText.equals("") && !aText.equals(" ") && aText.length() <= 30 && !stopWords.contains(aText)
                    && !this.isSingleCharacter(aText)) {
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
        this.documents.putIfAbsent(fileName, words);
    }

    private boolean isSingleCharacter(String word) {
        if (word.length() > 1) {
            return false;
        }
        try {
            Integer.parseInt(word);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

}
