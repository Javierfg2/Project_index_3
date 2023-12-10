import java.io.*;

public class ContentExtractor implements Extractor {

    @Override
    public String extractData(String bookFilePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(bookFilePath));

            boolean foundFirstOccurrence = false;
            StringBuilder contentWithoutMetadata = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("*** START OF ") && !foundFirstOccurrence) {
                    foundFirstOccurrence = true;
                } else if (foundFirstOccurrence) {
                    if (line.contains("*** END OF ")) {
                        break;
                    }
                    contentWithoutMetadata.append(line).append("\n");
                }
            }

            reader.close();
            return contentWithoutMetadata.toString();

        } catch (IOException e) {
            System.out.println("\nError: failed to process the file " + bookFilePath + ".");
            return null;
        }
    }

    @Override
    public void storeData(String content, String bookFilePath) throws IOException {
        String id = bookFilePath.substring(bookFilePath.lastIndexOf("/") + 1, bookFilePath.lastIndexOf("."));
        String filename = FolderCreator.getFilename(id, "content");

        BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
        writer.write(content);
        writer.close();
        System.out.println("Content stored in the file: " + filename);
    }
}