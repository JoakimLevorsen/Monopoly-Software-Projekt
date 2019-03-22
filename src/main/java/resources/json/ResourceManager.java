package resources.json;

import org.json.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class ResourceManager {
    public JSONObject readFile(JSONFile file) throws JSONException {
        String result = "";

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            System.out.println(classLoader.getResource(file.getFileName()));
            InputStream fStream = classLoader.getResourceAsStream(file.getFileName());
            result = IOUtils.toString(fStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            return new JSONObject(result);
        } catch (Exception e) {
            throw new JSONExceptionFileSyntaxError();
        }
    }
}
