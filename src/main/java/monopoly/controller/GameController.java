package monopoly.controller;

import monopoly.DatabaseBase;
import monopoly.model.Game;
import monopoly.model.Player;
import monopoly.model.cards.GetOutOfJailCard;
import monopoly.model.spaces.JailSpace;
import monopoly.model.spaces.Space;
import monopoly.view.View;
import org.javalite.activejdbc.validation.ValidationException;
import org.json.JSONObject;
import resources.json.JSONKey;

/**
 * GameController står for at spille spillet og holde styr på hvem der vinder
 * 
 * @author Anders Brandt, s185016
 * @author Cecilie Krog Drejer, s185032
 * @author Frederik Lykke Ullstad, s185018
 * @author Helle Achari, s180317
 * @author Joakim Bøegh Levorsen, s185023
 */
public class GameController {
    public CashController cashController;
    public MovementController movementController;
    public PropertyController propertyController;
    public View view;
    private Game game;
    private JSONObject jsonData;

    /**
     * GameController constructor
     * 
     * @param game Spillet, som skal kontrolleres
     * @param view View'et, som tilhører spillet
     */
    public GameController(Game game, View view) {
        this.game = game;
        this.view = view;
        this.jsonData = game.getLanguageData();
        this.movementController = new MovementController(this);
        this.cashController = new CashController(this);
        this.propertyController = new PropertyController(this);
    }

    /**
     * GetGame: Henter spillet associeret med GameController
     * 
     * @return Returnerer spillet associeret med GameController
     */
    public Game getGame() {
        return game;
    }

    /**
     * Play: Spiller spillet
     * 
     * @author Anders Brandt, s185016
     * @author Cecilie Krog Drejer, s185032
     * @author Frederik Lykke Ullstad, s185018
     * @author Helle Achari, s180317
     * @author Joakim Bøegh Levorsen, s185023
     */
    public void play() {
        do {
            while (playersLeft() > 1) {
                int currentPlayerTurn = game.getCurrentTurn();
                Player playerWithTurn = getGame().getPlayers().get(currentPlayerTurn);
                // Hvis spilleren er gået konkurs, ignorer dem
                if (!playerWithTurn.isBroke()) {
                    if (playerWithTurn.isInJail()) {
                        jailTurn(playerWithTurn);
                    }
                    // Spilleren er måske kommet ud, ellers perform turn
                    if (!playerWithTurn.isInJail()) {
                        DiceRoll r;
                        int doubleCount = 0;
                        do {
                            view.getGUI().showMessage(
                                    playerWithTurn.getName() + jsonData.getString(JSONKey.ROLL_DICE.getKey()));
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
                        propertyController.unmortgageProperties(playerWithTurn);
                    }
                    // Hvis spilleren ikke har flere penge start konkurs flow
                    if (playerWithTurn.hasOverdrawnAccount()) {
                        propertyController.playerBroke(playerWithTurn);
                    }
                }
                incrementTurn(currentPlayerTurn);
                saveGame();
            }
            view.getGUI().showMessage(getWinner().getName() + jsonData.getString(JSONKey.GAME_WINNER.getKey()));
        } while (view.getGUI().getUserLeftButtonPressed(jsonData.getString(JSONKey.PLAY_AGAIN.getKey()),
                jsonData.getString(JSONKey.YES.getKey()), jsonData.getString(JSONKey.NO.getKey())));
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

    /**
     * IncrementTurn: Holder styr på hvilken spiller, der har tur
     * 
     * @param currentPlayerTurn index på den spiller, der har tur nu
     * 
     * @author Joakim Bøegh Levorsen, s185023
     */
    public void incrementTurn(int currentPlayerTurn) {
        int nextPlayerTurn = currentPlayerTurn + 1;
        if (nextPlayerTurn == game.getPlayers().size()) {
            nextPlayerTurn = 0;
        }
        game.setCurrentTurn(nextPlayerTurn);
        System.out.println("Next turn is " + game.getCurrentTurn());
    }

    /**
     * JailTurn: Tager en spiller igennem processen at forsøge at undslippe fra
     * fængsel
     * 
     * @param player spilleren, som skal igennem fængsels-flow'et
     * 
     * @author Anders Brandt, s185016
     * @author Cecilie Krog Drejer, s185032
     * @author Frederik Lykke Ullstad, s185018
     * @author Helle Achari, s180317
     * @author Joakim Bøegh Levorsen, s185023
     */
    public void jailTurn(Player player) {
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
                    player.changeAccountBalance(-100);
                } else
                    jailCard.setOwner(null);
                game.getSpacesForType(JailSpace.class).get(0).release(player);
            }
        }
    }

    /**
     * DiceRoll: Klasse, der står for at slå med terningerne Klassens metoder bruges
     * kun i GameController, hvorfor den ligger her
     * 
     * @author Anders Brandt, s185016
     * @author Cecilie Krog Drejer, s185032
     * @author Frederik Lykke Ullstad, s185018
     * @author Helle Achari, s180317
     * @author Joakim Bøegh Levorsen, s185023
     */
    private class DiceRoll {
        private int roll1;
        private int roll2;

        public DiceRoll() {
            this.roll1 = (int) (Math.random() * 6 + 1);
            this.roll2 = (int) (Math.random() * 6 + 1);
            view.getGUI().setDice(roll1, roll2);
        }

        /**
         * @return Returnerer om et slag består at to ens eller ej
         */
        public boolean isDoubles() {
            return roll1 == roll2;
        }

        /**
         * @return Returnerer summen af de to terningers øjne
         */
        public int sum() {
            return roll1 + roll2;
        }
    }

    /**
     * GetWinner: Finder vinderen af spiller
     * 
     * @author Cecilie Krog Drejer, s185032
     * 
     * @return Returnerer vinderen af spillet.
     */
    public Player getWinner() {
        Player winner = null;
        for (Player player : getGame().getPlayers()) {
            if (!player.isBroke())
                winner = player;
        }
        return winner;
    }

    /**
     * PlayersLeft: Holder styr på hvor mange spillere, der er tilbage i spiller
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer hvor mange spillere der er tilbage i spillet.
     */
    public int playersLeft() {
        int count = 0;
        for (Player player : getGame().getPlayers()) {
            if (!player.isBroke())
                count++;
        }
        return count;
    }
}
