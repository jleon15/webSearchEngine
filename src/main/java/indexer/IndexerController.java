package indexer;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class IndexerController {

    private List<Map<String, Integer>> documents;
    private Map<String, Integer> vocabulary;
    private HTMLParser htmlParser;
    private FileManager fileManager;

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

    public HTMLParser getHtmlParser() {
        return htmlParser;
    }

    public void setHtmlParser(HTMLParser htmlParser) {
        this.htmlParser = htmlParser;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }
}
