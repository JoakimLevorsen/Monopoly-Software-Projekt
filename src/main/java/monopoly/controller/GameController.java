package monopoly.controller;

import monopoly.model.Game;
import monopoly.model.Player;
import monopoly.model.cards.Card;
import monopoly.view.View;

public class GameController {
    public CashController cashController = new CashController(this);
    public MovementController movementController = new MovementController(this);
    public PropertyController propertyController = new PropertyController(this);
    public View view;
    private Game game;

    public GameController(Game game, View view) {
        this.game = game;
        this.view = view;
    }

    public Game getGame() {
        return game;
    }

    public void play() {
        // TODO: Implementering

    }

    public void takeCard(Card card, Player player) {
        // TODO: Implementering
    }
}
