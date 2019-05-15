package monopoly.controller;

import monopoly.model.Player;
import monopoly.model.spaces.JailSpace;
import monopoly.model.spaces.Space;
import monopoly.model.spaces.StartSpace;

public class MovementController {
    public GameController controller;

    /*
     * MovementController: kontrollerer de forskellige måder at rykke spilleren
     *
     * @Author Anders Brandt, s185016
     */
    public MovementController(GameController owner) {
        this.controller = owner;
    }

    /*
     * moveForward: rykker spilleren frem ud fra hvad de har slået med terningen.
     *
     * @Author Anders Brandt, s185016
     */
    public void moveForward(int amount, Player player) {
        int moveAmount = player.getBoardPosition() + amount;

        if (moveAmount > 40) {
            moveAmount = -40;
            StartSpace s = (StartSpace) controller.getGame().getAll(StartSpace.class).load().get(0);
            player.changeAccountBalance(s.getPayment());
        }
        player.setBoardPosition(moveAmount);
        Space destination = controller.getGame().getBoard().get(player.getBoardPosition());
        destination.performAction(controller, player);
    }

    /*
     * goToJail: rykker spilleren i fængsel
     *
     * @Author Anders Brandt, s185016
     */
    public void goToJail(Player player) {
        JailSpace j = (JailSpace) controller.getGame().getAll(JailSpace.class).load().get(0);
        player.setBoardPosition(j.getBoardPosition());
    }

    /*
     * goTo: rykker spilleren til et bestemt felt.
     *
     * @Author Anders Brandt, s185016
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
