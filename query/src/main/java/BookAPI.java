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
        return "DatamartWords/" + firstLetter + "/" + second + "/" + third + "/" + word + ".txt";
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

    public static String getMetadataFilePath(String bookId) {
        bookId = bookId.substring(0, bookId.lastIndexOf("."));
        String firstDigit = bookId.substring(0, 1);
        if (bookId.length() == 1) {
            return "Datamart/metadata/" + firstDigit + "/" + bookId + ".json";
        } else if (bookId.length() == 2) {
            String secondDigits = bookId.substring(0, 2);
            return "Datamart/metadata/" + firstDigit + "/" + secondDigits+ "/" +bookId + ".json";
        } else {
            String secondDigits = bookId.substring(0, 2);
            String thirdDigits = bookId.substring(0, 3);
            return "Datamart/metadata/" + firstDigit + "/" + secondDigits+ "/" + thirdDigits + "/" + bookId + ".json";
        }
    }
}

