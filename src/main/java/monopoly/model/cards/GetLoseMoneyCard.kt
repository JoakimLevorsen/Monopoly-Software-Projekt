package monopoly.model.cards

import monopoly.controller.MovementController
import monopoly.model.Player

/**
 * GetLoseMoneyCard: Implementering af korttype til at betale penge til banken/få penge fra banken
 *
 * @author Cecilie Krog Drejer, s185032
 * @author Anders Brandt, s185016
 */
class GetLoseMoneyCard : Card() {

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
     * GetAmount: Henter beløbet, der skal betales/som banken udbetaler
     *
     * @author Cecilie Krog Drejer, s185032
     *
     * @return Returnerer beløbet
     */
    val amount: Int
        get() = this.getInteger(GetLoseMoneyCard.Properties.AMOUNT.property)!!

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
        get() = this.getInteger(GetLoseMoneyCard.Properties.STACK_POSITION.property)!!
        set(i) {
            this.set<GetLoseMoneyCard>(GetLoseMoneyCard.Properties.STACK_POSITION.property, i)
        }

    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    enum class Properties private constructor(val property: String) {
        TEXT("text"), AMOUNT("amount"), STACK_POSITION("stackPosition")
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
        return if (obj !is GetLoseMoneyCard) false else obj.longId == this.longId
    }

    /**
     * Execute: Ændrer spillerens balance med det pågældende beløb
     *
     * @param moveController en MovementController
     * @param player Spilleren, som trækker kortet
     *
     * @author Cecilie Krog Drejer, s185032
     */
    override fun execute(moveController: MovementController, player: Player) {
        player.changeAccountBalance(this.amount)
    }

    companion object {

        /**
         * Create: Opretter et GetLoseMoneyCard
         *
         * @param text Tekst, der står på kortet
         * @param amount Beløb, der skal betales til banken/som banken betale
         *
         * @author Cecilie Krog Drejer, s185032
         *
         * @return Returnerer et GetLoseMoneyCard
         */
        fun create(text: String, amount: Int): GetLoseMoneyCard {
            val getLoseMoneyCard = GetLoseMoneyCard()
            getLoseMoneyCard.set<Model>(GetLoseMoneyCard.Properties.TEXT.property, text)
            getLoseMoneyCard.set<Model>(GetLoseMoneyCard.Properties.AMOUNT.property, amount)
            return getLoseMoneyCard
        }
    }
}
