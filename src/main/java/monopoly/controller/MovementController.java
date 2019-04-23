package monopoly.controller;

import gui_main.GUI;
import monopoly.model.Player;
import monopoly.model.spaces.Space;

public class MovementController {
    public GameController controller;
    private GUI gui;
    private Player player;

    public MovementController(GameController owner) {
        this.controller = owner;
    }

    public void moveForward(int amount) {
        // TODO: Implementering
        doubles = (die1 == die2);
        gui.setDice(die1, die2);
        amount = die1 + die2;

       int moveAmount = player.getBoardPosition() + amount;

       if (moveAmount > 40) {
           moveAmount =- 40;
           player.setBoardPosition(moveAmount);
       } else {
           player.setBoardPosition(moveAmount);
       }


    }

    public void goToJail() {
        // TODO: Implementering

    }

    public void goTo(Space space) {
        // TODO: Implementering

    }
}
