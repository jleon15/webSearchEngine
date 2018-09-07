package indexer;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class FileManager {

    private List<Map<String, Integer>> documents;
    private Map<String, Integer> vocabulary;

    public FileManager(List<Map<String, Integer>> documents, Map<String, Integer> vocabulary) {
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
}
