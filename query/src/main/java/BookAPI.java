import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import spark.Spark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BookAPI {


    public static String getFilePath(String word) {
        String firstLetter = word.substring(0, 1);
        String second = word.substring(0, 2);
        String third = word.substring(0, 3);
        return "Datamart/" + firstLetter + "/" + second + "/" + third + "/" + word + ".txt";
    }

    public static String readJsonFromFile(String filePath) {
        StringBuilder jsonText = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                jsonText.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonText.toString();
    }
}

