package indexer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class IndexerController {

    private static final String BASE_DIRECTORY = "./src/main/java/resources/Coleccion/";

    private List<Map<String, Integer>> documents;
    private Map<String, Integer> vocabulary;
    private HTMLParser htmlParser;
    private FileManager fileManager;

    private String[] textfiles;

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

    public void findFiles(){
        File file = new File(".");

        FilenameFilter filter = new FilenameFilter(){
            public boolean accept(File dir, String fileName) {
                return fileName.endsWith("html");
            }
        };


        this.textfiles = file.list(filter);
        if(this.textfiles == null){
            System.out.println("No se encontraron archivos.");
        }
    }

    private void parseFiles (){
        for (int i = 0; i < textfiles.length; i++) {
            this.htmlParser.parseFile(BASE_DIRECTORY + textfiles[i]);
        }
    }

}
