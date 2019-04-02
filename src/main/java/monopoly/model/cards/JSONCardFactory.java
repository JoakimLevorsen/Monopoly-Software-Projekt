package monopoly.model.cards;

import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import monopoly.model.*;
import resources.json.JSONKey;

/*
JSONCardFactory:
Implementering af klasse til oprettelse af kort.

@author Cecilie Krog Drejer, s185032
*/

public class JSONCardFactory {
    public static Card[] createChanceCards(JSONObject JSONData, Game game) throws JSONException {
        JSONArray chanceCardData = JSONData.getJSONArray(JSONKey.CHANCE_CARDS.getKey());
        Card[] chanceCards = new Card[chanceCardData.length()];

        for (int i = 0; i < JSONData.length(); i++) {
            JSONObject chanceCard = chanceCardData.getJSONObject(i);
            int type = chanceCard.getInt(JSONKey.TYPE.getKey());
            switch (type) {
            case 0:
                chanceCards[i] = GoToSpaceCard.create(chanceCard.getString(JSONKey.TEXT.getKey()), chanceCard.getInt(JSONKey.SPACE.getKey()));
                break;
            case 1:
                chanceCards[i] = GetLoseMoneyCard.create(chanceCard.getString(JSONKey.TEXT.getKey()), chanceCard.getInt(JSONKey.AMOUNT.getKey()));
                break;
            case 2:
                chanceCards[i] = GetOutOfJailCard.create(chanceCard.getString(JSONKey.TEXT.getKey()));
                break;
            case 3:
                chanceCards[i] = GoToJailCard.create(chanceCard.getString(JSONKey.TEXT.getKey()), chanceCard.getInt(JSONKey.SPACE.getKey()));
                break;
            default:
                throw new JSONException("Unexpected space type: " + type);
            }
        }
        for (Card card : chanceCards) {
            game.add(card);
            card.save();
        }
        return chanceCards;
    }

    public static Card[] createCommunityChestCards(JSONObject JSONData, Game game) throws JSONException {
        JSONArray communityChestCardData = JSONData.getJSONArray(JSONKey.CHANCE_CARDS.getKey());
        Card[] communityChestCards = new Card[communityChestCardData.length()];

        for (int i = 0; i < JSONData.length(); i++) {
            JSONObject communityChestCard = communityChestCardData.getJSONObject(i);
            int type = communityChestCard.getInt(JSONKey.TYPE.getKey());
            switch (type) {
            case 0:
                communityChestCards[i] = GoToSpaceCard.create(communityChestCard.getString(JSONKey.TEXT.getKey()), communityChestCard.getInt(JSONKey.SPACE.getKey()));
                break;
            case 1:
                communityChestCards[i] = GetLoseMoneyCard.create(communityChestCard.getString(JSONKey.TEXT.getKey()), communityChestCard.getInt(JSONKey.AMOUNT.getKey()));
                break;
            case 2:
                communityChestCards[i] = GetOutOfJailCard.create(communityChestCard.getString(JSONKey.TEXT.getKey()));
                break;
            default:
                throw new JSONException("Unexpected space type: " + type);
            }
        }
        for (Card card : communityChestCards) {
            game.add(card);
            card.save();
        }
        return communityChestCards;
    }
}