package resources.json;

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
