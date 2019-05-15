package monopoly.model.spaces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import monopoly.model.*;
import monopoly.model.cards.CardStack;
import resources.json.JSONKey;

/*
JSONSpaceFactory:
Et objekt til at hente alle start felter fra vores JSON resourcer.

@author Joakim Levorsen, S185023
*/
public class JSONSpaceFactory {
    public static Space[] createSpaces(JSONObject boardData, Game game, CardStack chanceStack, CardStack communityStack)
            throws JSONException {
        JSONArray spaceData = boardData.getJSONArray(JSONKey.SPACES.getKey());
        Space[] resultSet = new Space[spaceData.length()];
        int startPayment = boardData.getInt(JSONKey.START_PAYMENT.getKey());

        for (int i = 0; i < spaceData.length(); i++) {
            JSONObject space = (JSONObject) spaceData.get(i);
            int type = space.getInt(JSONKey.TYPE.getKey());
            switch (type) {
            case 0:
                resultSet[i] = StartSpace.create(i, startPayment);
                break;
            case 1:
                resultSet[i] = PropertySpace.create(i, space.getInt(JSONKey.BASE_RENT.getKey()),
                        space.getString(JSONKey.NAME.getKey()), "ff00ff");
                break;
            case 2:
                resultSet[i] = StationSpace.create(i, space.getInt(JSONKey.BASE_RENT.getKey()),
                        space.getString(JSONKey.NAME.getKey()));
                break;
            case 3:
                resultSet[i] = FreeParkingSpace.create(i);
                break;
            case 4:
                resultSet[i] = JailSpace.create(i);
                break;
            case 5:
                resultSet[i] = GoToJailSpace.create(i);
                break;
            case 6:
            case 7:
                resultSet[i] = CardSpace.create(i, type == 6 ? chanceStack : communityStack);
                break;
            case 8:
                resultSet[i] = TaxSpace.create(i, space.getInt(JSONKey.TAX.getKey()));
                break;
            default:
                throw new JSONException("Unexpected space type " + type);
            }
        }
        for (Space space : resultSet) {
            game.add(space);
            space.save();
        }
        return resultSet;
    }
}
