package monopoly.model.spaces

import monopoly.controller.GameController
import monopoly.model.Game
import monopoly.model.Player
import monopoly.model.cards.Card
import monopoly.model.cards.CardStack

/**
 * CardSpace: Implementering af CardSpace
 *
 * @author Joakim Bøegh Levorsen, s185023
 * @author Anders Brandt, s185016
 */
class CardSpace : Space() {

    /**
     * GetCardStackId: Henter en kortbunkes ID
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer ID'et som en long
     */
    val cardStackId: Long
        get() = this.getLong(Properties.CARD_STACK_ID.property)!!.toLong()

    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    enum class Properties private constructor(val property: String) {
        BOARD_POSITION("boardPosition"), CARD_STACK_ID("card_stack_id")
    }

    /**
     * GetStack: Henter en kortbunker
     *
     * @param game Det spil, som kortbunken tilhører
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer en kortbunke
     */
    fun getStack(game: Game): CardStack? {
        return game.getStackForID(this.cardStackId)
    }

    /**
     * Equals: Bestemmer om et CardSpace ligner et andet
     *
     * @param obj Det objekt, som det pågældende CardSpace skal sammenlignes med
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer om de to ojekter er ens eller ej
     */
    override fun equals(obj: Any?): Boolean {
        if (obj !is CardSpace)
            return false
        val other = obj as CardSpace?
        return this.boardPosition == other!!.boardPosition
    }

    /**
     * PerformAction: Trækker et Chancekort og eksekverer det
     *
     * @param controller en GameController
     * @param player Spilleren, som lander på feltet
     *
     * @author Anders Brandt, s185016
     */
    override fun performAction(controller: GameController, player: Player) {
        for (stack in controller.game.cardStacks) {
            if (stack.longId!!.toLong() == this.cardStackId) {
                val drawnCard = stack.drawCard(controller.game)
                controller.view.gui.displayChanceCard(drawnCard.text)
                drawnCard.execute(controller.movementController, player)
            }
        }
    }

    companion object {

        /**
         * Create: Opretter et CardSpace
         *
         * @param position Feltets placering på spillebrættet
         * @param stack Hvilken kortbunke feltet har
         * @param name Feltets navn
         * @param color Feltets farve
         *
         * @author Joakim Bøegh Levorsen, s185023
         *
         * @return Returnerer et CardSpace
         */
        fun create(position: Int, stack: CardStack, name: String, color: String): CardSpace {
            var space = CardSpace()
            space = Space.setValues(space, name, color) as CardSpace
            space.set<CardSpace>(CardSpace.Properties.BOARD_POSITION.property, position)
            stack.add(space)
            return space
        }
    }
}
