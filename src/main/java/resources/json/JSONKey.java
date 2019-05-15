package resources.json;

/*
JSONKey:
Enum der indeholder alle JSON nøgler i projektet, for at undgå stavefejl mens JSON læses.

@author Joakim Levorsen, S185023
*/
public enum JSONKey {
    TEST("test"), START_PAYMENT("startPayment"), CURRENTLY_BROKE("currentlyBroke"), PLAYER_BROKE_TITLE("playerBrokeTitle"), SELL_PROPERTY("sellProperty"), 
    MORTGAGE_PROPERTY("mortgageProperty"), YOU_HAVE_CHOSEN("youHaveChosen"), WHAT_DO("whatDo"), HOUSES_ON_PROPERTY("housesOnProperty"), SPACES("spaces"), 
    NAME("name"), TYPE("type"), PRICE("price"),BASE_RENT("baseRent"), VALUE("value"), COLOR("color"), TEXT_COLOR("textColor"), IMAGE("image"),
    CHANCE_CARDS("chanceCards"), COMMUNITY_CHEST_CARDS("communityChestCards"), TEXT("text"), SPACE("space"),
    AMOUNT("amount"), TAX("tax"), ROLLED_DOUBLE("rolledDouble"), OUT_OF_JAIL("OutOfJail"), YES("yes)"), NO("no"), BALANCE("balance"), PLAYER_IS_IN_JAIL("playerIsInJail"),
    HOUSES_BUILT ("housesBuilt"), RENT ("rent"), MORTGAGE_BY("mortgageBY"), 
    OWNED_BY("ownedby"),
    STATION ("station"), 
    AUCTIONED_OFF("auctionedOff"), 
    BID("bid"), 
    ANSWER_TO_BID ("answerToBid"), 
    BID_NOT_VALID("bidNotValid"), 
    PLAYER ("Player"), 
    WINNER("winner"), 
    OFFER_PROPERTY("offerProperty"), 
    PROPERTY_COST("propertyCost"), 
    BOUGHT_PROPERTY("boughtProperty"), 
    PROPERTY ("property"), 
    SELL_PROPERTY_TO_BANK("sellPropertyToBank"), 
    TRADE_WTIH_ANOTHER_PLAYER("tradeWithAnotherPlayer"), BUY_OR_SELL_PROPERTY("buyOrSellProperty"), 
    BUY_PROPERTY("buyProperty"), WHICH_PROPERTY_DO_YOU_WANT("whichPropertyDoYouWantToSell"), 
    CANT_SELL_PROPERTY("cantSellProperty"), 
    CHOOSE_PLAYER_TO_SELL("choosePlayerToSell"),
    AMOUNT_TO_PAY_FOR_PROPERTY("amountToPayForProperty"), 
    MAX_BUILT_HOUSES("maxBuiltHouses"),
    TO_BUILD_MORE("toBuildMore"), 
     CANT_BUILD_ANYMORE("cantBuildAnymore"), 
     BUILD_ON_PROPERTY("buildOnYourProperty")



    ; 

    private final String keyValue;

    private JSONKey(String key) {
        this.keyValue = key;
    }

    public String getKey() {
        return this.keyValue;
    }
}
