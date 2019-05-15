package resources.json;

/*
JSONKey:
Enum der indeholder alle JSON nøgler i projektet, for at undgå stavefejl mens JSON læses.

@author Joakim Levorsen, S185023
*/
public enum JSONKey {
    TEST("test"), START_PAYMENT("startPayment"), CURRENTLY_BROKE("currentlyBroke"),
    PLAYER_BROKE_TITLE("playerBrokeTitle"), SELL_PROPERTY("sellProperty"), MORTGAGE_PROPERTY("mortgageProperty"),
    YOU_HAVE_CHOSEN("youHaveChosen"), WHAT_DO("whatDo"), HOUSES_ON_PROPERTY("housesOnProperty"), SPACES("spaces"),
    NAME("name"), TYPE("type"), PRICE("price"), BASE_RENT("baseRent"), VALUE("value"), COLOR("color"),
    TEXT_COLOR("textColor"), IMAGE("image"), CHANCE_CARDS("chanceCards"), COMMUNITY_CHEST_CARDS("communityChestCards"),
    TEXT("text"), SPACE("space"), AMOUNT("amount"), TAX("tax");

    private final String keyValue;

    private JSONKey(String key) {
        this.keyValue = key;
    }

    public String getKey() {
        return this.keyValue;
    }
}
