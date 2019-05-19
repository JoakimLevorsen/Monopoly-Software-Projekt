package resources.json;

import org.json.JSONException;

/**
 * JSONExceptionFileNotFound: JSONException hvis en .json-fil ikke kan findes
 * 
 * @author Joakim Bøegh Levorsen, s185023
 */
public class JSONExceptionFileNotFound extends JSONException {
    public static final long serialVersionUID = 1234567893;

    public JSONExceptionFileNotFound() {
        super("File not found");
    }
}