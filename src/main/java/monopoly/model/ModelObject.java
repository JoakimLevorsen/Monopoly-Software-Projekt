package monopoly.model;

import org.json.*;

/*
ModelObject:
Et interface med vigtige metoder alle objekter i modellen skal have.

@author Joakim Levorsen, S185023
*/
interface ModelObject {
    public ModelObject getFromJSON(JSONObject object);

    public ModelObject loadFromDatabase(ModelObject parent);
    public void saveToDatabase();
}