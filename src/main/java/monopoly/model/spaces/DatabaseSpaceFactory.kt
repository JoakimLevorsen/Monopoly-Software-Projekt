package monopoly.model.spaces

import monopoly.model.Game

import java.util.ArrayList
import java.util.Collections

/**
 * DatabaseSpaceFactory: Et objekt til at hente alle spaces der er gemt i databasen for et spil.
 *
 * @author Joakim Levorsen, S185023
 */
object DatabaseSpaceFactory {
    /**
     * GetSpacesFor: Henter felterne i et givent spil
     *
     * @param game Det spil, som der skal hentes felter fra
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer en liste af Spaces
     */
    fun getSpacesFor(game: Game): List<Space> {
        val classArray = arrayOf<Space::class>(CardSpace::class.java, FreeParkingSpace::class.java, GoToJailSpace::class.java, JailSpace::class.java, PropertySpace::class.java, StartSpace::class.java, StationSpace::class.java, TaxSpace::class.java)

        // Get the spaces
        val allSpaces = ArrayList<Space>()
        for (c in classArray) {
            allSpaces.addAll(game.getAll<Space>(c).load())
        }

        // Sort the spaces
        Collections.sort(allSpaces)

        return allSpaces
    }
}
