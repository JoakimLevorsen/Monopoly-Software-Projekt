package monopoly.model.spaces

import monopoly.controller.GameController
import monopoly.model.Player

/**
 * GoToJailSpace: Et objekt til at repræsentere gå i fængsel feltet.
 *
 * @author Joakim Bøegh Levorsen, s185023
 * @author Anders Brandt, s185016
 */
class GoToJailSpace : Space() {
    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    enum class Properties private constructor(val property: String) {
        BOARD_POSITION("boardPosition")
    }

    /**
     * PerformAction: Sender en spiller til fængslet og fængsler dem
     *
     * @param controller en GameController
     * @param player Spilleren, der landet på feltet
     *
     * @author Anders Brandt, s105016
     */
    override fun performAction(controller: GameController, player: Player) {
        var target: JailSpace? = null
        for (space in controller.game.getBoard()) {
            if (space is JailSpace) {
                target = space
            }
        }
        controller.movementController.goTo(target!!, true, player)
        target.jail(player)
    }

    /**
     * Equals: Bestemmer om et GoToJailSpace ligner et andet
     *
     * @param obj Det objekt, som det pågældende GoToJailSpace skal sammenlignes med
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer om de to ojekter er ens eller ej
     */
    override fun equals(obj: Any?): Boolean {
        if (obj !is GoToJailSpace)
            return false
        val other = obj as GoToJailSpace?
        return other!!.longId == this.longId && this.boardPosition == other.boardPosition
    }

    companion object {

        /**
         * Create: Opretter et GoToJailSpace
         *
         * @param position Feltets placering på spillebrættet
         * @param name Feltets navn
         * @param color Feltets farve
         *
         * @author Joakim Bøegh Levorsen, s185023
         *
         * @return Returnerer et GoToJailSpace
         */
        fun create(position: Int, name: String, color: String): GoToJailSpace {
            var space = GoToJailSpace()
            space = Space.setValues(space, name, color) as GoToJailSpace
            space.set<GoToJailSpace>(GoToJailSpace.Properties.BOARD_POSITION.property, position)
            return space
        }
    }
}
