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
    /**
     * Rigorous Test.
     */
    @Test
    public void testAllFilesPresent() {
        // This test ensures all files declared are present in the project.
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

    @Test
    public void testAllKeysPresent() {
        // This test ensures all keys actually exist, to avoid clutter
        // It also tests all files contain the key, to ensure no files are missing
        // NOTE only identifies if more than one match exists
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
