import java.io.File;

public class FolderCreator {

    private static boolean foldersCreated = false;

    private static void createFolders() {
        if (!foldersCreated) {
            File datamartFolder = new File("Datamart");
            if (!datamartFolder.exists()) {
                datamartFolder.mkdirs();
            }

            File metadataFolder = new File("Datamart/metadata/");
            if (!metadataFolder.exists()) {
                metadataFolder.mkdirs();
            }

            File contentFolder = new File("Datamart/content/");
            if (!contentFolder.exists()) {
                contentFolder.mkdirs();
            }

            foldersCreated = true;
        }
    }

    public static String getFilename(String id, String fileType) {
        createFolders();

        String destination = "Datamart/";
        switch (fileType) {
            case "metadata" -> destination += "metadata/";
            case "content" -> destination += "content/";
        }

        for (int i = 0; i < 3 && i < id.length(); i++) {
            String digits = id.substring(0, i + 1);
            destination += digits + "/";
            File folder = new File(destination);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }
        if (fileType.equals("metadata")) {
            destination += id + ".json";
        } else {
            destination += id + ".txt";
        }
        return destination;
    }
}