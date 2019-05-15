package resources.json;

import org.json.JSONException;

public class JSONExceptionFileNotFound extends JSONException {
    public static final long serialVersionUID = 1234567893;

    public JSONExceptionFileNotFound() {
        super("File not found");
    }
}