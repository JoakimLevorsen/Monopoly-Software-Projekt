package monopoly.controller;

import monopoly.model.Player;
import monopoly.model.spaces.JailSpace;
import monopoly.model.spaces.Space;
import monopoly.model.spaces.StartSpace;

public class MovementController {
    public GameController controller;


    public MovementController(GameController owner) {
        this.controller = owner;
    }

    public void moveForward(int amount, Player player) {
       int moveAmount = player.getBoardPosition() + amount;

       if (moveAmount > 40) {
           moveAmount =- 40;
           StartSpace s = (StartSpace) controller.getGame().getAll(StartSpace.class).load().get(0);
           player.changeAccountBalance(s.getPayment());
       }
       player.setBoardPosition(moveAmount);


    }



    public void goToJail(Player player) {
        JailSpace j = (JailSpace) controller.getGame().getAll(JailSpace.class).load().get(0);
        player.setBoardPosition(j.getBoardPosition());
    }

    public void goTo(Space space, Boolean ignoreStart, Player player) {
        player.setBoardPosition(space.getBoardPosition());
        //TODO: Tjek for start kryds
    }
}
