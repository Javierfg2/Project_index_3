import java.io.File;

public class FolderCreator {

    private static boolean foldersCreated = false;

    private static void createFolders() {
        if (!foldersCreated) {
            File dataLakeFolder = new File("DataLake");
            if (!dataLakeFolder.exists()) {
                dataLakeFolder.mkdirs();
            }

            File booksFolder = new File("DataLake/books");
            if (!booksFolder.exists()) {
                booksFolder.mkdirs();
            }

            foldersCreated = true;
        }
    }

    public static String getFilename(String id) {
        createFolders();

        String destination = "DataLake/books/";

        for (int i = 0; i < 3 && i < id.length(); i++) {
            String digits = id.substring(0, i + 1);
            destination += digits + "/";
            File folder = new File(destination);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }

        destination += id + ".txt";
        return destination;
    }


    public static File getFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            return file;
        } else {
            System.out.println("El archivo no existe.");
            return null;
        }
    }
}
