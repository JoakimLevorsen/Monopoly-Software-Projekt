package monopoly.model.spaces

import monopoly.controller.GameController
import monopoly.model.Player

/**
 * StartSpace: Et objekt til at repræsentere start feltet
 *
 * @author Joakim Bøegh Levorsen, s185023
 */
class StartSpace : Space() {

    /**
     * GetPayment: Henter det beløb, der skal udbetales når en spiller lander på/passerer feltet
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer beløbet
     */
    val payment: Int
        get() = this.getInteger(StartSpace.Properties.PAYMENT.property)!!.toInt()

    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    enum class Properties private constructor(val property: String) {
        BOARD_POSITION("boardPosition"), PAYMENT("payment")
    }

    /**
     * PerformAction: Udbetaler et beløb til spilleren
     *
     * @param controller en GameController
     * @param player Spilleren, som lander på feltet
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    override fun performAction(controller: GameController, player: Player) {
        player.changeAccountBalance(this.payment)
    }

    /**
     * Equals: Bestemmer om et StartSpace ligner et andet
     *
     * @param obj Det objekt, som det pågældende StartSpace skal sammenlignes med
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer om de to ojekter er ens eller ej
     */
    override fun equals(obj: Any?): Boolean {
        if (obj !is StartSpace)
            return false
        val other = obj as StartSpace?
        return other!!.longId == this.longId && this.boardPosition == other.boardPosition
    }

    companion object {

        /**
         * Create: Opretter et StartSpace
         *
         * @param position Feltets placering på spillebrættet
         * @param payment Det beløb, som udbetales når en spiller passerer/lander på feltet
         * @param name Feltets navn
         * @param color Feltets farve
         *
         * @author Joakim Bøegh Levorsen, s185023
         *
         * @return Returnerer et StartSpace
         */
        fun create(position: Int, payment: Int, name: String, color: String): StartSpace {
            var space = StartSpace()
            space = Space.setValues(space, name, color) as StartSpace
            space.set<StartSpace>(StartSpace.Properties.BOARD_POSITION.property, position)
            space.set<StartSpace>(StartSpace.Properties.PAYMENT.property, payment)
            return space
        }
    }
}
