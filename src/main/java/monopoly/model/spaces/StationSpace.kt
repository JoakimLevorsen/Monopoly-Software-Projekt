package monopoly.model.spaces

import monopoly.controller.GameController
import monopoly.model.Game
import monopoly.model.Player
import resources.json.JSONKey

/**
 * StationSpace: Et objekt til at repræsentere stationer.
 *
 * @author Joakim Bøegh Levorsen, s185023
 * @author Cecilie Krog Drejer, s185032
 * @author Anders Brandt, s185016
 * @author Helle Achari, s180317
 */
open class StationSpace : Space() {

    /**
     * GetPrice: Henter ejendommens pris
     *
     * @author Helle Achari, s180317
     *
     * @return Returnerer prisen for ejendommen
     */
    val price: Int
        get() = this.getInteger(Properties.PRICE.property)!!

    /**
     * IsMortgaged: Bestemmer om ejendommen er pantsat
     *
     * @author Cecilie Krog Drejer, s185032
     *
     * @return Returnerer om ejendommen er pantsat eller ej
     */
    /**
     * SetMortgaged: Sætter ejendommens pantsat-status
     *
     * @param mortgaged Boolean om ejendommen er pantsat eller ej
     *
     * @author Cecilie Krog Drejer, s185032
     */
    var isMortgaged: Boolean
        get() = this.getBoolean(Properties.MORTGAGED.property)!!
        set(mortgaged) {
            this.set<StationSpace>(Properties.MORTGAGED.property, mortgaged)
        }

    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    enum class Properties private constructor(val property: String) {
        BOARD_POSITION("boardPosition"), NAME("name"), MORTGAGED("mortgaged"), PRICE("price"), BASE_RENT("baseRent"),
        OWNER("player_id")
    }

    /**
     * PerformAction: Betaler leje hvis feltet er ejet af en anden spiller, gør
     * ingenting hvis feltet er ejet af den spiller, der lander på feltet, eller
     * tilbyder spilleren at købe ejendommen, hvis den ikke er ejet af nogen
     *
     * @param controller en GameController
     * @param player     Spilleren, som lander på feltet
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    override fun performAction(controller: GameController, player: Player) {
        val owner = this.getOwner(controller.game)
        if (owner != null && !isMortgaged) {
            if (owner != player) {
                controller.cashController.payment(player, this.getRent(controller.game), owner)
                controller.view.gui.showMessage(
                        player.name + controller.jsonData.getString(JSONKey.PAYS.key) + getRent(controller.game) + controller.jsonData.getString(JSONKey.TO.key) + owner.name)
            }
        } else
            controller.propertyController.offerProperty(this, player)
    }

    /**
     * Equals: Bestemmer om et StationSpace ligner et andet
     *
     * @param obj Det objekt, som det pågældende StationSpace skal sammenlignes med
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer om de to ojekter er ens eller ej
     */
    override fun equals(obj: Any?): Boolean {
        if (obj !is StationSpace)
            return false
        val other = obj as StationSpace?
        return this.boardPosition == other!!.boardPosition
    }

    /**
     * SetOwner: Sætter en spiller som ejer af ejendommen
     *
     * @param player Spilleren, som skal sættes som ejer
     *
     * @author Cecilie Krog Drejer, s185032
     */

    fun setOwner(player: Player) {
        player.add(this)
    }

    /**
     * RemoveOwner: Fjerner den nuværende ejer af ejendommen
     *
     * @author Cecilie Krog Drejer, s185032
     */

    fun removeOwner() {
        this.set<StationSpace>(Properties.OWNER.property, null)
    }

    /**
     * GetOwner: Henter ejendommens ejer
     *
     * @param game Det spil, som ejendommen tilhører
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer ejeren (en Player)
     */
    fun getOwner(game: Game): Player? {
        val owner = this.parent<Player>(Player::class.java)
        if (owner != null) {
            val id = owner.longId!!
            return game.getPlayerForID(id)
        }
        return null
    }

    /**
     * GetRent: Henter lejen for stationen
     *
     * @param game Det spil, som stationen tilhører
     *
     * @author Anders Brandt, s185016
     *
     * @return Returnerer stationens leje
     */
    open fun getRent(game: Game): Int {
        val baseRent = this.getInteger(Properties.BASE_RENT.property)!!
        var amountOwned = 0
        val owner = this.getOwner(game) ?: return 0
        for (space in game.getBoard()) {
            if (space is StationSpace && space !is PropertySpace) {
                val otherOwner = space.getOwner(game)
                if (otherOwner != null && otherOwner == owner)
                    amountOwned++
            }
        }
        return baseRent * amountOwned
    }

    companion object {

        /**
         * Create: Opretter et StationSpace
         *
         * @param position Feltets placering på spillebrættet
         * @param name     Feltets navn
         * @param price    Ejendommens pris
         * @param baseRent Ejendommens basisleje
         * @param color    Feltets farve
         *
         * @author Joakim Bøegh Levorsen, s185023
         * @author Cecilie Krog Drejer, s185032
         *
         * @return Returnerer et StationSpace
         */
        fun create(position: Int, name: String, price: Int, baseRent: Int, color: String): StationSpace {
            var space = StationSpace()
            space = Space.setValues(space, name, color) as StationSpace
            space.set<StationSpace>(StationSpace.Properties.BOARD_POSITION.property, position)
            space.set<StationSpace>(StationSpace.Properties.NAME.property, name)
            space.set<StationSpace>(StationSpace.Properties.MORTGAGED.property, false)
            space.set<StationSpace>(StationSpace.Properties.PRICE.property, price)
            space.set<StationSpace>(StationSpace.Properties.BASE_RENT.property, baseRent)
            return space
        }
    }
}
