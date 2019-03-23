package resources;

import org.junit.Test;

import resources.json.*;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.Stack;
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
                fail("File named " + file.getFileName() + " not found");
                e.printStackTrace();
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
                fail("File named " + file.getFileName() + " not found");
                e.printStackTrace();
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

    /*
     * testAllFilesLayoutIdentical: Tjekker om layoutet af JSON filer er identisk,
     * dvs at alle JSON filer har de samme nøgler. Den tjekker IKKE array indhold,
     * eller typerne af værdierne ved nøgler(Med untagelse af hvis værdien er at
     * objekt, så tjekkes den).
     * 
     * @author Joakim Levorsen, S185023
     */
    @Test public void testAllFilesLayoutIdentical() {
        ResourceManager manager = new ResourceManager();
        Stack<JSONObject> allResourceFiles = new Stack<>();
        for (JSONFile file: JSONFile.values()) {
            try {
                allResourceFiles.push(manager.readFile(file));
            } catch (JSONException e) {
                fail("Could not read file " + file.getFileName());
            }
        }
        // If there´s only one file, it must match itself.
        if (allResourceFiles.size() > 1) {
            // This works by verifying all files match the first one, since if everyone matches the first one, they all must match
            JSONObject first = allResourceFiles.pop();
            while (allResourceFiles.size() > 0) {
                JSONObject compareTo = allResourceFiles.pop();
                if (!keysMatch(first, compareTo)) {
                    fail("Objects do not match key layout");
                }
            }
        }
    }

    /*
     * keysMatch: Tjekker to JSON objekter har de samme nøgler (minus arrays).
     * 
     * @author Joakim Levorsen, S185023
     */
    private boolean keysMatch(JSONObject a, JSONObject b) {
        // First check all keys from a exist in b
        Iterator aKeys = a.keys();
        Set bKeySet = b.keySet();
        while (aKeys.hasNext()) {
            String key = (String)aKeys.next();
            if (!bKeySet.contains(key)) return false;
            if (a.get(key) instanceof JSONObject) {
                // First we verify b also has an object at this key, then we compare the deeper objects.
                if (b.get(key) instanceof JSONObject) {
                    if (!keysMatch((JSONObject) a.get(key), (JSONObject) b.get(key))) return false;
                } else {
                    return false;
                }
            }
        }
        // Then all b keys exist in a
        Iterator bKeys = b.keys();
        Set aKeySet = a.keySet();
        while (bKeys.hasNext()) {
            String key = (String)bKeys.next();
            if (!aKeySet.contains(key)) return false;
            if (b.get(key) instanceof JSONObject) {
                // First we verify b also has an object at this key, then we compare the deeper objects.
                if (a.get(key) instanceof JSONObject) {
                    if (!keysMatch((JSONObject) b.get(key), (JSONObject) a.get(key))) return false;
                } else {
                    return false;
                }
            }
        }
        // Now everything has been compared without returning false, so the objects keys must match
        return true;
    }
}
