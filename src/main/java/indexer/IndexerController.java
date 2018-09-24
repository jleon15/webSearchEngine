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

    public void findFiles(){ //CREO que hay que leer el archivo de URLs
        File file = new File(BASE_DIRECTORY);

        FilenameFilter filter = new FilenameFilter(){
            public boolean accept(File dir, String fileName) {
                return fileName.endsWith("1_AP.html");
            }
        };

        this.textfiles = file.list(filter);
        if(this.textfiles == null){
            System.out.println("No se encontraron archivos.");
        }
    }

    private void parseFiles (){
        for (int i = 0; i < textfiles.length; i++) {
            this.htmlParser.parseFile(textfiles[i], this.BASE_DIRECTORY);
        }
    }

    private void generateFiles() {
        this.fileManager.generateTokFiles();
        this.fileManager.generateVocabularyFile();
    }

    public static void main (String args[]){
        IndexerController indexerController = new indexer.IndexerController();
        indexerController.findFiles();
        indexerController.parseFiles();
        indexerController.generateFiles();
    }

}
