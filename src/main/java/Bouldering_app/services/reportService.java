package Bouldering_app.services;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class reportService {

    private static final String CSV_FILE_PATH = "src/main/java/Bouldering_app/report.csv";

    public static void logReport(String function) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH, true))){
            LocalDateTime now = LocalDateTime.now();
            writer.write(function + "," + now + "\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
