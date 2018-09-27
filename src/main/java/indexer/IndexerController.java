package indexer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class IndexerController {

    private static final String BASE_DIRECTORY = "./src/main/java/resources/Coleccion/";

    private Map<String,Map<String,Double>> documents;
    private Map<String, Double> vocabulary;
    private HTMLParser htmlParser;
    private FileManager fileManager;

    private String[] textfiles;
    private double totalCollectionFiles;

    public IndexerController() {
        this.documents = new HashMap<String,Map<String,Double>>();
        this.vocabulary = new HashMap<String, Double>();
        this.htmlParser = new HTMLParser(this.documents, this.vocabulary);
        this.fileManager = new FileManager(this.documents, this.vocabulary);
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

    private void findFiles(){
        File file = new File(BASE_DIRECTORY);

        FilenameFilter filter = (dir, fileName) -> fileName.endsWith(".html");

        this.textfiles = file.list(filter);
        if(this.textfiles == null){
            System.out.println("No se encontraron archivos.");
        }
    }

    private void parseFiles (){
        for (String textfile : textfiles) {
            this.totalCollectionFiles++;
            this.htmlParser.parseFile(textfile, IndexerController.BASE_DIRECTORY);
        }
    }

    private void generateFiles() {
        this.fileManager.generateTokFiles();
        this.fileManager.generateVocabularyFile(this.totalCollectionFiles);
    }

    public static void main (String args[]){
        IndexerController indexerController = new indexer.IndexerController();
        indexerController.findFiles();
        indexerController.parseFiles();
        indexerController.generateFiles();
    }

}
