package monopoly.model.spaces;

import monopoly.model.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DatabaseSpaceFactory: Et objekt til at hente alle spaces der er gemt i databasen for et spil.
 *
 * @author Joakim Levorsen, S185023
 */
public class DatabaseSpaceFactory {
    /**
     * GetSpacesFor: Henter felterne i et givent spil
     * 
     * @param game Det spil, som der skal hentes felter fra
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer en liste af Spaces
     */
    public static List<Space> getSpacesFor(Game game) {
        Class<? extends Space>[] classArray = new Class[] { CardSpace.class, FreeParkingSpace.class,
                GoToJailSpace.class, JailSpace.class, PropertySpace.class, StartSpace.class, StationSpace.class,
                TaxSpace.class };

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
