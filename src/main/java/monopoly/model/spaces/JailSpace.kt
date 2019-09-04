package monopoly.model.spaces

import monopoly.controller.GameController
import monopoly.model.Player

/**
 * JailSpace: Et objekt til at repræsentere fængsels feltet.
 *
 * @author Joakim Bøegh Levorsen, s185023
 */
class JailSpace : Space() {
    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    enum class Properties private constructor(val property: String) {
        BOARD_POSITION("boardPosition")
    }

    /**
     * PerformAction: Gør intet, da man bare er på besøg hvis man lander på JailSpace
     *
     * @param controller en GameController
     * @param player Spilleren, der landet på feltet
     */
    override fun performAction(controller: GameController, player: Player) {
        // Da man bare på besøg, sker der ikke noget når man lander på dette felt.
    }

    /**
     * Equals: Bestemmer om et JailSpace ligner et andet
     *
     * @param obj Det objekt, som det pågældende JailSpace skal sammenlignes med
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer om de to ojekter er ens eller ej
     */
    override fun equals(obj: Any?): Boolean {
        if (obj is JailSpace) {
            val other = obj as JailSpace?
            return other!!.longId == this.longId
        }
        return false
    }

    /**
     * Jail: Fængsler en spiller
     *
     * @param player Spilleren, der skal fængsles
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    fun jail(player: Player) {
        this.add(player)
    }

    /**
     * Release: Løslader en spiller
     *
     * @param player Spilleren, der skal løslades
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    fun release(player: Player) {
        player.getOutOfJail()
    }

    companion object {

        /**
         * Create: Opretter et JailSpace
         *
         * @param position Feltets placering på spillebrættet
         * @param name Feltets navn
         * @param color Feltets farve
         *
         * @author Joakim Bøegh Levorsen, s185023
         *
         * @return Returnerer et JailSpace
         */
        fun create(position: Int, name: String, color: String): JailSpace {
            var space = JailSpace()
            space = Space.setValues(space, name, color) as JailSpace
            space.set<JailSpace>(JailSpace.Properties.BOARD_POSITION.property, position)
            return space
        }
    }
}
