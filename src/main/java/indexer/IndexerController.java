package indexer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 */
public class IndexerController {

    /**
     * Tiene el directorio base donde se tienen que buscar todos los archivos de la colección.
     */
    private static final String BASE_DIRECTORY = "./src/main/java/resources/Coleccion/";

    /**
     * Este mapa contiene:
     * -Llave: Nombre del documento html.
     * -Valor: Mapa <Palabra, Cantidad de veces que aparece esa palabra en el documento>.
     */
    private Map<String,Map<String,Double>> documents;

    /**
     * Este mapa contiene:
     * -Llave: Palabra.
     * -Valor: Cantidad de documentos en los que aparece dicha palabra.
     */
    private Map<String, Double> vocabulary;

    /**
     * Instancia del HTMLParser que sirve para parsear cada uno de los archivos html de la colección.
     */
    private HTMLParser htmlParser;

    /**
     * Instancia del FileManaher que sirve para generar los archivos .tok y el de vocabulario.
     */
    private FileManager fileManager;

    /**
     * Array que contiene todos los nombres de los archivos a parsear.
     */
    private String[] textfiles;

    /**
     * Tiene la cantidad de archivos que posee la colacción.
     */
    private double totalCollectionFiles;

    public IndexerController() {
        this.documents = new TreeMap<String,Map<String,Double>>();
        this.vocabulary = new TreeMap<String, Double>();
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

    /**
     * Encuentra cuáles son cada uno de los archivos que se deben parsear,
     * y almacena sus nombre en un array.
     */
    private void findFiles(){
        File file = new File(BASE_DIRECTORY);

        FilenameFilter filter = (dir, fileName) -> fileName.endsWith(".html");

        this.textfiles = file.list(filter);
        if(this.textfiles == null){
            System.out.println("No se encontraron archivos.");
        }
    }

    /**
     * Itera sobre el array que contiene los nombres de los archivos a parsear y llama
     * al método de parseo que contiene el HTMLParser.
     */
    private void parseFiles (){
        for (String textfile : textfiles) {
            this.totalCollectionFiles++;
            this.htmlParser.parseFile(textfile, IndexerController.BASE_DIRECTORY);
        }
    }

    /**
     * Genera los archivos .tok y el archivo de Vocabulario.
     */
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
