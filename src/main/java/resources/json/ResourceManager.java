package resources.json;

import org.json.*;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class ResourceManager {
    /**
     * ReadFile: Metode til at læse en fil fra Resource-mappen til et JSON-objekt
     * 
     * @param file .json-fil
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer data fra .json-filen til et JSONObject
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
