import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class FileIndexer {

    public FileIndexer(File file, String datamart) {
        String[] words = Utils.fileCleaner(file);
        Set<String> usualWords = new HashSet<>();

        for (String word : words) {
            word = word.replace(" ", "");

            if (word.length() >= 3 && !usualWords.contains(word) && !Pattern.matches("^(nul|con|aux|prn).*", word)) {
                String fileName = word + ".txt";
                String route = datamart + "/" + word.substring(0, 1) + "/" + word.substring(0, 2) + "/"
                        + word.substring(0, 3) + "/" + fileName;
                usualWords.add(word);

                routeManager(route, file, word);
            }
        }
    }

    public void fileCreator(File file, String route, String word) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(route, true))) {
            bw.write("{\"word\": \"" + word + "\", \"references\": [\"" + file.getName() + "\"]}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void routeManager(String route, File file, String word) {
        File wordFile = new File(route);

        if (wordFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(route))) {
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                int bracketIndex = bracketTracker(sb);
                sb.insert(bracketIndex, ", \"" + file.getName() + "\"");

                try (BufferedWriter bw = new BufferedWriter(new FileWriter(route))) {
                    bw.write(sb.toString());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            File directory = wordFile.getParentFile();

            if (!directory.exists()) {
                directory.mkdirs();
            }

            fileCreator(file, route, word);
        }
    }

    private int bracketTracker(StringBuilder sb) {
        int i = 0;

        for (char element : sb.toString().toCharArray()) {
            if (element == ']') {
                break;
            }
            i++;
        }
        return i;
    }
}
