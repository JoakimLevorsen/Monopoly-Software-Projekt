package resources.json;

/*
JSONKey:
Enum der indeholder alle JSON nøgler i projektet, for at undgå stavefejl mens JSON læses.

@author Joakim Levorsen, S185023
*/
public enum JSONKey {
    TEST("test"), START_PAYMENT("startPayment"), CURRENTLY_BROKE("currentlyBroke"), PLAYER_BROKE_TITLE("playerBrokeTitle"), SELL_PROPERTY("sellProperty"), MORTGAGE_PROPERTY("mortgageProperty"), WHAT_DO("whatDo"), HOUSES_ON_PROPERTY_CHOOSE_HOUSES("housesOnPropertyChooseHouses"), HOUSES_ON_PROPERTY_SELL_ALL_HOUSES("housesOnPropertySellAllHouses"), SPACES("spaces"), 
    NAME("name"), TYPE("type"), PRICE("price"),BASE_RENT("baseRent"), VALUE("value"), COLOR("color"), TEXT_COLOR("textColor"), IMAGE("image"), CHANCE_CARDS("chanceCards"), COMMUNITY_CHEST_CARDS("communityChestCards"), TEXT("text"), SPACE("space"), AMOUNT("amount"), TAX("tax"), ROLLED_DOUBLE("rolledDouble"), OUT_OF_JAIL("OutOfJail"), YES("yes"), NO("no"), BALANCE("balance"), PLAYER_IS_IN_JAIL("playerIsInJail"), HOUSES_BUILT ("housesBuilt"), RENT ("rent"), MORTGAGE_BY("mortgageBY"), OWNED_BY("ownedby"), AUCTIONED_OFF("auctionedOff"), BID("bid"), BID_NOT_VALID("bidNotValid"), WINNER("winner"), OFFER_PROPERTY("offerProperty"), PROPERTY_COST("propertyCost"), BOUGHT_PROPERTY("boughtProperty"), TRADE("trade"), BUY_OR_SELL_PROPERTY("buyOrSellProperty"), BUY_PROPERTY("buyProperty"), CHOOSE_PROPERTY("chooseProperty"), CHOOSE_PLAYER("choosePlayer"), AMOUNT_TO_PAY("amountToPay"), ACCEPT("accept"), MAX_BUILT_HOUSES("maxBuiltHouses"), CANT_BUILD_ANYMORE("cantBuildAnymore"), BUILD_ON_PROPERTY("buildOnYourProperty"), WHICH_PROPERTY_TO_BUILD_ON("whichPropertyToBuildOn"), ROLL_DICE("rollDice");

    private final String keyValue;

    private JSONKey(String key) {
        this.keyValue = key;
    }

    public String getKey() {
        return this.keyValue;
    }
}
