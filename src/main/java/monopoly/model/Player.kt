package monopoly.model

import designpatterns.Observer
import designpatterns.Subject
import monopoly.model.cards.CardStack
import monopoly.model.cards.GetOutOfJailCard
import monopoly.model.spaces.PropertySpace
import monopoly.model.spaces.Space
import monopoly.model.spaces.StationSpace
import org.javalite.activejdbc.Model

import java.awt.*
import java.util.ArrayList
import java.util.HashSet

/**
 * Player: Implementering af Player model objektet, med ORM for databasen.
 *
 * @author Joakim Bøegh Levorsen, s185023
 * @author Cecilie Krog Drejer, s185032
 * @author Anders Brandt, s185016
 * @author Helle Achari, s180317
 * @author Ekkart Kindler, ekki@dtu.dk
 */
class Player : Model(), Subject {
    private var ownedProperties: ArrayList<StationSpace>? = null

    /**
     * GetName: Henter spillerens navn
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer spillerens navn som en String
     */
    val name: String
        get() = this.get(Player.Properties.NAME.property) as String

    /**
     * GetPlayerIndex: Henter spillerens index
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer spillerens index som en integer
     */
    val playerIndex: Int
        get() = this.get(Player.Properties.PLAYER_INDEX.property) as Int

    /**
     * GetBoardPosition: Henter spillerens placering på spillebrættet
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer spillerens placering som en integer
     */
    /**
     * SetBoardPosition: Sætter spillerens placering på spillebrættet
     *
     * @param position index for det felt, spilleren skal stå på
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    var boardPosition: Int
        get() = this.get(Player.Properties.BOARD_POSITION.property) as Int
        set(position) {
            this.set<Model>(Player.Properties.BOARD_POSITION.property, position)
            this.updated()
        }

    /**
     * GetAccountBalance: Henter spillerens kontobalance
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer spillerens balance som en integer
     */
    /**
     * SetAccountBalance: Sætter spillerens kontobalance til en bestemt værdi
     *
     * @param newBalance Den nye balances værdi
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    var accountBalance: Int
        get() = this.get(Player.Properties.ACCOUNT_BALANCE.property) as Int
        set(newBalance) {
            this.set<Model>(Player.Properties.ACCOUNT_BALANCE.property, newBalance)
            this.updated()
        }

    /**
     * getPrisonStatus: Henter om spilleren er fængslet eller ej
     *
     * @author Anders Brandt, s185016
     *
     * @return Returnerer om spilleren er fængslet eller ej som boolean
     */
    val isInJail: Boolean
        get() = this.get(Properties.JAIL_SPACE.property) != null

    /**
     * GetColour: Henter hexkoden for ejendommen og ændrer det til RGB
     *
     * @author Anders Brandt, s185016
     *
     * @return Returnerer et java Color-objekt
     */
    val color: Color
        get() {
            val hex = this.getString(PropertySpace.Properties.COLOR.property)
            return Color(Integer.valueOf(hex.substring(0, 2), 16), Integer.valueOf(hex.substring(2, 4), 16),
                    Integer.valueOf(hex.substring(4, 6), 16))
        }

    /**
     * IsBroke: Viser om spiller er gået konkurs, og dermed ude af spillet
     *
     * @author Joakim Levorsen, s185023
     *
     * @return Returnerer om spilleren er gået konkurs eller ej
     */
    val isBroke: Boolean
        get() = this.getBoolean(Properties.BROKE.property)!!

    /**
     * Variabler og metoder til at implementere Subject
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    override val observers = HashSet<Observer>()

    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    enum class Properties private constructor(val property: String) {
        NAME("name"), PLAYER_INDEX("playerIndex"), BOARD_POSITION("boardPosition"), ACCOUNT_BALANCE("accountBalance"),
        JAIL_SPACE("jail_space_id"), BROKE("broke"), COLOR("color")
    }

    /**
     * ChangeAccountBalance: Ændrer en spillers kontobalance med et beløb
     *
     * @param by Beløbet, balancen skal ændres med - kan også være negativ
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    fun changeAccountBalance(by: Int) {
        val newBalance = this.accountBalance + by
        this.set<Model>(Player.Properties.ACCOUNT_BALANCE.property, newBalance)
        this.updated()
    }

    /**
     * GetOwnedProperties: Henter og returnerer liste af ejendomme ejet af spilleren
     *
     * @param game Det pågældende spil, som spilleren tilhører
     *
     * @author Cecilie Krog Drejer, s185032
     *
     * @return Returnerer en liste af de ejendomme, som spilleren ejer
     */

