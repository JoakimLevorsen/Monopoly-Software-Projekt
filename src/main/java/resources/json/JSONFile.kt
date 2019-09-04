package resources.json

/**
 * JSONFile: Enumeration der indeholder alle JSON filnavne i projektet
 * Kan også retunere filnavnet for en værdi
 *
 * @author Joakim Bøegh Levorsen, s185023
 */
enum class JSONFile private constructor(val packName: String) {
    DA("da"), UK("uk"), US("us");

    val fileName: String
        get() = this.packName + ".json"

    companion object {

        fun getFile(name: String): JSONFile? {
            for (file in JSONFile.values()) {
                if (file.packName == name)
                    return file
            }
            return null
        }
    }
}
