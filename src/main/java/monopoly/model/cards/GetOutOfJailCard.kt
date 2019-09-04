package monopoly.model.cards

import monopoly.controller.MovementController
import monopoly.model.Game
import monopoly.model.Player
import org.javalite.activejdbc.annotations.BelongsTo

/**
 * GetOutOfJailCard: Implementering af Get Out of Jail Free Card
 *
 * @author Cecilie Krog Drejer, s185032
 * @author Joakim Bøegh Levorsen, s185023
 * @author Anders Brandt, s185016
 */
@BelongsTo(parent = Player::class, foreignKeyName = "player_id")
class GetOutOfJailCard : Card() {

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
        get() = this.getInteger(GetOutOfJailCard.Properties.STACK_POSITION.property)!!
        set(i) {
            this.set<GetOutOfJailCard>(GetOutOfJailCard.Properties.STACK_POSITION.property, i)
        }

    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    enum class Properties private constructor(val property: String) {
        TEXT("text"), OWNER("player_id"), STACK_POSITION("stackPosition")
    }

    /**
     * GetOwner: Henter ejeren af det pågældende GetOutofJailFreeCard
     *
     * @param game Spillet, som kortet tilhører
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer ejeren (en Player)
     */
    fun getOwner(game: Game): Player? {
        val playerID = this.get(Properties.OWNER.property)
        if (playerID != null) {
            if (playerID is Int) {
                return game.getPlayerForID(playerID.toLong())
            } else {
                println("Woah unexpected type of player id")
            }
        }
        return null
    }

    /**
     * SetOwner: Sætter ejeren af det pågældende GetOutOfJailCard
     *
     * @param player Spilleren, der skal være ejer af kortet
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    fun setOwner(player: Player?) {
        if (player == null) {
            this.set<GetOutOfJailCard>(Properties.OWNER.property, null)
        }
        this.set<GetOutOfJailCard>(Properties.OWNER.property, player!!.longId)
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
        return if (obj !is GetOutOfJailCard) false else obj.longId == this.longId
    }

    /**
     * Execute: Sætter spilleren som ejer af kortet, så det kan bruges senere
     *
     * @param moveController en MovementController
     * @param player Spilleren, som trækker kortet
     *
     * @author Cecilie Krog Drejer, s185032
     */
    override fun execute(moveController: MovementController, player: Player) {
        setOwner(player)
    }

    companion object {

        /**
         * Create: Opretter et GetOutOfJailCard
         *
         * @param text Tekst, der står på kortet
         *
         * @author Cecilie Krog Drejer, s185032
         *
         * @return Returnerer et GetOutOfJailCard
         */
        fun create(text: String): GetOutOfJailCard {
            val getOutOfJailCard = GetOutOfJailCard()
            getOutOfJailCard.set<GetOutOfJailCard>(GetOutOfJailCard.Properties.TEXT.property, text)
            return getOutOfJailCard
        }
    }
}
