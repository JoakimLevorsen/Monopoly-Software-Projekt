package monopoly.model.spaces

import monopoly.controller.GameController
import monopoly.model.Player

/**
 * FreeParkingSpace: Et objekt til at repræsentere gratis parkering feltet
 *
 * @author Joakim Bøegh Levorsen, s185023
 * @author Anders Brandt, s185016
 */
class FreeParkingSpace : Space() {

    /**
     * GetTreasure: Henter pengebeløbet, der ligger på feltet
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer pengebeløbet
     */
    val treasure: Int
        get() = this.getInteger(FreeParkingSpace.Properties.TREASURE.property)!!.toInt()

    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    enum class Properties private constructor(val property: String) {
        BOARD_POSITION("boardPosition"), TREASURE("treasure")
    }

    /**
     * PerformAction: Udbetaler gevinsten til spilleren samt nulstiller gevinstbeløbet
     *
     * @param controller en GameController
     * @param player Spilleren, som lander på feltet
     *
     * @author Anders Brandt, s185016
     */
    override fun performAction(controller: GameController, player: Player) {
        player.changeAccountBalance(treasure)
        resetTreasure()
    }

    /**
     * Equals: Bestemmer om et FreeParkingSpace ligner et andet
     *
     * @param obj Det objekt, som det pågældende FreeParkingSpace skal sammenlignes med
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer om de to ojekter er ens eller ej
     */
    override fun equals(obj: Any?): Boolean {
        if (obj !is FreeParkingSpace)
            return false
        val other = obj as FreeParkingSpace?
        return (other!!.longId == this.longId && this.treasure == other.treasure
                && this.boardPosition == other.boardPosition)
    }

    /**
     * ResetTreasure: Sætter pengebeløbet, der ligger på feltet, lig 0
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    fun resetTreasure() {
        this.set<FreeParkingSpace>(FreeParkingSpace.Properties.TREASURE.property, 0)
    }

    /**
     * AddToTreasure: Tilføjer til pengebeløbet, der ligger på feltet
     *
     * @param amount Beløb, der skal tilføjes til skatten
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    fun addToTreasure(amount: Int) {
        val newBalance = this.treasure + amount
        this.set<FreeParkingSpace>(FreeParkingSpace.Properties.TREASURE.property, newBalance)
    }

    companion object {

        /**
         * Create: Opretter et FreeParkingSpace
         *
         * @param position Feltets placering på spillebrættet
         * @param name Feltets navn
         * @param color Feltets farve
         *
         * @author Joakim Bøegh Levorsen, s185023
         *
         * @return Returnerer et FreeParkingSpace
         */
        fun create(position: Int, name: String, color: String): FreeParkingSpace {
            var space = FreeParkingSpace()
            space = Space.setValues(space, name, color) as FreeParkingSpace
            space.set<FreeParkingSpace>(FreeParkingSpace.Properties.BOARD_POSITION.property, position)
            space.set<FreeParkingSpace>(FreeParkingSpace.Properties.TREASURE.property, 0)
            return space
        }
    }
}
