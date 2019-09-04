package resources.json

import org.json.*

import java.io.InputStream

import org.apache.commons.io.IOUtils

class ResourceManager {
    /**
     * ReadFile: Metode til at læse en fil fra Resource-mappen til et JSON-objekt
     *
     * @param file .json-fil
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer data fra .json-filen til et JSONObject
     */
    @Throws(JSONException::class)
    fun readFile(file: JSONFile): JSONObject {
        var result = ""

        val classLoader = javaClass.getClassLoader()
        try {
            val fStream = classLoader.getResourceAsStream(file.fileName)
            result = IOUtils.toString(fStream)
        } catch (e: NullPointerException) {
            throw JSONExceptionFileNotFound()
        } catch (e: Exception) {
            e.printStackTrace()
            throw JSONExceptionFileNotFound()
        }

        try {
            return JSONObject(result)
        } catch (e: Exception) {
            throw JSONExceptionFileSyntaxError()
        }

    }
}
