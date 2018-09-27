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

    private void completeSpaces(String value, int size) {
        if (value.length() < size) {
            for (int i = value.length() + 1; i < size; i++) {
                value += " ";
            }
            this.writer.print(value);
        }
    }

    private void writeValue(String value, int maxColumnSize) {
        if (value.length() >= maxColumnSize) {
            value = value.substring(0, maxColumnSize - 1);
            this.writer.print(value);
        } else {
            this.completeSpaces(value, maxColumnSize);
        }
    }

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

            this.writer.close();
        }
    }

    public void generateVocabularyFile(double totalCollectionFiles) {
        try {
            this.writer = new PrintWriter(RESULTS_DIRECTORY + "Vocabulario.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (Map.Entry<String, Double> word : this.vocabulary.entrySet()) {
            double totalAppearancesInDocuments = this.calculateTotalAppearancesInDocuments(word.getKey());
            this.writeToFile(0, word.getKey());
            this.writeToFile(1, Double.toString(totalAppearancesInDocuments));
            this.writeToFile(2, Double.toString(Math.log10(totalCollectionFiles / totalAppearancesInDocuments)));
        }
        this.writer.close();
    }

    private int calculateTotalAppearancesInDocuments(String word) {
        int total = 0;
        for (Map.Entry<String, Map<String, Double>> document : this.documents.entrySet()) {
            if (document.getValue().containsKey(word)) {
                total++;
            }
        }
        return total;
    }
}
