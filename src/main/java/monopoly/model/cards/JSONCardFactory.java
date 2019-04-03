package monopoly.model.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    public static ArrayList<Card> createChanceCards(JSONObject JSONData, CardStack chanceStack) throws JSONException {
        JSONArray chanceCardData = JSONData.getJSONArray(JSONKey.CHANCE_CARDS.getKey());
        ArrayList<Card> chanceCards = new ArrayList<Card>();

        for (int i = 0; i < JSONData.length(); i++) {
            JSONObject chanceCard = chanceCardData.getJSONObject(i);
            int type = chanceCard.getInt(JSONKey.TYPE.getKey());
            switch (type) {
            case 0:
                chanceCards.add( GoToSpaceCard.create(chanceCard.getString(JSONKey.TEXT.getKey()), chanceCard.getInt(JSONKey.SPACE.getKey())));
                break;
            case 1:
                chanceCards.add(GetLoseMoneyCard.create(chanceCard.getString(JSONKey.TEXT.getKey()), chanceCard.getInt(JSONKey.AMOUNT.getKey())));
                break;
            case 2:
                chanceCards.add(GetOutOfJailCard.create(chanceCard.getString(JSONKey.TEXT.getKey())));
                break;
            case 3:
                chanceCards.add(GoToJailCard.create(chanceCard.getString(JSONKey.TEXT.getKey()), chanceCard.getInt(JSONKey.SPACE.getKey())));
                // TODO: Den her create kører en Create metode, siden det andet parameter er den forkerte type.
                break;
            default:
                throw new JSONException("Unexpected space type: " + type);
            }
        }
        Collections.shuffle(chanceCards);
        for (int i = 0; i < chanceCards.size(); i++) {
            Card card = chanceCards.get(i);
            card.setStackPosition(i);
            chanceStack.add(card);
            card.save();
        }
        return chanceCards;
    }

    public static ArrayList<Card> createCommunityChestCards(JSONObject JSONData, CardStack communityChestStack) throws JSONException {
        JSONArray communityChestCardData = JSONData.getJSONArray(JSONKey.CHANCE_CARDS.getKey());
        ArrayList<Card> communityChestCards = new ArrayList<Card>();

        for (int i = 0; i < JSONData.length(); i++) {
            JSONObject communityChestCard = communityChestCardData.getJSONObject(i);
            int type = communityChestCard.getInt(JSONKey.TYPE.getKey());
            switch (type) {
            case 0:
                communityChestCards.add(GoToSpaceCard.create(communityChestCard.getString(JSONKey.TEXT.getKey()), communityChestCard.getInt(JSONKey.SPACE.getKey())));
                break;
            case 1:
                communityChestCards.add(GetLoseMoneyCard.create(communityChestCard.getString(JSONKey.TEXT.getKey()), communityChestCard.getInt(JSONKey.AMOUNT.getKey())));
                break;
            case 2:
                communityChestCards.add(GetOutOfJailCard.create(communityChestCard.getString(JSONKey.TEXT.getKey())));
                break;
            default:
                throw new JSONException("Unexpected space type: " + type);
            }
        }
        Collections.shuffle(communityChestCards);
        for (int i = 0; i < communityChestCards.size(); i++) {
            Card card = communityChestCards.get(i);
            card.setStackPosition(i);
            communityChestStack.add(card);
            card.save();
        }
        return communityChestCards;
    }
}