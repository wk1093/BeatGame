package beats.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
    public static String readFile(String filePath) {
        String result = "";
        try {
            result = Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
