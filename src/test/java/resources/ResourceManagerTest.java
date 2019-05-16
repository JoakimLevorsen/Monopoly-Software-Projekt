package resources;

import org.junit.Test;

import resources.json.*;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.Stack;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                // Test for alle keys i denne fil
                for (JSONKey key : JSONKey.values()) {
                    assertTrue("Key " + key + " was not found in " + file, objectContainsKey(jFile, key));
                }
            } catch (JSONException e) {
                fail("File named " + file.getFileName() + " not found");
                e.printStackTrace();
            }
        }

    }

    /*
     * objectContainsKey: Tjekker om nøgle findes mindst en gang i et array eller
     * objekt.
     * 
     * @author Joakim Levorsen, S185023
     */
    public boolean objectContainsKey(JSONArray object, JSONKey key) {
        for (Object child : object) {
            if (child instanceof JSONArray) {
                if (objectContainsKey((JSONArray) child, key))
                    return true;
            }
            if (child instanceof JSONObject) {
                if (objectContainsKey((JSONObject) child, key))
                    return true;
            }
        }
        return false;
    }

    public boolean objectContainsKey(JSONObject object, JSONKey key) {
        for (String childKey : object.keySet()) {
            if (childKey.equals("spaces") && key.getKey().equals("price")) {
                System.out.println("Ayyy");
            } else {
                System.out.println(key.getKey() + " in " + childKey);
            }
            if (childKey.equals(key.getKey()))
                return true;
            Object atKey = object.get(childKey);
            if (atKey instanceof JSONArray) {
                if (objectContainsKey((JSONArray) atKey, key))
                    return true;
            }
            if (atKey instanceof JSONObject) {
                if (objectContainsKey((JSONObject) atKey, key))
                    return true;
            }
        }
        return false;
    }

    /*
     * keyExists: Kode taget fra StackOverflow, til at undersøge rekusivt om et
     * JSONObject indeholder en nøgle mindst en gang. Original kilde:
     * https://stackoverflow.com/questions/31043606/check-whether-a-key-exists-or-
     * not-in-a-nested-json/31044236
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
                if (object.get(key) instanceof JSONArray) {
                    exists = keyExists((JSONArray) object.get(key), searchedKey);
                }
            }
        }
        return exists;
    }

    /*
     * keyExists: Kør key exists på alle elementer i JSONArray
     * 
     * @author Joakim Levorsen, S185023
     */

    private boolean keyExists(JSONArray array, String searchedKey) {
        for (int i = 0; i < array.length(); i++) {
            Object item = array.get(i);
            if (item instanceof JSONObject && keyExists((JSONObject) item, searchedKey)) {
                return true;
            }
            if (item instanceof JSONArray && keyExists((JSONArray) item, searchedKey)) {
                return true;
            }
        }
        return false;
    }

    /*
     * testAllFilesLayoutIdentical: Tjekker om layoutet af JSON filer er identisk,
     * dvs at alle JSON filer har de samme nøgler. Den tjekker IKKE typerne af
     * værdierne ved nøgler(Med untagelse af hvis værdien er at objekt eller array,
     * så tjekkes den).
     * 
     * @author Joakim Levorsen, S185023
     */
    @Test
    public void testAllFilesLayoutIdentical() {
        ResourceManager manager = new ResourceManager();
        Stack<JSONObject> allResourceFiles = new Stack<>();
        for (JSONFile file : JSONFile.values()) {
            try {
                allResourceFiles.push(manager.readFile(file));
            } catch (JSONException e) {
                fail("Could not read file " + file.getFileName());
            }
        }
        // If there´s only one file, it must match itself.
        if (allResourceFiles.size() > 1) {
            // This works by verifying all files match the first one, since if everyone
            // matches the first one, they all must match
            JSONObject first = allResourceFiles.pop();
            int checkIndex = 1;
            while (allResourceFiles.size() > 0) {
                JSONObject compareTo = allResourceFiles.pop();
                if (!keysMatch(first, compareTo)) {
                    fail("Objects do not match key layout, compared " + JSONFile.values()[0].getFileName() + " to "
                            + JSONFile.values()[checkIndex].getFileName());
                }
                checkIndex++;
            }
        }
    }

    /*
     * keysMatch: Tjekker to JSON objekter har de samme nøgler.
     * 
     * @author Joakim Levorsen, S185023
     */
    private boolean keysMatch(JSONObject a, JSONObject b) {
        // First check all keys from a exist in b
        Iterator<String> aKeys = a.keys();
        Set<String> bKeySet = b.keySet();
        while (aKeys.hasNext()) {
            String key = (String) aKeys.next();
            if (!bKeySet.contains(key)) {
                System.out.println(key + " found in object a but not on object a");
                return false;
            }
            ;
            if (!compareObjectsFromJSON(a.get(key), b.get(key)))
                return false;
        }
        // Then all b keys exist in a
        Iterator<String> bKeys = b.keys();
        Set<String> aKeySet = a.keySet();
        while (bKeys.hasNext()) {
            String key = (String) bKeys.next();
            if (!aKeySet.contains(key))
                return false;
            if (!compareObjectsFromJSON(b.get(key), a.get(key)))
                return false;
        }
        // Now everything has been compared without returning false, so the objects keys
        // must match
        return true;
    }

    /*
     * arrayMatch: Sammenlign to JSONArrays, og deres værdier.
     * 
     * @author Joakim Levorsen, S185023
     */
    private boolean arrayMatch(JSONArray a, JSONArray b) {
        if (a.length() != b.length()) {
            System.out.println("Arrays not equal length");
            return false;
        }
        for (int i = 0; i < a.length(); i++) {
            if (!compareObjectsFromJSON(a.get(i), b.get(i))) {
                System.out.println("Array comparison failed at index " + i);
                return false;
            }
        }
        return true;
    }

    /*
     * compareObjectFromJSON: Sammenlign to Objekt typer, om de er dybere JSON
     * objeckter, og så videre om de stemmer overens.
     * 
     * @author Joakim Levorsen, S185023
     */
    private boolean compareObjectsFromJSON(Object a, Object b) {
        if (a instanceof JSONObject) {
            // First we verify b also has an object at this key, then we compare the deeper
            // objects.
            if (b instanceof JSONObject) {
                if (!keysMatch((JSONObject) a, (JSONObject) b))
                    return false;
            } else
                return false;
        } else if (a instanceof JSONArray) {
            if (b instanceof JSONArray) {
                if (!arrayMatch((JSONArray) a, (JSONArray) b))
                    return false;
            } else
                return false;
        }
        return true;
    }
}
