package monopoly.model.spaces

import monopoly.controller.GameController
import monopoly.model.Player
import resources.json.JSONKey

/**
 * TaxSpace: Et objekt til at repræsentere skattefeltet
 *
 * @author Anders Brandt, s185016
 * @author Joakim Bøegh Levorsen, s185023
 */
class TaxSpace : Space() {

    /**
     * GetTax: Henter det beløb, som skal betales på det pågældende TaxSpace
     *
     * @author Anders Brandt, s185016
     *
     * @return Returnerer beløbet
     */
    val tax: Int
        get() = this.getInteger(Properties.TAX.property)!!.toInt()

    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    enum class Properties private constructor(val property: String) {
        BOARD_POSITION("boardPosition"), TAX("tax"), TAX_MESSAGE("taxMessage")
    }

    /**
     * PerformAction: Trækker skat fra spillerens konto samt tilføjer beløbet til
     * gevinsten på gratis parkering
     *
     * @param controller en GameController
     * @param player Spilleren, som lander på feltet
     *
     * @author Anders Brandt, s185016
     */
    override fun performAction(controller: GameController, player: Player) {
        player.changeAccountBalance(-tax)

        for (space in controller.game.getBoard()) {
            if (space is FreeParkingSpace) {
                space.addToTreasure(tax)
            }
        }
        val message = controller.game.languageData.getString(JSONKey.TAX_MESSAGE.key)
        controller.view.gui.showMessage(message)
    }

    /**
     * Equals: Bestemmer om et TaxSpace ligner et andet
     *
     * @param obj Det objekt, som det pågældende TaxSpace skal sammenlignes med
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer om de to ojekter er ens eller ej
     */
    override fun equals(obj: Any?): Boolean {
        if (obj !is TaxSpace)
            return false
        val other = obj as TaxSpace?
        return other!!.longId == this.longId && this.boardPosition == other.boardPosition
    }

    companion object {

        /**
         * Create: Opretter et TaxSpace
         *
         * @param position Feltets placering på spillebrættet
         * @param tax Beløbet, som skal betales i skat
         * @param name Feltets navn
         * @param color Feltets farve
         *
         * @author Joakim Bøegh Levorsen, s185023
         * @author Anders Brandt, s185016
         *
         * @return Returnerer et TaxSpace
         */
        fun create(position: Int, tax: Int, name: String, color: String): TaxSpace {
            var space = TaxSpace()
            space = Space.setValues(space, name, color) as TaxSpace
            space.set<TaxSpace>(Properties.BOARD_POSITION.property, position)
            space.set<TaxSpace>(Properties.TAX.property, tax)
            return space
        }
    }
}
