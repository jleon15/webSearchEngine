package indexer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

/**
 *
 */
public class FileManager {

    private static final String RESULTS_DIRECTORY = "./src/main/java/resources/Results/";

    private Map<String, Map<String, Double>> documents;

    private Map<String, Double> vocabulary;

    /**
     * Permite escribir en los archivos de salida.
     */
    private PrintWriter writer;

    public FileManager(Map<String, Map<String, Double>> documents, Map<String, Double> vocabulary) {
        this.documents = documents;
        this.vocabulary = vocabulary;
        this.writer = null;
    }

    public Map<String, Map<String, Double>> getDocuments() {
        return documents;
    }

    public void setDocuments(Map<String, Map<String, Double>> documents) {
        this.documents = documents;
    }

    public Map<String, Double> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(Map<String, Double> vocabulary) {
        this.vocabulary = vocabulary;
    }

    /**
     * Escribe en los archivos completando la cantidad de espacios en blanco.
     * @param index
     * @param value
     */
    private void writeToFile(int index, String value) {
        switch (index) {
            case 0: {
                this.completeSpaces(value, 30);
                this.writer.print(" ");
                break;
            }
            case 1: {
                this.writeValue(value, 12);
                this.writer.print(" ");
                break;
            }
            default: {
                this.writeValue(value, 20);
                this.writer.println();
                break;
            }
        }
    }

    /**
     * Completa los espacios en blanco.
     * @param value
     * @param size
     */
    private void completeSpaces(String value, int size) {
        if (value.length() < size) {
            for (int i = value.length() + 1; i < size; i++) {
                value += " ";
            }
            this.writer.print(value);
        }
    }

    /**
     * Escribe valores en el archivo.
     * @param value
     * @param maxColumnSize
     */
    private void writeValue(String value, int maxColumnSize) {
        if (value.length() >= maxColumnSize) {
            value = value.substring(0, maxColumnSize - 1);
            this.writer.print(value);
        } else {
            this.completeSpaces(value, maxColumnSize);
        }
    }

    /**
     * Genera los archivos .tok que contienen:
     * -Palabra.
     * -Número de veces que aparece el término en el documento.
     * -Frecuencia normalizada.
     * y manda a escribir en ellos.
     */
    public void generateTokFiles() {
        double max;
        for (Map.Entry<String, Map<String, Double>> entry : this.documents.entrySet()) {
            try {
                this.writer = new PrintWriter(RESULTS_DIRECTORY + entry.getKey().replace(".html", ".tok"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if(entry.getValue().entrySet().size() != 0) {
                max = entry.getValue().entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();

                for (Map.Entry<String, Double> word : entry.getValue().entrySet()) {
                    this.writeToFile(0, word.getKey());
                    this.writeToFile(1, word.getValue().toString());
                    this.writeToFile(2, Double.toString(word.getValue() / max));

                }
            }
            this.writer.flush();
            this.writer.close();
        }
    }

    /**
     * Genera un archivo con el vocabulario, que contiene la cantidad de documentos en los
     * que aparece cada parabra y el idf.
     * @param totalCollectionFiles
     */
    public void generateVocabularyFile(double totalCollectionFiles) {
        try {
            this.writer = new PrintWriter(RESULTS_DIRECTORY + "Vocabulario.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (Map.Entry<String, Double> word : this.vocabulary.entrySet()) {
            this.writeToFile(0, word.getKey());
            this.writeToFile(1, Double.toString(word.getValue()));
            this.writeToFile(2, Double.toString(Math.log10(totalCollectionFiles / word.getValue())));
        }
        this.writer.flush();
        this.writer.close();
    }

}
