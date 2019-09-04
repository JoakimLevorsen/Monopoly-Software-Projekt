package monopoly.controller

import monopoly.DatabaseBase
import monopoly.model.Game
import monopoly.model.Player
import monopoly.model.cards.GetOutOfJailCard
import monopoly.model.spaces.JailSpace
import monopoly.model.spaces.Space
import monopoly.view.View
import org.javalite.activejdbc.validation.ValidationException
import org.json.JSONObject
import resources.json.JSONKey

/**
 * GameController står for at spille spillet og holde styr på hvem der vinder
 *
 * @author Anders Brandt, s185016
 * @author Cecilie Krog Drejer, s185032
 * @author Frederik Lykke Ullstad, s185018
 * @author Helle Achari, s180317
 * @author Joakim Bøegh Levorsen, s185023
 */
class GameController
/**
 * GameController constructor
 *
 * @param game Spillet, som skal kontrolleres
 * @param view View'et, som tilhører spillet
 *
 * @author Joakim Bøegh Levorsen, s185023
 */
(
        /**
         * GetGame: Henter spillet associeret med GameController
         *
         * @return Returnerer spillet associeret med GameController
         *
         * @author Joakim Bøegh Levorsen, s185023
         */
        val game: Game, var view: View) {
    var cashController: CashController
    var movementController: MovementController
    var propertyController: PropertyController
    /**
     * GetJSONData: Henter GameControllers JSON-data
     *
     * @return Returnerer spillet associeret med GameController
     *
     * @author Cecilie Krog Drejer, s185032
     */
    val jsonData: JSONObject

    /**
     * GetWinner: Finder vinderen af spiller
     *
     * @author Cecilie Krog Drejer, s185032
     *
     * @return Returnerer vinderen af spillet.
     */
    val winner: Player?
        get() {
            var winner: Player? = null
            for (player in game.getPlayers()) {
                if (!player.isBroke)
                    winner = player
            }
            return winner
        }

    init {
        this.jsonData = game.languageData
        this.movementController = MovementController(this)
        this.cashController = CashController(this)
        this.propertyController = PropertyController(this)
    }

    /**
     * Play: Spiller spillet
     *
     * @author Anders Brandt, s185016
     * @author Cecilie Krog Drejer, s185032
     * @author Frederik Lykke Ullstad, s185018
     * @author Helle Achari, s180317
     * @author Joakim Bøegh Levorsen, s185023
     */
    fun play() {
        do {
            while (playersLeft() > 1) {
                val currentPlayerTurn = game.currentTurn
                val playerWithTurn = game.getPlayers()[currentPlayerTurn]
                // Hvis spilleren er gået konkurs, ignorer dem
                if (!playerWithTurn.isBroke) {
                    if (playerWithTurn.isInJail) {
                        jailTurn(playerWithTurn)
                    }
                    // Spilleren er måske kommet ud, ellers perform turn
                    if (!playerWithTurn.isInJail) {
                        var r: DiceRoll
                        var doubleCount = 0
                        do {
                            view.gui.showMessage(
                                    playerWithTurn.name + jsonData.getString(JSONKey.ROLL_DICE.key))
                            r = DiceRoll()
                            if (doubleCount == 2 && r.isDoubles) {
                                for (space in game.getBoard()) {
                                    if (space is JailSpace) {
                                        space.jail(playerWithTurn)
                                    }
                                }
                            } else {
                                movementController.moveForward(r.sum(), playerWithTurn)
                            }
                            doubleCount++
                        } while (r.isDoubles && doubleCount < 2)
                    }
                    // Kom spiller i fængsel i sit ryk?
                    if (!playerWithTurn.isInJail) {
                        propertyController.trade(playerWithTurn)
                        propertyController.offerToBuild(playerWithTurn)
                        propertyController.unmortgageProperties(playerWithTurn)
                    }
                    // Hvis spilleren ikke har flere penge start konkurs flow
                    if (playerWithTurn.hasOverdrawnAccount()) {
                        propertyController.playerBroke(playerWithTurn)
                    }
                }
                incrementTurn(currentPlayerTurn)
                saveGame()
            }
            view.gui.showMessage(winner!!.name + jsonData.getString(JSONKey.GAME_WINNER.key))
        } while (view.gui.getUserLeftButtonPressed(jsonData.getString(JSONKey.PLAY_AGAIN.key),
                        jsonData.getString(JSONKey.YES.key), jsonData.getString(JSONKey.NO.key)))
    }

    /**
     * SaveGame: Gemmer spillet til databasen
     *
     * @author ?
     */
    fun saveGame() {
        try {
            DatabaseBase.openTransaction()
            game.saveIt()
            DatabaseBase.commitTransaction()
        } catch (e: ValidationException) {
            System.out.println("Save of game failed")
            DatabaseBase.rollBackTransaction()
            e.printStackTrace()
        }

    }

    /**
     * IncrementTurn: Holder styr på hvilken spiller, der har tur
     *
     * @param currentPlayerTurn index på den spiller, der har tur nu
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    fun incrementTurn(currentPlayerTurn: Int) {
        var nextPlayerTurn = currentPlayerTurn + 1
        if (nextPlayerTurn == game.getPlayers().size) {
            nextPlayerTurn = 0
        }
        game.currentTurn = nextPlayerTurn
        System.out.println("Next turn is " + game.currentTurn)
    }

    /**
     * JailTurn: Tager en spiller igennem processen at forsøge at undslippe fra
     * fængsel
     *
     * @param player spilleren, som skal igennem fængsels-flow'et
     *
     * @author Anders Brandt, s185016
     * @author Cecilie Krog Drejer, s185032
     * @author Frederik Lykke Ullstad, s185018
     * @author Helle Achari, s180317
     * @author Joakim Bøegh Levorsen, s185023
     */
    fun jailTurn(player: Player) {
        val r = DiceRoll()
        if (r.isDoubles) {
            view.gui.showMessage(jsonData.getString(JSONKey.ROLLED_DOUBLE.key))
            for (space in game.getBoard()) {
                if (space is JailSpace) {
                    space.release(player)
                }
            }
        } else {
            if (view.gui.getUserLeftButtonPressed(jsonData.getString(JSONKey.OUT_OF_JAIL.key),
                            jsonData.getString(JSONKey.YES.key), jsonData.getString(JSONKey.NO.key))) {
                val jailCard = player.getGetOutOfJailCard(game)
                if (jailCard == null) {
                    player.changeAccountBalance(-100)
                } else
                    jailCard.setOwner(null)
                game.getSpacesForType(JailSpace::class)[0].release(player)
            }
        }
    }

    /**
     * DiceRoll: Klasse, der står for at slå med terningerne Klassens metoder bruges
     * kun i GameController, hvorfor den ligger her
     *
     * @author Anders Brandt, s185016
     * @author Cecilie Krog Drejer, s185032
     * @author Frederik Lykke Ullstad, s185018
     * @author Helle Achari, s180317
     * @author Joakim Bøegh Levorsen, s185023
     */
    private inner class DiceRoll {
        private val roll1: Int
        private val roll2: Int

        /**
         * @return Returnerer om et slag består at to ens eller ej
         */
        val isDoubles: Boolean
            get() = roll1 == roll2

        init {
            this.roll1 = (Math.random() * 6 + 1).toInt()
            this.roll2 = (Math.random() * 6 + 1).toInt()
            view.gui.setDice(roll1, roll2)
        }

        /**
         * @return Returnerer summen af de to terningers øjne
         */
        fun sum(): Int {
            return roll1 + roll2
        }
    }

    /**
     * PlayersLeft: Holder styr på hvor mange spillere, der er tilbage i spiller
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer hvor mange spillere der er tilbage i spillet.
     */
    fun playersLeft(): Int {
        var count = 0
        for (player in game.getPlayers()) {
            if (!player.isBroke)
                count++
        }
        return count
    }
}
