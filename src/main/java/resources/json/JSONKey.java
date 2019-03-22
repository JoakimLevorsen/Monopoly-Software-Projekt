package resources.json;

/*
JSONKey:
Enum der indeholder alle JSON nøgler i projektet, for at undgå stavefejl mens JSON læses.

@author Joakim Levorsen, S185023
*/
public enum JSONKey {
    TEST("test");

    private final String keyValue;

    private JSONKey(String key) {
        this.keyValue = key;
    }

    public String getKey() {
        return this.keyValue;
    }
}
