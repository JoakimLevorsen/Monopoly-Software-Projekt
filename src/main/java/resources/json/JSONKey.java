package resources.json;

/*
JSONKey:
Enum der indeholder alle JSON nøgler i projektet, for at undgå stavefejl mens JSON læses.

@author Joakim Levorsen, S185023
*/
public enum JSONKey {
    TEST("test"), SPACES("spaces"), NAME("name"), TYPE("type"), START_PAYMENT("startPayment"), PRICE("price"),
    BASE_RENT("baseRent"), VALUE("value"), COLOR("color"), TEXT_COLOR("textColor"), IMAGE("image"),
    CHANCE_CARDS("chanceCards"), COMMUNITY_CHEST_CARDS("communityChestCards"), TEXT("text"), SPACE("space"),
    AMOUNT("amount");

    private final String keyValue;

    private JSONKey(String key) {
        this.keyValue = key;
    }

    public String getKey() {
        return this.keyValue;
    }
}
