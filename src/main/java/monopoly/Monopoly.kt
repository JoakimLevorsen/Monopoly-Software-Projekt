package monopoly

import gui_main.GUI
import monopoly.controller.GameController
import monopoly.model.Game
import monopoly.view.View

/**
 * Monopoly: Klasse fra hvilken softwaren køres
 *
 * @author Joakim Bøegh Levorsen, s185023
 */
object Monopoly {
    /**
     * Main: Kører softwaren
     *
     * @param args
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    @JvmStatic
    fun main(args: Array<String>) {
        DatabaseBase.openBase()
        val ourGame = View.chooseGame()
        if (ourGame != null) {
            val gameGUI = GUI(ourGame.exportGUIFields())

            val gameView = View(ourGame, gameGUI)
            val controller = GameController(ourGame, gameView)
            controller.play()
        } else System.out.println("Game start failed")

        DatabaseBase.closeBase()
    }
}
