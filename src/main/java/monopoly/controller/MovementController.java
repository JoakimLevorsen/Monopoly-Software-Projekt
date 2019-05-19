package monopoly.controller;

import monopoly.model.Player;
import monopoly.model.spaces.JailSpace;
import monopoly.model.spaces.Space;
import monopoly.model.spaces.StartSpace;

/**
 * MovementController: Kontrollerer de forskellige måder at rykke spilleren
 *
 * @author Anders Brandt, s185016
 */
public class MovementController {
    public GameController controller;

    /**
     * MovementController constructor
     * 
     * @param owner GameController, som "ejer" MovementController
     * 
     * @author Anders Brandt, s185016
     */
    public MovementController(GameController owner) {
        this.controller = owner;
    }

    /**
     * MoveForward: Rykker spilleren frem ud fra hvad de har slået med terningen.
     *
     * @param amount Antal felter, spilleren skal rykkes
     * @param player Spilleren, som skal rykkes
     * 
     * @author Anders Brandt, s185016
     */
    public void moveForward(int amount, Player player) {
        int moveAmount = player.getBoardPosition() + amount;

        if (moveAmount >= 40) {
            moveAmount -= 40;
            StartSpace s = (StartSpace) controller.getGame().getAll(StartSpace.class).load().get(0);
            player.changeAccountBalance(s.getPayment());
        }
        player.setBoardPosition(moveAmount);
        Space destination = controller.getGame().getBoard().get(player.getBoardPosition());
        destination.performAction(controller, player);
    }

    /**
     * GoToJail: Rykker spilleren i fængsel
     *
     * @param player Spilleren, som skal i fængsel
     * 
     * @author Anders Brandt, s185016
     */
    public void goToJail(Player player) {
        JailSpace j = (JailSpace) controller.getGame().getAll(JailSpace.class).load().get(0);
        player.setBoardPosition(j.getBoardPosition());
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
    public void goTo(Space space, Boolean ignoreStart, Player player) {
        player.setBoardPosition(space.getBoardPosition());
        space.performAction(controller, player);
        if (player.getBoardPosition() > space.getBoardPosition() && !ignoreStart) {
            StartSpace s = (StartSpace) controller.getGame().getAll(StartSpace.class).load().get(0);
            player.changeAccountBalance(s.getPayment());
        }
    }

}
