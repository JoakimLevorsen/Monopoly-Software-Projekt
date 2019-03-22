package resources;

import org.json.*;
import org.apache.commons.io.IOUtils;

public class ResourceManager {
    public JSONObject readFile(JSON_FILE file) {
        file.fileName();

        String result = "";

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}