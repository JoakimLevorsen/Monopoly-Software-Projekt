package monopoly.model.cards

import monopoly.controller.MovementController
import monopoly.model.Player

/**
 * GoToSpaceCard: Implementering af Go To Space Card
 *
 * @author Cecilie Krog Drejer, s185032
 * @author Joakim Bøegh Levorsen, s185023
 * @author Anders Brandt, s185016
 */
class GoToSpaceCard : Card() {

    /**
     * GetSpace: Henter index for det felt, kortet henviser til
     *
     * @author Cecilie Krog Drejer, s185032
     *
     * @return Returnerer index for et felt
     */
    val space: Int
        get() = this.getInteger(GoToSpaceCard.Properties.SPACE.property)!!

    /**
     * GetStackPosition: Henter kortets placering i kortbunken
     *
     * @author Cecilie Krog Drejer, s185032
     *
     * @return Returnerer index for kortets placering i bunken
     */
    /**
     * SetStackPosition: Sætter kortets placering i kortbunken
     *
     * @param i Index, der angiver kortets ønskede placering
     *
     * @author Cecilie Krog Drejer, s185032
     */
    override var stackPosition: Int
        get() = this.getInteger(GoToSpaceCard.Properties.STACK_POSITION.property)!!
        set(i) {
            this.set<GoToSpaceCard>(GoToSpaceCard.Properties.STACK_POSITION.property, i)
        }

    /**
     * GetText: Henter teksten der står på kortet
     *
     * @author Anders Brandt, s185016
     *
     * @return Returnerer teksten som en String
     */
    override val text: String
        get() = this.getString(Properties.TEXT.property)

    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    enum class Properties private constructor(val property: String) {
        TEXT("text"), SPACE("finalBoardPosition"), STACK_POSITION("stackPosition")
    }

    /**
     * Equals: Bestemmer om et GoToSpaceCard ligner et andet
     *
     * @param obj Det objekt, som det pågældende GoToSpaceCard skal sammenlignes med
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer om de to ojekter er ens eller ej
     */
    override fun equals(obj: Any?): Boolean {
        return if (obj !is GoToSpaceCard) false else obj.longId == this.longId
    }

    /**
     * Execute: Flytter spilleren til det pågældende felt
     *
     * @param moveController en MovementController
     * @param player Spilleren, som trækker kortet
     *
     * @author Anders Brandt, s185016
     * @author Cecilie Krog Drejer, s185032
     */
    override fun execute(moveController: MovementController, player: Player) {
        moveController.goTo(moveController.controller.game.getBoard()[space], false, player)
    }

    companion object {

        /**
         * Create: Opretter et GoToSpaceCard
         *
         * @param text Tekst, der står på kortet
         * @param space Index på det felt, der henvises til
         *
         * @author Cecilie Krog Drejer, s185032
         *
         * @return Returnerer et GoToSpaceCard
         */
        fun create(text: String, space: Int): GoToSpaceCard {
            val goToSpaceCard = GoToSpaceCard()
            goToSpaceCard.set<GoToSpaceCard>(GoToSpaceCard.Properties.TEXT.property, text)
            goToSpaceCard.set<GoToSpaceCard>(GoToSpaceCard.Properties.SPACE.property, space)
            return goToSpaceCard
        }
    }
}
