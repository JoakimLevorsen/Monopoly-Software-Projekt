package monopoly.model;

import org.json.*;

/*
ModelJSONObject:
Et interface der beskriver den metode der henter data fra JSON

@author Joakim Levorsen, S185023
*/
interface ModelJSONObject {
    public ModelJSONObject getFromJSON(JSONObject object) throws JSONException;
}
