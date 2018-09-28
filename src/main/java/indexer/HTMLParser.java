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

    /**
     * Dirección del archivo que contiene los "stopwords".
     */
    private static final String STOPWORDS_FILE_PATH = "./src/main/java/resources/stopwords.txt";

    /**
     * Expresiones regulares que permiten excluir contenido de los htmls.
     */
    private static final String URLS_REGEX = "(http|ftp|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#\\-]*[\\w@?^=%&/~+#\\-])?";
    private static final String NUMBERS_WORDS_REGEX = "([a-z]+[\\d]+[\\w@]*|[\\d]+[a-z]+[\\w@]*)";
    private static final String SPECIAL_SYMBOLS_REGEX = "[^a-z0-9ñáéíóú_\\s]";
    private static final String SPECIAL_SPACES_REGEX = "[\n\r]";

    private Map<String, Map<String, Double>> documents;
    private Map<String, Double> vocabulary;

    /**
     * Contiene una lista de los "stopwords" que se recuperaron del archivo.
     */
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

    /**
     * Carga los "stopwords" del archivo correspondiente y los mete en una lista enlazada.
     */
    private void loadStopWords() {
        try (Stream<String> stream = Files.lines(Paths.get(HTMLParser.STOPWORDS_FILE_PATH))) {
            stream.forEach(this.stopWords::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parsea cada uno de los htmls con Jsoup, y tomando cada uno de los términos:
     * -Los "filtra" con los regex para que estén de acuerdo a las reglas establecidas.
     * -Los añade al HashMap de "vocabulary" que posee la palabra y la cantidad de documentos en las que aparece.
     * -Los añade al HashMap "documents" que posee el nombre del documento y
     * las palabras del documento con la cantidad de veces que aparece dicha palabra en el documento.
     * @param fileName
     * @param filePath
     */
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
        doc = doc.replaceAll(HTMLParser.SPECIAL_SPACES_REGEX, " ");
        doc = doc.replaceAll(HTMLParser.SPECIAL_SYMBOLS_REGEX, " ");
        doc = doc.replaceAll(HTMLParser.NUMBERS_WORDS_REGEX, "");

        String[] text = doc.split(" ");
        Map<String, Double> words = new HashMap<String, Double>();
        for (String term : text) {
            term = term.trim();
            if (!term.equals("") && !term.equals(" ") && term.length() <= 30 && !stopWords.contains(term)
                    && !this.isSmallWord(term)) {
                if (!words.containsKey(term)) {
                    words.put(term, 1.0);
                    if (!vocabulary.containsKey(term)) {
                        vocabulary.put(term, 1.0);
                    } else {
                        vocabulary.put(term, vocabulary.get(term) + 1);
                    }
                } else {
                    words.put(term, words.get(term) + 1);
                }
            }
        }
        this.documents.putIfAbsent(fileName, words);
    }

    /**
     *
     * @param word
     * @return
     */
    private boolean isSmallWord(String word) {
        if (word.length() > 2) {
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
