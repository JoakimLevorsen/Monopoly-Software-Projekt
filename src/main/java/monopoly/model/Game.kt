package monopoly.model

import designpatterns.Observer
import designpatterns.Subject
import gui_fields.*
import monopoly.model.cards.CardStack
import monopoly.model.spaces.*
import org.javalite.activejdbc.Model
import org.javalite.activejdbc.CompanionModel;
import org.javalite.activejdbc.validation.ValidationException
import org.json.JSONException
import org.json.JSONObject
import resources.json.JSONFile
import resources.json.JSONKey
import resources.json.ResourceManager

import java.awt.*
import java.sql.Timestamp
import java.util.ArrayList
import java.util.HashSet
import kotlin.reflect.KClass

/**
 * Game: Implementering af Game model objektet, med ORM for databasen.
 *
 * @author Joakim Bøegh Levorsen, s185023
 * @author Anders Brandt, s185016
 * @author Cecilie Krog Drejer, s185032
 * @author Helle Achari, s180317
 * @author Ekkart Kindler, ekki@dtu.dk
 */
class Game : Model(), Subject {
    companion object:CompanionModel<Game>(Game::class) {
        /**
         * NewGame: Opretter et nyt spil
         *
         * @param saveName Navn, som spillet skal gemmes under
         * @param languagePack .json-fil, som tekst skal læses fra
         * @param jsonData Data fra JSON, som bruges til at lave felter og kort
         * @param playerCount Antal spillere
         *
         * @author Joakim Bøegh Levorsen, s185023
         * @author Cecilie Krog Drejer, s185032
         *
         * @return Returnerer et nyt spil
         */
        fun newGame(saveName: String, languagePack: JSONFile, jsonData: JSONObject, playerCount: Int): Game {
            val g = Game()
            g.set<Model>(Game.Properties.CURRENT_TURN.property, 0)
            g.set<Model>(Game.Properties.SAVE_NAME.property, saveName)
            g.set<Model>(Properties.JSON_PACK.property, languagePack.packName)
            g.save()
            val chanceStack = CardStack.create(jsonData, g, true, 0)
            val communityStack = CardStack.create(jsonData, g, false, 0)
            JSONSpaceFactory.createSpaces(jsonData, g, chanceStack, communityStack)
            val colors = arrayOf("FF0000", "0000FF", "008000", "FFFF00")
            for (i in 0 until playerCount) {
                val newPlayer = Player.newPlayer(jsonData.getString(JSONKey.PLAYER.key) + (i + 1), i, 2000, colors[i])
                g.addPlayer(newPlayer)
            }
            return g
        }

        fun gameList(): List<Game> {
            return Game.findAll().orderBy<Game>(Game.Properties.CURRENT_TURN.property + " asc").load()
        }
    }
    private var players: MutableList<Player>? = null
    private var stacks: List<CardStack>? = null
    private var board: List<Space>? = null

    /**
     * GetCardStacks: Henter kortbunker fra databasen
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer en liste af kortbunker
     */
    val cardStacks: List<CardStack>
        get() {
            var actualStack = this.stacks ?: this.getAll(CardStack::class.java)
            this.stacks = actualStack
            return actualStack
        }

    /**
     * GetGameName: Henter spillets navn
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer spillets navn i form af en String
     */
    val gameName: String
        get() = this.get(Properties.SAVE_NAME.property) as String

    /**
     * GetUpdateTime: Henter det tidspunkt, spillet sidst er gemt
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer det tidspunkt, spillet sidst er gemt, som Timestamp
     */
    val updateTime: Timestamp
        get() = this.getTimestamp(Properties.UPDATED_AT.property)

