package resources;

public enum JSON_FILE {
    DA("da");

    private final String value;

    private JSON_FILE(String value) {
        this.value = value;
    }

    public String fileName() {
        return "JSON/" + this.value;
    }
}