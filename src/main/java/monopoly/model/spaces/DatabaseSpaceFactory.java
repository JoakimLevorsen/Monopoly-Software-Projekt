package monopoly.model.spaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import monopoly.model.Game;

public class DatabaseSpaceFactory {
    public static List<Space> getSpacesFor(Game game) {

        Class<? extends Space>[] classArray = new Class[] { CardSpace.class, FreeParkingSpace.class, GoToJailSpace.class,
                JailSpace.class, PropertySpace.class, StartSpace.class, StationSpace.class };

        // Get the spaces
        List<Space> allSpaces = new ArrayList<Space>();
        for (Class<? extends Space> c : classArray) {
            allSpaces.addAll(game.getAll(c).load());
        }

        // Sort the spaces
        Collections.sort(allSpaces);

        return allSpaces;
    }
}