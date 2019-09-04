package monopoly.model.cards

import monopoly.controller.MovementController
import monopoly.model.Player
import monopoly.model.spaces.JailSpace
import monopoly.model.spaces.Space

/**
 * GoToJailCard: Implementering af Go To Jail Card
 *
 * @author Cecilie Krog Drejer, s185032
 * @author Joakim Bøegh Levorsen, s185023
 * @author Anders Brandt, s185016
 */
class GoToJailCard : Card() {

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
        get() = this.getInteger(GoToJailCard.Properties.STACK_POSITION.property)!!
        set(i) {
            this.set<GoToJailCard>(GoToJailCard.Properties.STACK_POSITION.property, i)
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
        TEXT("text"), SPACE("space"), STACK_POSITION("stackPosition")
    }

    /**
     * Equals: Bestemmer om et GetLoseMoneyCard ligner et andet
     *
     * @param obj Det objekt, som det pågældende GetLoseMoneyCard skal sammenlignes med
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer om de to ojekter er ens eller ej
     */
    override fun equals(obj: Any?): Boolean {
        return if (obj !is GoToJailCard) false else obj.longId == this.longId
    }

    /**
     * Execute: Sørger for at spilleren bliver rykket hen til fængsels feltet samt
     * fængsler spilleren
     *
     * @param moveController en MovementController
     * @param player Spilleren, som trækker kortet
     *
     * @author Anders Brandt, s185016
     */
    override fun execute(moveController: MovementController, player: Player) {
        var target: JailSpace? = null
        for (space in moveController.controller.game.getBoard()) {
            if (space is JailSpace) {
                target = space
            }
        }
        moveController.goTo(target!!, true, player)
        target.jail(player)
    }

    companion object {

        /**
         * Create: Opretter et GoToJailCard
         *
         * @param text Tekst, der står på kortet
         * @param space Index på det felt, der henvises til
         *
         * @author Cecilie Krog Drejer, s185032
         *
         * @return Returnerer et GoToJailCard
         */
        fun create(text: String, space: Int): GoToJailCard {
            val goToJailCard = GoToJailCard()
            goToJailCard.set<GoToJailCard>(GoToJailCard.Properties.TEXT.property, text)
            goToJailCard.set<GoToJailCard>(GoToJailCard.Properties.SPACE.property, space)
            return goToJailCard
        }
    }
}
