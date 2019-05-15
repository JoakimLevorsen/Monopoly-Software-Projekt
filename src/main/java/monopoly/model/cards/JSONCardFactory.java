package monopoly.model.cards;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import resources.json.JSONKey;

/*
JSONCardFactory:
Implementering af klasse til oprettelse af kort.

@author Cecilie Krog Drejer, s185032
*/

public class JSONCardFactory {
    public static ArrayList<Card> createChanceCards(JSONObject JSONData, CardStack chanceStack) throws JSONException {
        JSONArray chanceCardData = JSONData.getJSONArray(JSONKey.CHANCE_CARDS.getKey());
        ArrayList<Card> chanceCards = new ArrayList<Card>();

        for (int i = 0; i < chanceCardData.length(); i++) {
            JSONObject chanceCard = chanceCardData.getJSONObject(i);
            int type = chanceCard.getInt(JSONKey.TYPE.getKey());
            switch (type) {
            case 0:
                chanceCards.add(GoToSpaceCard.create(chanceCard.getString(JSONKey.TEXT.getKey()),
                        chanceCard.getInt(JSONKey.SPACE.getKey())));
                break;
            case 1:
                chanceCards.add(GetLoseMoneyCard.create(chanceCard.getString(JSONKey.TEXT.getKey()),
                        chanceCard.getInt(JSONKey.AMOUNT.getKey())));
                break;
            case 2:
                chanceCards.add(GetOutOfJailCard.create(chanceCard.getString(JSONKey.TEXT.getKey())));
                break;
            case 3:
                String text = chanceCard.getString(JSONKey.TEXT.getKey());
                int spaceID = chanceCard.getInt(JSONKey.SPACE.getKey());
                GoToJailCard newCard = GoToJailCard.create(text, spaceID);
                chanceCards.add(newCard);
                break;
            default:
                throw new JSONException("Unexpected space type: " + type);
            }
        }
        Collections.shuffle(chanceCards);
        chanceStack.save();
        for (int i = 0; i < chanceCards.size(); i++) {
            Card card = chanceCards.get(i);
            card.setStackPosition(i);
            chanceStack.add(card);
            card.save();
        }
        return chanceCards;
    }

    public static ArrayList<Card> createCommunityChestCards(JSONObject JSONData, CardStack communityChestStack)
            throws JSONException {
        JSONArray communityChestCardData = JSONData.getJSONArray(JSONKey.COMMUNITY_CHEST_CARDS.getKey());
        ArrayList<Card> communityChestCards = new ArrayList<Card>();

        for (int i = 0; i < communityChestCardData.length(); i++) {
            JSONObject communityChestCard = communityChestCardData.getJSONObject(i);
            int type = communityChestCard.getInt(JSONKey.TYPE.getKey());
            switch (type) {
            case 0:
                communityChestCards.add(GoToSpaceCard.create(communityChestCard.getString(JSONKey.TEXT.getKey()),
                        communityChestCard.getInt(JSONKey.SPACE.getKey())));
                break;
            case 1:
                communityChestCards.add(GetLoseMoneyCard.create(communityChestCard.getString(JSONKey.TEXT.getKey()),
                        communityChestCard.getInt(JSONKey.AMOUNT.getKey())));
                break;
            case 2:
                communityChestCards.add(GetOutOfJailCard.create(communityChestCard.getString(JSONKey.TEXT.getKey())));
                break;
            default:
                throw new JSONException("Unexpected space type: " + type);
            }
        }
        Collections.shuffle(communityChestCards);
        communityChestStack.save();
        for (int i = 0; i < communityChestCards.size(); i++) {
            Card card = communityChestCards.get(i);
            card.setStackPosition(i);
            communityChestStack.add(card);
            card.save();
        }
        return communityChestCards;
    }
}