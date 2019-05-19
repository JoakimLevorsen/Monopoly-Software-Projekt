package resources.json;

import org.json.JSONException;

/**
 * JSONExceptionFileNotFound: JSONException hvis der er fejl i syntaksen i en .json-fil
 * 
 * @author Joakim Bøegh Levorsen, s185023
 */
public class JSONExceptionFileSyntaxError extends JSONException {
    public static final long serialVersionUID = 1234567892;

    public JSONExceptionFileSyntaxError() {
        super("JSON file has syntax error");
    }
}