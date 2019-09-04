package resources.json

import org.json.JSONException

/**
 * JSONExceptionFileNotFound: JSONException hvis der er fejl i syntaksen i en .json-fil
 *
 * @author Joakim Bøegh Levorsen, s185023
 */
class JSONExceptionFileSyntaxError : JSONException("JSON file has syntax error") {
    companion object {
        val serialVersionUID: Long = 1234567892
    }
}
