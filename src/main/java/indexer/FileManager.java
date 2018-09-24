package indexer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

/**
 *
 */
public class FileManager {

    private Map<String,Map<String, Integer>> documents;
    private Map<String, Integer> vocabulary;
    private PrintWriter writer;

    public FileManager(Map<String,Map<String, Integer>> documents, Map<String, Integer> vocabulary) {
        this.documents = documents;
        this.vocabulary = vocabulary;
        this.writer = null;
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

    private void writeToFile (int index, String value){
        switch (index){
            case 0:
            {
                this.completeSpaces(value, 30);
                this.writer.print(" ");
                break;
            }
            case 1:
            {
                this.completeSpaces(value, 12);
                this.writer.print(" ");
                break;
            }
            default:
            {
                this.completeSpaces(value, 20);
                this.writer.println();
                break;
            }
        }
    }

    private void completeSpaces (String value, int size){
        if (value.length() < size){
            for (int i = value.length() + 1; i < size; i++){
                value += " ";
            }
            this.writer.print(value);
        }
    }

    public void generateTokFiles(Map<String,Map<String, Integer>> documents, String filePath){
        int max = 1;
        for (Map.Entry<String,Map<String,Integer>> entry: this.documents.entrySet()){
            try {
                this.writer = new PrintWriter(filePath + entry.getKey().replace(".html",".tok"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //max = entry.; OJO Calcular el máximo
            for (Map.Entry<String,Integer> word: entry.getValue().entrySet()){
                this.writeToFile(0,word.getKey());
                this.writeToFile(1,word.getValue().toString());
                this.writeToFile(2,Integer.toString(word.getValue()/max));
            }
            this.writer.close();
        }
    }

    public void generateVocabularyFile(Map<String,Integer> vocabulary, String filePath){
        try {
            this.writer = new PrintWriter(filePath + "vocabulario.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (Map.Entry<String,Integer> word: vocabulary.entrySet()){
            this.writeToFile(0,word.getKey());
            this.writeToFile(1,word.getValue().toString());
            this.writeToFile(2,word.getValue().toString()); //OJO Calcular la cantidad de documentos en los que sale
        }
        this.writer.close();

    }
}
