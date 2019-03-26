package monopoly.model;

import org.json.*;
import java.sql.Connection;

/*
ModelObject:
Et interface med vigtige metoder alle objekter i modellen skal have.

@author Joakim Levorsen, S185023
*/
interface ModelObject {
    public int getDatabaseKey();
    public ModelObject getFromJSON(JSONObject object) throws JSONException;

    public void saveToDatabase(Connection c);
}
