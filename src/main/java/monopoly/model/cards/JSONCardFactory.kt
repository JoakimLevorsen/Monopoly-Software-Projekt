package monopoly.model.cards

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import resources.json.JSONKey

import java.util.ArrayList
import java.util.Collections

/**
 * JSONCardFactory: Et objekt til at hente alle start felter fra vores JSON resourcer
 *
 * @author Cecilie Krog Drejer, s185032
 * @author Joakim Bøegh Levorsen, s185023
 */

object JSONCardFactory {
    /**
     * CreateChanceCards: Opretter Chancekort ud fra data fra JSON
     *
     * @param JSONData Dataobjekt fra JSON
     * @param chanceStack Kortbunke, som kortene skal tilhøre
     *
     * @author Cecilie Krog Drejer, s185032
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer et ArrayList af Cards
     *
     * @throws JSONException
     */
    @Throws(JSONException::class)
    fun createChanceCards(JSONData: JSONObject, chanceStack: CardStack): ArrayList<Card> {
        val chanceCardData = JSONData.getJSONArray(JSONKey.CHANCE_CARDS.key)
        val chanceCards = ArrayList<Card>()

        for (i in 0 until chanceCardData.length()) {
            val chanceCard = chanceCardData.getJSONObject(i)
            val type = chanceCard.getInt(JSONKey.TYPE.key)
            when (type) {
                0 -> chanceCards.add(GoToSpaceCard.create(chanceCard.getString(JSONKey.TEXT.key),
                        chanceCard.getInt(JSONKey.SPACE.key)))
                1 -> chanceCards.add(GetLoseMoneyCard.create(chanceCard.getString(JSONKey.TEXT.key),
                        chanceCard.getInt(JSONKey.AMOUNT.key)))
                2 -> chanceCards.add(GetOutOfJailCard.create(chanceCard.getString(JSONKey.TEXT.key)))
                3 -> {
                    val text = chanceCard.getString(JSONKey.TEXT.key)
                    val spaceID = chanceCard.getInt(JSONKey.SPACE.key)
                    val newCard = GoToJailCard.create(text, spaceID)
                    chanceCards.add(newCard)
                }
                else -> throw JSONException("Unexpected space type: $type")
            }
        }
        Collections.shuffle(chanceCards)
        chanceStack.save()
        for (i in chanceCards.indices) {
            val card = chanceCards[i]
            card.stackPosition = i
            chanceStack.add(card)
            card.save()
        }
        return chanceCards
    }

    /**
     * CreateCommunityChestCards: Opretter Prøv Lykken-kort ud fra data fra JSON
     *
     * @param JSONData Dataobjekt fra JSON
     * @param communityChestStack Kortbunke, som kortene skal tilhøre
     *
     * @author Cecilie Krog Drejer, s185032
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer et ArrayList af Cards
     *
     * @throws JSONException
     */
    @Throws(JSONException::class)
    fun createCommunityChestCards(JSONData: JSONObject, communityChestStack: CardStack): ArrayList<Card> {
        val communityChestCardData = JSONData.getJSONArray(JSONKey.COMMUNITY_CHEST_CARDS.key)
        val communityChestCards = ArrayList<Card>()

        for (i in 0 until communityChestCardData.length()) {
            val communityChestCard = communityChestCardData.getJSONObject(i)
            val type = communityChestCard.getInt(JSONKey.TYPE.key)
            when (type) {
                0 -> communityChestCards.add(GoToSpaceCard.create(communityChestCard.getString(JSONKey.TEXT.key),
                        communityChestCard.getInt(JSONKey.SPACE.key)))
                1 -> communityChestCards.add(GetLoseMoneyCard.create(communityChestCard.getString(JSONKey.TEXT.key),
                        communityChestCard.getInt(JSONKey.AMOUNT.key)))
                2 -> communityChestCards.add(GetOutOfJailCard.create(communityChestCard.getString(JSONKey.TEXT.key)))
                else -> throw JSONException("Unexpected space type: $type")
            }
        }
        Collections.shuffle(communityChestCards)
        communityChestStack.save()
        for (i in communityChestCards.indices) {
            val card = communityChestCards[i]
            card.stackPosition = i
            communityChestStack.add(card)
            card.save()
        }
        return communityChestCards
    }
}
