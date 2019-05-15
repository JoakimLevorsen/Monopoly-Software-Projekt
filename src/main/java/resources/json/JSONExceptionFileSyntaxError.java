package resources.json;

import org.json.JSONException;

public class JSONExceptionFileSyntaxError extends JSONException {
    public static final long serialVersionUID = 1234567892;

    public JSONExceptionFileSyntaxError() {
        super("JSON file has syntax error");
    }
}