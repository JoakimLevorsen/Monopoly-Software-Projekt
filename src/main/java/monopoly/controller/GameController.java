package monopoly.controller;

import monopoly.model.Game;
import monopoly.model.Player;
import monopoly.model.cards.Card;

public class GameController {
    public CashController cashController = new CashController(this);
    public MovementController movementController = new MovementController(this);
    public PropertyController propertyController = new PropertyController(this);

    public Game getGame() {
        // TODO: faktist retuner et game
        return null;
    }

    public void play() {
        // TODO: Implementering

    }

    public void takeCard(Card card, Player player) {
        // TODO: Implementering
    }
}