    fun getOwnedProperties(game: Game): ArrayList<StationSpace> {
        if (ownedProperties == null) {
            val allProperties = game.getBoard()
            ownedProperties = ArrayList()
            for (space in allProperties) {
                if (space is StationSpace) {
                    if (space.getOwner(game) != null && space.getOwner(game) == this) {
                        ownedProperties!!.add(space)
                    }
                }
            }
        }
        return ownedProperties as ArrayList<StationSpace>
    }

    /**
     * AddToOwnedProperties: Tilføjer ejendom til liste af ejendomme ejet af
     * spilleren
     *
     * @param property Ejendom, der skal tilføjes
     * @param game Det pågældende spil, som spilleren tilhører
     *
     * @author Cecilie Krog Drejer, s185032
     * @author Joakim Bøegh Levorsen, s185023
     */

    fun addToOwnedProperties(property: StationSpace, game: Game) {
        this.getOwnedProperties(game).add(property)
        this.updated()
    }

    /**
     * RemoveFromOwnedProperties: Fjerner ejendom fra liste af ejendomme ejet af
     * spilleren
     *
     * @param property Ejendom, der skal fjernes
     * @param game Det pågældende spil, som spilleren tilhører
     *
     * @author Cecilie Krog Drejer, s185032
     * @author Joakim Bøegh Levorsen, s185023
     */

    fun removeFromOwnedProperties(property: StationSpace, game: Game) {
        this.getOwnedProperties(game).remove(property)
        this.updated()
    }

    /**
     * HasOverdrawnAccount: Tjekker om spillerens kontobalance er negativ
     *
     * @author Cecilie Krog Drejer, s185032
     *
     * @return Returnerer om spillerens balance er negativ eller ej
     */
    fun hasOverdrawnAccount(): Boolean {
        return this.accountBalance < 0
    }

    /**
     * SetColor: sætter farven for spilleren
     *
     * @param color Spillerens farve skrevet som hexkode i en String (eksempelvis, blå = 0000FF)
     *
     * @author Anders Brandt, s185016
     */
    fun setColor(color: String) {
        this.set<Model>(Player.Properties.COLOR.property, color)
        this.updated()
    }

    /**
     * SetBrokeStatus: Sætter konkurs status
     *
     * @param to Boolean, som konkursstatus skal sættes til
     *
     * @author Joakim Levorsen, s185023
     */
    fun setBrokeStatus(to: Boolean) {
        this.set<Model>(Properties.BROKE.property, to)
        this.updated()
    }

    /**
     * GetOutOfJailCard: Finder spillerens GetOutOfJailCard, hvis de har et
     *
     * @param game Det spil, som spilleren tilhører
     *
     * @author Joakim Levorsen, s185023
     *
     * @return Returnerer et GetOutOfJailCard, hvis spilleren har et, ellers null
     */
    fun getGetOutOfJailCard(game: Game): GetOutOfJailCard? {
        for (stack in game.cardStacks) {
            for (getOutCard in stack.getCardForType<GetOutOfJailCard>(GetOutOfJailCard::class.java!!)) {
                val jailCardOwner = getOutCard.getOwner(game)
                if (jailCardOwner != null && jailCardOwner == this)
                    return getOutCard
            }
        }
        return null
    }

    /**
     * GetOutOfJail: Løslader spilleren fra fængsel
     *
     * @author Joakim Levorsen, s185023
     */
    fun getOutOfJail() {
        this.set<Model>(Properties.JAIL_SPACE.property, null)
    }

    companion object {

        /**
         * NewPlayer: Opretter en ny spiller
         *
         * @param name Spillerens navn
         * @param index Spillerens index
         * @param balance Spillerens startbalance
         * @param color Spillerens farve
         *
         * @author Joakim Bøegh Levorsen, s185023
         * @author Cecilie Krog Drejer, s185032
         *
         * @return Returnerer en ny spiller
         */
        fun newPlayer(name: String, index: Int, balance: Int, color: String): Player {
            return Player().set<Model>(Player.Properties.NAME.property, name)
                    .set<Model>(Player.Properties.PLAYER_INDEX.property, index)
                    .set<Model>(Player.Properties.BOARD_POSITION.property, 0)
                    .set<Model>(Player.Properties.ACCOUNT_BALANCE.property, balance)
                    .set(Player.Properties.COLOR.property, color)
        }
    }
}
