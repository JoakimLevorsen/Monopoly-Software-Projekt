package monopoly.controller

import monopoly.model.Player
import monopoly.model.spaces.JailSpace
import monopoly.model.spaces.Space
import monopoly.model.spaces.StartSpace

/**
 * MovementController: Kontrollerer de forskellige måder at rykke spilleren
 *
 * @author Anders Brandt, s185016
 */
class MovementController
/**
 * MovementController constructor
 *
 * @param owner GameController, som "ejer" MovementController
 *
 * @author Anders Brandt, s185016
 */
(var controller: GameController) {

    /**
     * MoveForward: Rykker spilleren frem ud fra hvad de har slået med terningen.
     *
     * @param amount Antal felter, spilleren skal rykkes
     * @param player Spilleren, som skal rykkes
     *
     * @author Anders Brandt, s185016
     */
    fun moveForward(amount: Int, player: Player) {
        var moveAmount = player.boardPosition + amount

        if (moveAmount >= 40) {
            moveAmount -= 40
            val s = controller.game.getSpacesForType(StartSpace::class)[0]
            player.changeAccountBalance(s.payment)
        }
        player.boardPosition = moveAmount
        val destination = controller.game.getBoard()[player.boardPosition]
        destination.performAction(controller, player)
    }

    /**
     * GoToJail: Rykker spilleren i fængsel
     *
     * @param player Spilleren, som skal i fængsel
     *
     * @author Anders Brandt, s185016
     */
    fun goToJail(player: Player) {
        val j = controller.game.getAll<JailSpace>(JailSpace::class.java).load<JailSpace>()[0]
        player.boardPosition = j.boardPosition
    }

    /**
     * GoTo: Flytter spilleren til et bestemt felt.
     *
     * @param space Hvilket felt, der skal flyttes til
     * @param ignoreStart Om spilleren skal modtage §200 når de passerer Start eller ej
     * @param player Spilleren, der skal flyttes
     *
     * @author Anders Brandt, s185016
     */
    fun goTo(space: Space, ignoreStart: Boolean?, player: Player) {
        player.boardPosition = space.boardPosition
        space.performAction(controller, player)
        if (player.boardPosition > space.boardPosition && (ignoreStart == false)!!) {
            val s = controller.game.getAll<StartSpace>(StartSpace::class.java).load<StartSpace>()[0] as StartSpace
            player.changeAccountBalance(s.payment)
        }
    }

}
