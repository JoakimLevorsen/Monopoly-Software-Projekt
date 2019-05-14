package monopoly.controller;

import monopoly.model.Game;
import monopoly.model.Player;
import monopoly.model.cards.*;
import monopoly.model.spaces.*;
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
        while(playersLeft() > 1) {
            int currentPlayerTurn = game.getCurrentTurn();
            Player playerWithTurn = getGame().getPlayers().get(currentPlayerTurn);
            // Hvis spilleren er gået konkurs, ignorer dem
            if (!playerWithTurn.isBroke()) {
                if (playerWithTurn.isInJail()) {
                    prisonTurn(playerWithTurn);
                }
                // Spilleren er måske kommet ud, ellers perform turn
                if (!playerWithTurn.isInJail()) {
                    DiceRoll r;
                    int doubleCount = 0;
                    do {
                        r = new DiceRoll();
                        if (doubleCount == 2 && r.isDoubles()) {
                            for (Space space: game.getBoard()) {
                                if (space instanceof JailSpace) {
                                    ((JailSpace)space).jail(playerWithTurn);
                                }
                            }
                        } else {
                            movementController.moveForward(r.sum(), playerWithTurn);
                        }
                        doubleCount++;
                    } while (r.isDoubles() && doubleCount < 2);
                }
                // Kom spiller i fængsel i sit ryk?
                if (!playerWithTurn.isInJail()) {
                    propertyController.trade();
                    propertyController.offerToBuild();
                }
            } 
            incrementTurn(currentPlayerTurn);
        }
    }

    public void incrementTurn(int currentPlayerTurn) {
        if (currentPlayerTurn + 1 == game.getPlayers().size()) {
            game.setCurrentTurn(0);
        } else {
            game.setCurrentTurn(currentPlayerTurn + 1);
        }
    }

    public void prisonTurn(Player player) {
        DiceRoll r = new DiceRoll();
        if (r.isDoubles()) {
            view.getGUI().showMessage("Du slog dobbelt og kom gratis ud");
            for (Space space: game.getBoard()) {
                if (space instanceof JailSpace) {
                    ((JailSpace)space).release(player);
                }
            }
        } else {
            if (view.getGUI().getUserLeftButtonPressed("Vil du ud af fængsel?", "Ja", "Nej")) {
                // TODO: Alt det her
            }
        }
    }

    private class DiceRoll {
        int roll1;
        int roll2;

        public DiceRoll() {
            this.roll1 = (int)(Math.random() * 6 + 1);
            this.roll2 = (int)(Math.random() * 6 + 1);
            view.getGUI().setDice(roll1, roll2);
        }

        public boolean isDoubles() {
            return roll1 == roll2;
        }

        public int sum() {return roll1 + roll2;}
    }

    public int playersLeft() {
        int count = 0;
        for (Player player : getGame().getPlayers()) {
            if (!player.isBroke()) count++;
        }
        return count;
    }
}