    /**
     * GetCurrentTurn: Henter hvilken spiller, der pt. har tur, i form af et index
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer et spillerindex
     */
    /**
     * SetCurrentTurn: Sætter hvilken spiller, der pt. har tur, i form af et index
     *
     * @param turn Index på den spiller, der skal have tur
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    var currentTurn: Int
        get() = this.get(Properties.CURRENT_TURN.property) as Int
        set(turn) {
            this.set<Model>(Game.Properties.CURRENT_TURN.property, turn)
            this.updated()
        }

    /**
     * GetLanguageData: Henter data fra en .json-fil
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer data fra .json-fil som JSONObject
     *
     * @throws JSONException
     */
    val languageData: JSONObject
        @Throws(JSONException::class)
        get() {
            val packName = this.getString(Properties.JSON_PACK.property)
            val file = JSONFile.getFile(packName)
                    ?: throw JSONException("Json pakken med navnet $packName was not found")
            val rm = ResourceManager()
            return rm.readFile(file)
        }

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
        CURRENT_TURN("currentTurn"), SAVE_NAME("saveName"), JSON_PACK("jsonPack"), UPDATED_AT("updated_at")
    }

    /**
     * DeleteThisGame: Sletter et spil og alle dets børn fra databasen
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    fun deleteThisGame() {
        this.deleteCascade()
    }

    /**
     * SaveIt: Overskriver saveIt() for game, men kalder den på alle dens "børne"-elementer
     *
     * @author Anders Brandt, s185016
     *
     * @return Returnerer hvorvidt der blev gemt succesfuldt
     */
    override fun saveIt(): Boolean {

        try {
            super.saveIt()
        } catch (e: ValidationException) {
            e.printStackTrace()
            return false
        }

        if (players != null) {
            for (player in players!!) {
                try {
                    player.saveIt()
                } catch (e: ValidationException) {
                    e.printStackTrace()
                    return false
                }

            }
        }

        if (stacks != null) {
            for (cardStack in stacks!!) {
                try {
                    cardStack.saveIt()
                } catch (e: ValidationException) {
                    e.printStackTrace()
                    return false
                }

            }
        }

        if (board != null) {
            for (space in board!!) {
                try {
                    space.saveIt()
                } catch (e: ValidationException) {
                    e.printStackTrace()
                    return false
                }

            }
        }
        return true
    }

    /**
     * ExportGUIFields: Opretter spillebrættet til GUI'en med forskellige felttyper
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer spillebrættet i form af et array af GUI-felter
     */
    fun exportGUIFields(): Array<GUI_Field> {
        val board = this.getBoard()
        val guiBoard = arrayOfNulls<GUI_Field>(board.size)
        for (i in board.indices) {
            val space = board[i]
            if (space is PropertySpace) {
                guiBoard[i] = GUI_Street(space.name,
                        languageData.getString(JSONKey.RENT.key) + " " + space.getRent(this).toString(),
                        space.name, "", space.color, Color.WHITE)
            } else if (space is StationSpace) {
                guiBoard[i] = GUI_Shipping("default", space.name, "", "", space.getRent(this).toString(),
                        space.color, Color.WHITE)
            } else if (space is FreeParkingSpace) {
                guiBoard[i] = GUI_Refuge("default", space.name, space.name, "", space.color, Color.BLACK)
            } else if (space is JailSpace) {
                guiBoard[i] = GUI_Jail("default", space.name, space.name, "", space.color, Color.BLACK)
            } else if (space is GoToJailSpace) {
                guiBoard[i] = GUI_Jail("default", space.name, space.name, "", space.color, Color.BLACK)
            } else if (space is CardSpace) {
                guiBoard[i] = GUI_Chance("?", space.name, "", space.color, Color.BLACK)
            } else if (space is TaxSpace) {
                guiBoard[i] = GUI_Tax(space.name, space.tax.toString(), "", space.color, Color.white)
            } else if (space is StartSpace) {
                guiBoard[i] = GUI_Start(space.name, space.name, "", space.color, Color.BLACK)
            } else {
                println("Item not put on board")
            }
        }
//        We do this type conversion since we KNOW that no nulls are left.
        return guiBoard as Array<GUI_Field>
    }

    /**
     * AddPlayer: Tilføjer en spiller til spillet
     *
     * @param player Spiller, som skal tilføjes
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    fun addPlayer(player: Player) {
        if (this.players == null)
            this.getPlayers()
        this.players!!.add(player)
        this.add(player)
    }

    /**
     * GetPlayers: Henter spillere fra databasen
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer en liste af de spillere, der er med i spillet
     */
    fun getPlayers(): List<Player> {
        if (this.players == null) {
            this.players = this.getAll<Player>(Player::class.java).load()
        }
        // Sender kopi af listen så man ikke kan ændre den
        return ArrayList(this.players!!)
    }

    /**
     * GetPlayerForID: Henter en spiller fra databasen ud fra et ID
     *
     * @param id ID for den spiller, man vil hente
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer en spiller
     */
    fun getPlayerForID(id: Long): Player? {
        val players = this.getPlayers()
        for (p in players) {
            if (p.longId!!.toLong() == id) {
                return p
            }
        }
        return null
    }

    /**
     * GetSpacesForType: Finder alle felter af en bestemt type på brættet
     *
     * @param type Typen af felt på klasse-form (eksempelvis CardSpace.class)
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer en liste af felterne af den angivne type
     */
    fun <S : Space> getSpacesForType(type: KClass<S>): List<S> {
        val matches = ArrayList<S>()
        for (space in getBoard()) {
            if (type.isInstance(space)) {
                matches.add(space as S)
            }
        }
        return matches
    }

    /**
     * GetBoard: Henter spillebrættet fra databasen
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer en liste af felter
     */
    fun getBoard(): List<Space> {
        var actualBoard = this.board ?: DatabaseSpaceFactory.getSpacesFor(this)
        this.board = actualBoard
        return actualBoard
    }

    /**
     * GetStackForID: Henter en kortbunke ud fra et ID
     *
     * @param id Kortbunkens ID
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer en kortbunke
     */
    fun getStackForID(id: Long): CardStack? {
        val stacks = this.cardStacks
        for (c in stacks) {
            if (c.longId!!.toLong() == id) {
                return c
            }
        }
        return null
    }
}
