package monopoly.model.spaces

import monopoly.model.Game
import monopoly.model.cards.CardStack
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import resources.json.JSONKey

/**
 * JSONSpaceFactory: Et objekt til at hente alle start felter fra vores JSON resourcer
 *
 * @author Joakim Bøegh Levorsen, s185023
 */
object JSONSpaceFactory {
    /**
     * CreateSpaces: Opretter felter ud fra data fra JSON
     *
     * @param boardData Dataobjekt fra JSON
     * @param game Spillet, som felterne tilhører
     * @param chanceStack Kortbunke med Chancekort
     * @param communityStack Kortbunke med Prøv Lykken-kort
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer et spillebræt i form af et array af Spaces
     *
     * @throws JSONException
     */
    @Throws(JSONException::class)
    fun createSpaces(boardData: JSONObject, game: Game, chanceStack: CardStack, communityStack: CardStack): Array<Space> {
        val spaceData = boardData.getJSONArray(JSONKey.SPACES.key)
        val resultSet = arrayOfNulls<Space>(spaceData.length())
        val startPayment = boardData.getInt(JSONKey.START_PAYMENT.key)

        for (i in 0 until spaceData.length()) {
            val space = spaceData.get(i) as JSONObject
            val type = space.getInt(JSONKey.TYPE.key)
            when (type) {
                0 -> resultSet[i] = StartSpace.create(i, startPayment, space.getString(JSONKey.NAME.key),
                        space.getString(JSONKey.COLOR.key))
                1 -> resultSet[i] = PropertySpace.create(i, space.getString(JSONKey.NAME.key),
                        space.getInt(JSONKey.PRICE.key), space.getInt(JSONKey.BASE_RENT.key),
                        space.getString(JSONKey.COLOR.key))
                2 -> resultSet[i] = StationSpace.create(i, space.getString(JSONKey.NAME.key),
                        space.getInt(JSONKey.PRICE.key), space.getInt(JSONKey.BASE_RENT.key),
                        space.getString(JSONKey.COLOR.key))
                3 -> resultSet[i] = FreeParkingSpace.create(i, space.getString(JSONKey.NAME.key),
                        space.getString(JSONKey.COLOR.key))
                4 -> resultSet[i] = JailSpace.create(i, space.getString(JSONKey.NAME.key),
                        space.getString(JSONKey.COLOR.key))
                5 -> resultSet[i] = GoToJailSpace.create(i, space.getString(JSONKey.NAME.key),
                        space.getString(JSONKey.COLOR.key))
                6, 7 -> resultSet[i] = CardSpace.create(i, if (type == 6) chanceStack else communityStack,
                        space.getString(JSONKey.NAME.key), space.getString(JSONKey.COLOR.key))
                8 -> resultSet[i] = TaxSpace.create(i, space.getInt(JSONKey.TAX.key),
                        space.getString(JSONKey.NAME.key), space.getString(JSONKey.COLOR.key))
                else -> throw JSONException("Unexpected space type $type")
            }
        }
        for (space in resultSet) {
            game.add(space)
            space?.save()
        }
        return resultSet as Array<Space>
    }
}
