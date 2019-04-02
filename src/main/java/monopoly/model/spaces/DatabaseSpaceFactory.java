package monopoly.model.spaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import monopoly.model.Game;

public class DatabaseSpaceFactory {
    public static List<Space> getSpacesFor(Game game) {
        // Get the spaces
        List<Space> allSpaces = new ArrayList<Space>();
        allSpaces.addAll(game.getAll(CardSpace.class).load());
        allSpaces.addAll(game.getAll(FreeParkingSpace.class).load());
        allSpaces.addAll(game.getAll(GoToJailSpace.class).load());
        allSpaces.addAll(game.getAll(JailSpace.class).load());
        allSpaces.addAll(game.getAll(PropertySpace.class).load());
        allSpaces.addAll(game.getAll(StartSpace.class).load());
        allSpaces.addAll(game.getAll(StationSpace.class).load());

        //Sort the spaces
        Collections.sort(allSpaces);

        return allSpaces;
    }
}