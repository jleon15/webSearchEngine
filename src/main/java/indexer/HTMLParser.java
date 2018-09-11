package indexer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.lang.model.util.Elements;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class HTMLParser {

    private List<Map<String, Integer>> documents;
    private Map<String, Integer> vocabulary;

    public HTMLParser(List<Map<String, Integer>> documents, Map<String, Integer> vocabulary){
        this.documents = documents;
        this.vocabulary = vocabulary;
    }


    public List<Map<String, Integer>> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Map<String, Integer>> documents) {
        this.documents = documents;
    }

    public Map<String, Integer> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(Map<String, Integer> vocabulary) {
        this.vocabulary = vocabulary;
    }

    public void parseFile(String fileLocation){
        Document doc = null;
        try {
            doc = Jsoup.connect(fileLocation).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements links = (Elements) doc.select("a");
        Elements sections = (Elements) doc.select("section");
        Elements logo = (Elements) doc.select(".spring-logo--container");
        Elements pagination = (Elements) doc.select("#pagination_control");
        Elements divsDescendant = (Elements) doc.select("header div");
        Elements divsDirect = (Elements) doc.select("header > div");
    }




}
