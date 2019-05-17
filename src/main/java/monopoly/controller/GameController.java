package monopoly.controller;

import monopoly.DatabaseBase;
import monopoly.model.Game;
import monopoly.model.Player;
import monopoly.model.cards.GetOutOfJailCard;
import monopoly.model.spaces.*;
import monopoly.view.View;
import resources.json.JSONKey;

import org.javalite.activejdbc.validation.ValidationException;
import org.json.JSONObject;

public class GameController {
    public CashController cashController;
    public MovementController movementController;
    public PropertyController propertyController;
    public View view;
    private Game game;
    private JSONObject jsonData;

    public GameController(Game game, View view) {
        this.game = game;
        this.view = view;
        this.jsonData = game.getLanguageData();
        this.movementController = new MovementController(this);
        this.cashController = new CashController(this);
        this.propertyController = new PropertyController(this);
    }

    public Game getGame() {
        return game;
    }

    public void play() {
        do {
            while (playersLeft() > 1) {
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
                            view.getGUI()
                                    .showMessage(playerWithTurn.getName() + jsonData.getString(JSONKey.ROLL_DICE.getKey()));
                            r = new DiceRoll();
                            if (doubleCount == 2 && r.isDoubles()) {
                                for (Space space : game.getBoard()) {
                                    if (space instanceof JailSpace) {
                                        ((JailSpace) space).jail(playerWithTurn);
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
                        propertyController.trade(playerWithTurn);
                        propertyController.offerToBuild(playerWithTurn);
                    }
                }
                incrementTurn(currentPlayerTurn);
                saveGame();
            }
            // TODO: JSON this shit up
            view.getGUI().showMessage(getWinner().getName() + " won the game! Congratulations!");
        } while (view.getGUI().getUserLeftButtonPressed("Do you want to play another game", "Yes", "No"));
    }

    public void saveGame() {
        try {
            DatabaseBase.openTransaction();
            game.saveIt();
            DatabaseBase.commitTransaction();
        } catch (ValidationException e) {
            System.out.println("Save of game failed");
            DatabaseBase.rollBackTransaction();
            e.printStackTrace();
        }
    }

    public void incrementTurn(int currentPlayerTurn) {
        int nextPlayerTurn = currentPlayerTurn + 1;
        if (nextPlayerTurn == game.getPlayers().size()) {
            nextPlayerTurn = 0;
        }
        game.setCurrentTurn(nextPlayerTurn);
        System.out.println("Next turn is " + game.getCurrentTurn());
    }

    public void prisonTurn(Player player) {
        DiceRoll r = new DiceRoll();
        if (r.isDoubles()) {
            view.getGUI().showMessage(jsonData.getString(JSONKey.ROLLED_DOUBLE.getKey()));
            for (Space space : game.getBoard()) {
                if (space instanceof JailSpace) {
                    ((JailSpace) space).release(player);
                }
            }
        } else {
            if (view.getGUI().getUserLeftButtonPressed(jsonData.getString(JSONKey.OUT_OF_JAIL.getKey()),
                    jsonData.getString(JSONKey.YES.getKey()), jsonData.getString(JSONKey.NO.getKey()))) {
                GetOutOfJailCard jailCard = player.getGetOutOfJailCard(game);
                if (jailCard == null) {
                    player.changeAccountBalance(-50);
                } else
                    jailCard.setOwner(null);
                game.getSpacesForType(JailSpace.class).get(0).release(player);
            }
        }
    }

    private class DiceRoll {
        int roll1;
        int roll2;

        public DiceRoll() {
            this.roll1 = (int) (Math.random() * 6 + 1);
            this.roll2 = (int) (Math.random() * 6 + 1);
            view.getGUI().setDice(roll1, roll2);
        }

        public boolean isDoubles() {
            return roll1 == roll2;
        }

        public int sum() {
            return roll1 + roll2;
        }
    }

    public Player getWinner() {
        Player winner = null;
        for (Player player : getGame().getPlayers()) {
            if (!player.isBroke())
                winner = player;
        }
        return winner;
    }

    public int playersLeft() {
        int count = 0;
        for (Player player : getGame().getPlayers()) {
            if (!player.isBroke())
                count++;
        }
        return count;
    }
}
