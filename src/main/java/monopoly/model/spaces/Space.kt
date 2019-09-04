package monopoly.model.spaces

import designpatterns.*
import monopoly.controller.GameController
import monopoly.model.Player
import org.javalite.activejdbc.Model

import java.awt.*
import java.util.HashSet

/**
 * Space: En abstrakt klasse til at repræsentere felterne i spillet
 *
 * @author Joakim Bøegh Levorsen, s185023
 * @author Anders Brandt, s185016
 * @author Helle Achari, s180317
 * @author Ekkart Kindler, ekki@dtu.dk
 */
abstract class Space : Model(), Comparable<Space>, Subject {

    /**
     * GetBoardPosition: Henter feltets placering på spillebrættet
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer feltets placering på brættet som index i et Space-array
     */
    val boardPosition: Int
        get() = this.getInteger(CardSpace.Properties.BOARD_POSITION.property)!!.toInt()

    /**
     * GetName: Henter navnet på feltet.
     *
     * @author Anders Brandt, s185016
     */
    val name: String
        get() = this.getString(StationSpace.Properties.NAME.property)

    /**
     * GetColour: Henter hexkoden for et felt og ændrer det til RGB.
     *
     * @author Anders Brandt, s185016
     *
     * @return Returnerer farven som et Color-objekt
     */
    val color: Color
        get() {
            val hex = this.getString(PropertySpace.Properties.COLOR.property)
            return Color(Integer.valueOf(hex.substring(0, 2), 16), Integer.valueOf(hex.substring(2, 4), 16),
                    Integer.valueOf(hex.substring(4, 6), 16))
        }

    /**
     * Variabler og metoder til at implementere Subject
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    override val observers = HashSet<Observer>()

    abstract fun performAction(controller: GameController, player: Player)

    abstract override fun equals(obj: Any?): Boolean

    /**
     * CompareTo: Sammenligner et Space med et andet
     *
     * @param otherSpace Det felt, som der skal sammenlignes med
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer forskellen i felternes placering på spillebrættet
     */
    override fun compareTo(otherSpace: Space): Int {
        return this.boardPosition - otherSpace.boardPosition
    }

    companion object {

        /**
         * SetValues: Sætter værdier på et felt
         *
         * @param space Det felt, for hvilket værdierne skal ændres
         * @param name Feltets navn
         * @param color Feltets farve
         *
         * @author Joakim Bøegh Levorsen, s185023
         *
         * @return Returnerer feltet
         */
        fun setValues(space: Space, name: String, color: String): Space {
            space.set<Model>(PropertySpace.Properties.NAME.property, name)
            space.set<Model>(PropertySpace.Properties.COLOR.property, color)
            return space
        }
    }
}
