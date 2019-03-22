package resources.json;

/*
JSONFile:
Enum der indeholder alle JSON fil navne i projektet, det kan også retunere filnavet for en værdi.

@author Joakim Levorsen, S185023
*/
public enum JSONFile {
    DA("da");

    private final String value;

    private JSONFile(String value) {
        this.value = value;
    }

    public String getFileName() {
        return this.value + ".json";
    }
}
