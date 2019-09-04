package resources.json

import org.json.JSONException

/**
 * JSONExceptionFileNotFound: JSONException hvis en .json-fil ikke kan findes
 *
 * @author Joakim Bøegh Levorsen, s185023
 */
class JSONExceptionFileNotFound : JSONException("File not found") {
    companion object {
        val serialVersionUID: Long = 1234567893
    }
}
