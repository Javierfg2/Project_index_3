import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import spark.Spark;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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

        System.out.println("\nGo to http://localhost:8080/{word} to get information about your word");
    }
}
