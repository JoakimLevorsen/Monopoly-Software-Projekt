package monopoly.view

import designpatterns.Observer
import designpatterns.Subject
import gui_fields.*
import gui_main.GUI
import monopoly.model.Game
import monopoly.model.Player
import monopoly.model.spaces.PropertySpace
import monopoly.model.spaces.Space
import monopoly.model.spaces.StationSpace
import org.json.JSONException
import org.json.JSONObject
import resources.json.JSONFile
import resources.json.JSONKey
import resources.json.ResourceManager

import javax.swing.*
import java.awt.*
import java.util.ArrayList
import java.util.HashMap

/**
 * View: Klasse der fungerer som View i MVC-modellen
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Anders Brandt s185016
 * @author Cecilie Krog Drejer, s185032
 * @author Joakim Bøegh Levorsen, s185023
 */
class View
/**
 * View constructor
 *
 * @param game Spillet, som view'et skal tilhøre
 * @param gui  GUI, som View'et skal arbejde med
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Anders Brandt s185016
 */
(private val game: Game,
 /**
  * GetGUI: Henter dette views GUI
  *
  * @author Joakim Bøegh Levorsen, s185023
  *
  * @return Returnerer dette views GUI
  */
 val gui: GUI) : Observer {
    private val jsonData: JSONObject

    private val playerToPosition = HashMap<Player, Int>()
    private val spaceToField = HashMap<Space, GUI_Field>()
    private val playerToGUIPlayer = HashMap<Player, GUI_Player>()
    private val panels = HashMap<Player, PlayerPanel>()
    private val disposed = false

    init {
        this.jsonData = game.languageData
        val guiFields = gui.fields
        var i = 0
        for (space in game.getBoard()) {
            space.addObserver(this)
            spaceToField[space] = guiFields[i++]
        }

        // create the players in the GUI
        for (player in game.getPlayers()) {
            val car = GUI_Car(player.color, Color.black, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL)
            val guiPlayer = GUI_Player(player.name, player.accountBalance, car)
            playerToGUIPlayer[player] = guiPlayer
            gui.addPlayer(guiPlayer)

            // register this view with the player as an observer, in order to update the
            // player's state in the GUI
            player.addObserver(this)
            val playerPanel = PlayerPanel(game, player)
            panels[player] = playerPanel
            updatePlayer(player)
        }
    }

    /**
     * WhichPropertyDoYouWantToBuildOn: Metode til at vælge hvilken ejendom man vil
     * bygge på
     *
     * @param possibleChoices Properties spilleren må bygge på
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @author Cecilie Krog Drejer, s185032
     * @return det PropertySpace brugeren vil bygge på
     */
    fun whichPropertyDoWantToBuildOn(possibleChoices: List<PropertySpace>): PropertySpace? {
        val targets = HashMap<String, PropertySpace>()
        val choices = ArrayList<String>()
        for (p in possibleChoices) {
            targets[p.name] = p
            choices.add(p.name)
        }
        val stringArray = choices.toTypedArray<String>()
        val choice = JOptionPane.showInputDialog(null,
                jsonData.getString(JSONKey.WHICH_PROPERTY_TO_BUILD_ON.key),
                jsonData.getString(JSONKey.CHOOSE_PROPERTY.key), JOptionPane.QUESTION_MESSAGE, null, stringArray,
                stringArray[0])
        if (choice != null) {
            val choiceString = choice.toString()
            return targets[choiceString]
        } else
            return null
    }

    /**
     * Update: Opdaterer GUI'en
     *
     * @param subject Det subjekt, der skal opdateres
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Anders Brandt s185016
     */
    override fun update(subject: Subject) {
        if (!disposed) {
            if (subject is Player) {
                updatePlayer(subject)
            } else if (subject is StationSpace) {
                updateProperty(subject)
            }
        }
    }

    /**
     * View: Opdaterer spilleren
     *
     * @param player Spiller der skal opdateres
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Anders Brandt s185016
     */
    private fun updatePlayer(player: Player) {
        val guiPlayer = this.playerToGUIPlayer[player]
        if (guiPlayer != null) {
            guiPlayer.balance = player.accountBalance
            val guiFields = gui.fields
            val oldPosition = playerToPosition[player]
            if (oldPosition != null && oldPosition < guiFields.size) {
                guiFields[oldPosition].setCar(guiPlayer, false)
            }
            val pos = player.boardPosition
            if (pos < guiFields.size) {
                playerToPosition[player] = pos
                guiFields[pos].setCar(guiPlayer, true)
            }

            val name = player.name
            if (name != guiPlayer.name) {
                guiPlayer.name = name
            }
        }
        panels[player]?.update()
    }

    /**
     * View: Opdaterer ejendommene
     *
     * @param property Ejendom der skal opdateres
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Anders Brandt s185016
     * @author Cecilie Krog Drejer, s185032
     */
    private fun updateProperty(property: StationSpace) {
        val thisField = this.spaceToField[property]
        val thisOwnableField = thisField as GUI_Ownable
        thisOwnableField.rentLabel = jsonData.getString(JSONKey.RENT.key) + " " + property.getRent(game).toString()
        if (thisOwnableField != null) {
            if (property.getOwner(game) != null) {
                if (property.isMortgaged) {
                    thisOwnableField.ownableLabel = jsonData.getString(JSONKey.MORTGAGE_BY.key)
                    thisOwnableField.ownerName = property.getOwner(game)!!.name
                    thisOwnableField.setBorder(Color.BLACK)
                } else {
                    thisOwnableField.ownableLabel = jsonData.getString(JSONKey.OWNED_BY.key)
                    thisOwnableField.ownerName = property.getOwner(game)!!.name
                    thisOwnableField.setBorder(property.getOwner(game)!!.color)
                }
            } else {
                thisOwnableField.ownableLabel = ""
                thisOwnableField.ownerName = ""
                thisOwnableField.setBorder(null)
            }
        }
        if (property is PropertySpace) {
            val thisStreet = thisField as GUI_Street
            if (property.housesBuilt == 5) {
                thisStreet.setHotel(true)
                thisStreet.setHouses(0)
            } else {
                thisStreet.setHotel(false)
                thisStreet.setHouses(property.housesBuilt)
            }
            this.updateProperty(property)
        }
    }

    companion object {

        /**
         * ChooseGame: Metode til at vælge om man vil hente et gemt spil (og i så fald
         * hvilket) eller om man vil starte et nyt spil. Hvis man henter et spil kan man
         * vælge mellem de 50 nyeste
         *
         * @author Cecilie Krog Drejer, s185032
         * @author Joakim Bøegh Levorsen, s185023
         *
         * @return Returnerer det valgte savegame eller et nyt spil
         */
        fun chooseGame(): Game? {
            val saveNameToGame = HashMap<String, Game>()
            val chooseGameGUI = GUI(arrayOfNulls(0))
            val rm = ResourceManager()

            val loadGame = chooseGameGUI.getUserLeftButtonPressed(
                    "Do you want to load a saved game or start a new game? / Vil du indlæse et gemt spil eller starte et nyt spil?",
                    "Load game / Indlæs spil", "Start new game / Start nyt spil")

            if (loadGame) {
                val savedGames = Game.findAll().limit<Game>(50).orderBy<Game>("updated_at desc")
                if (savedGames.isEmpty()) {
                    chooseGameGUI.showMessage(
                            "There are no saved games. Press 'OK' or the 'Enter'-key on your keyboard to start a new game. / Der er ingen gemte spil. Tryk 'OK' eller tryke på 'Enter'-tasten på dit tastatur for at starte et nyt spil.")
                    return startNewGame(chooseGameGUI, rm)
                } else {
                    val saveNames = arrayOfNulls<String>(savedGames.size)
                    for (i in savedGames.indices) {
                        val saveGameName = (savedGames.get(i).gameName + " "
                                + savedGames.get(i).updateTime.toGMTString())
                        saveNames[i] = saveGameName
                        saveNameToGame[saveGameName] = savedGames.get(i)
                    }
                    val selection = JOptionPane.showInputDialog(null, "Choose a game / Vælg et spil.",
                            "Load game / Indlæs spil", JOptionPane.QUESTION_MESSAGE, null, saveNames, saveNames[0])
                    if (selection != null) {
                        val selectionString = selection.toString()
                        val loadedGame = saveNameToGame[selectionString]
                        chooseGameGUI.close()
                        return loadedGame
                    } else {
                        chooseGameGUI.showMessage(
                                "You cancelled choosing a saved game. Press 'OK' or the 'Enter'-key on your keyboard to start a new game. / Du afbrød at vælge et gemt spil. Tryk 'OK' eller tryke på 'Enter'-tasten på dit tastatur for at starte et nyt spil.")
                        return startNewGame(chooseGameGUI, rm)
                    }
                }
            } else {
                return startNewGame(chooseGameGUI, rm)
            }
        }

        /**
         * StartNewGame: Metode til at få information til at starte et nyt spil fra
         * spilleren
         *
         * @param gooey GUI man vil bruge til beskeder
         * @param rm    En instans af ResourceManager
         *
         * @author Cecilie Krog Drejer, s185032
         *
         * @return Returnerer det nye Game spilleren har oprettet
         */
        fun startNewGame(gooey: GUI, rm: ResourceManager): Game? {
            val saveName = gooey.getUserString("Type a name for your new game / Angiv et navn til dit nye spil.")
            val playerAmount = gooey.getUserInteger("How many players? / Hvor mange spillere?", 2, 4)
            try {
                var language: JSONFile?
                do {
                    language = chooseLanguage()
                } while (language == null)
                val languageData = rm.readFile(language)
                gooey.close()
                Game
                return Game.newGame(saveName, language, languageData, playerAmount)
            } catch (e: JSONException) {
                println("Could not find resource")
                System.err.println(e)
                gooey.close()
                return null
            }

        }

        /**
         * ChooseLanguage: Metode til at vælge sprog
         *
         * @author Joakim Bøegh Levorsen, s185023
         * @author Cecilie Krog Drejer, s185032
         *
         * @return JSONFile-objekt for det sprog brugeren valgte
         *
         * @throws JSONException
         */
        @Throws(JSONException::class)
        fun chooseLanguage(): JSONFile? {
            val languageChoices = HashMap<String, JSONFile>()
            val stringChoices = ArrayList<String>()
            for (file in JSONFile.values()) {
                val text: String
                when (file.packName) {
                    "da" -> text = "Dansk"
                    "uk" -> text = "English (UK)"
                    "us" -> text = "English (US)"
                    else -> text = "Unknown Language: " + file.packName
                }
                languageChoices[text] = file
                stringChoices.add(text)
            }
            val stringArray = stringChoices.toTypedArray<String>()
            val choice = JOptionPane.showInputDialog(null, "Choose a language / Vælg et sprog",
                    "Vælg sprog / Choose language", JOptionPane.QUESTION_MESSAGE, null, stringArray, stringArray[0])
            if (choice != null) {
                val choiceString = choice.toString()
                return languageChoices[choiceString]
            } else
                return null
        }
    }
}
