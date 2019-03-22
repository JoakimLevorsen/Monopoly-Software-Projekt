package resources;

import org.junit.Test;

import resources.json.*;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.json.JSONObject;

/**
 * Unit test for simple App.
 */
public class ResourceManagerTest {
    /*
     * testAllFilesPresent: Tjekker om alle filer nævnt i JSONFile enum´et faktisk
     * findes i projektet.
     * 
     * @author Joakim Levorsen, S185023
     */
    @Test
    public void testAllFilesPresent() {
        ResourceManager manager = new ResourceManager();
        for (JSONFile file : JSONFile.values()) {
            try {
                manager.readFile(file);
            } catch (JSONException e) {
                e.printStackTrace();
                fail("File named " + file.getFileName() + " not found");
            }
        }
    }

    /*
     * testAllKeysPresent: Tjekker om alle nøgler nævnt i JSONKey findes mindst en
     * gang i alle JSON filer nævnt i JSONFile
     * 
     * @author Joakim Levorsen, S185023
     */
    @Test
    public void testAllKeysPresent() {
        ResourceManager manager = new ResourceManager();
        for (JSONFile file : JSONFile.values()) {
            try {
                JSONObject jFile = manager.readFile(file);
                for (JSONKey key : JSONKey.values()) {
                    assertTrue("Key " + key + " was not found in " + file, keyExists(jFile, key.getKey()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                fail("File named " + file.getFileName() + " not found");
            }
        }

    }

    /*
     * keyExists: Kode taget fra StackOverflow, til at undersøge rekusivt om et
     * JSONObject indeholder en nøgle mindst en gang. Original kilde:
     * https://stackoverflow.com/questions/31043606/check-whether-a-key-exists-or-not-in-a-nested-json/31044236
     * 
     * @author Joakim Levorsen, S185023
     */
    private boolean keyExists(JSONObject object, String searchedKey) {
        boolean exists = object.has(searchedKey);
        if (!exists) {
            Iterator<?> keys = object.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (object.get(key) instanceof JSONObject) {
                    exists = keyExists((JSONObject) object.get(key), searchedKey);
                }
            }
        }
        return exists;
    }
}
