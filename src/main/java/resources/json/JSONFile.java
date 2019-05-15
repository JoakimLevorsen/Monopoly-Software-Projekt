package resources.json;

/*
JSONFile:
Enum der indeholder alle JSON fil navne i projektet, det kan også retunere filnavet for en værdi.

@author Joakim Levorsen, S185023
*/
public enum JSONFile {
    DA("da"), EN("en"), AM("am");

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
