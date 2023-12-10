import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import spark.Spark;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Main {

    public static void main(String[] args) {

        Spark.port(8080);

        Spark.get("/:word", (request, response) -> {
            String word = request.params(":word");
            String path = BookAPI.getFilePath(word);

            File file = new File(path);

            if (!file.exists()) {
                return "The word \"" + word + "\" does not appear in any book";
            } else {
                String json = BookAPI.readJsonFromFile(path);
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

                String jword = jsonObject.get("word").getAsString();
                JsonObject references = jsonObject.getAsJsonObject("references");

                Map<String, Integer> occurrences = new HashMap<>();
                for (Map.Entry<String, JsonElement> entry : references.entrySet()) {
                    String fileName = entry.getKey();
                    int count = entry.getValue().getAsInt();
                    occurrences.put(fileName, count);
                }

                StringBuilder result = new StringBuilder("The word \"" + jword + "\" appears in the books: ");
                for (Map.Entry<String, Integer> entry : occurrences.entrySet()) {
                    result.append("{").append(entry.getKey()).append(": ").append(entry.getValue()).append(" times}\n");
                }

                return result.toString();

            }
        });


        Spark.get("/:word/:startDate/:endDate", (request, response) -> {
            String word = request.params(":word").toLowerCase();
            String startDateStr = request.params(":startDate");
            String endDateStr = request.params(":endDate");

            SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            Date startDate, endDate;
            try {
                startDate = format.parse(startDateStr);
                endDate = format.parse(endDateStr);
            } catch (ParseException e) {
                return "Invalid date format. Please use 'MMMM dd, yyyy'";
            }

            String path = BookAPI.getFilePath(word);

            File file = new File(path);

            if (!file.exists()) {
                return "The word \"" + word + "\" does not appear in any book";
            } else {
                String json = BookAPI.readJsonFromFile(path);
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

                String jword = jsonObject.get("word").getAsString().toLowerCase();
                JsonObject references = jsonObject.getAsJsonObject("references");

                Map<String, Integer> occurrences = new HashMap<>();
                for (Map.Entry<String, JsonElement> entry : references.entrySet()) {
                    String bookName = entry.getKey();
                    int count = entry.getValue().getAsInt();

                    String metadataPath = BookAPI.getMetadataFilePath(bookName);
                    String metadataJson = BookAPI.readJsonFromFile(metadataPath);
                    JsonObject metadataObject = jsonParser.parse(metadataJson).getAsJsonObject();
                    String dateStr = metadataObject.get("publicationDate").getAsString();

                    Date date;
                    try {
                        date = format.parse(dateStr);
                    } catch (ParseException e) {
                        continue;
                    }

                    if ((date.after(startDate) || date.equals(startDate)) && (date.before(endDate) || date.equals(endDate))) {
                        occurrences.put(bookName, count);
                    }
                }

                StringBuilder result = new StringBuilder("The word \"" + jword + "\" appears in the books: ");
                for (Map.Entry<String, Integer> entry : occurrences.entrySet()) {
                    result.append("{").append(entry.getKey()).append(": ").append(entry.getValue()).append(" times}\n");
                }
                return result.toString();
            }
        });

        System.out.println("\nGo to http://localhost:8080/{word} to get information about your word");
    }
}
