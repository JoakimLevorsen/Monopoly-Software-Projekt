package resources.json;

/**
 * JSONFile: Enumeration der indeholder alle JSON filnavne i projektet
 *           Kan også retunere filnavnet for en værdi
 * 
 * @author Joakim Bøegh Levorsen, s185023
 */
public enum JSONFile {
    DA("da"), UK("uk"), US("us");

    private final String value;

    private JSONFile(String value) {
        this.value = value;
    }

    public String getFileName() {
        return this.value + ".json";
    }

    public String getPackName() {
        return this.value;
    }

    public static JSONFile getFile(String name) {
        for (JSONFile file : JSONFile.values()) {
            if (file.getPackName().equals(name))
                return file;
        }
        return null;
    }
}
