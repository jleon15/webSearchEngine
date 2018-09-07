package indexer;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class HTMLParser {

    private List<Map<String, Integer>> documents;
    private Map<String, Integer> vocabulary;

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
}
