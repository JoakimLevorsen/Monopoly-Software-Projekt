package resources.json;

import org.json.*;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class ResourceManager {
    /*
     * readFile Metode til at læse en fil fra Resource mappen til et JSON objekt.
     * 
     * @author Joakim Levorsen, S185023
     */
    public JSONObject readFile(JSONFile file) throws JSONException {
        String result = "";

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            InputStream fStream = classLoader.getResourceAsStream(file.getFileName());
            result = IOUtils.toString(fStream);
        } catch (NullPointerException e) {
            throw new JSONExceptionFileNotFound();
        } catch (Exception e) {
            e.printStackTrace();
            throw new JSONExceptionFileNotFound();
        }

        try {
            return new JSONObject(result);
        } catch (Exception e) {
            throw new JSONExceptionFileSyntaxError();
        }
    }
}
