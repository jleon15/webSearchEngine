package indexer;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class HTMLParser {

    private Map<String,Map<String, Integer>> documents;
    private Map<String, Integer> vocabulary;
    private List<String> stopWords;

    public HTMLParser(Map<String,Map<String, Integer>> documents, Map<String, Integer> vocabulary){
        this.documents = documents;
        this.vocabulary = vocabulary;
        this.stopWords = loadStopWords();
    }

    public Map<String,Map<String, Integer>> getDocuments() {
        return documents;
    }

    public void setDocuments(Map<String,Map<String, Integer>> documents) {
        this.documents = documents;
    }

    public Map<String, Integer> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(Map<String, Integer> vocabulary) {
        this.vocabulary = vocabulary;
    }

    private List<String> loadStopWords(){ //Llenarlo puede ser con un archivo
        List<String> stopWords = new LinkedList<String>();
        stopWords.add("de");
        stopWords.add("del");
        stopWords.add("las");
        return stopWords;
    }

    public void parseFile(String fileName, String filePath){
        File inputFile = new File(filePath + fileName);
        String doc = "";
        try {
            doc = Jsoup.parse(inputFile, "UTF-8").text();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc = doc.toLowerCase();
        doc = doc.replaceAll("[~`!@#$%^&*()-+=:;'\",<.>/?¿]",""); //OJO falta quitar los links
//        System.out.println(doc);
        String[] text = doc.split(" ");
        Map<String,Integer> words = new HashMap<String,Integer>();
        for(int i = 0; i < text.length; ++i){
            if(text[i].length() < 30 && !stopWords.contains(text[i])) {
                if (!words.containsKey(text[i])) {
                    words.put(text[i], 1);
                } else {
                    words.put(text[i], words.get(text[i]) + 1);
                }
                if (!vocabulary.containsKey(text[i])) {
                    vocabulary.put(text[i], 1);
                } else {
                    vocabulary.put(text[i], vocabulary.get(text[i]) + 1);
                }
            }
        }
        this.documents.putIfAbsent(fileName,words);
    }

}
