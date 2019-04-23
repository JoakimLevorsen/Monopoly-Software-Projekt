package monopoly.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import designpatterns.Observer;
import org.javalite.activejdbc.Model;

import designpatterns.Subject;
import monopoly.model.cards.CardStack;
import monopoly.model.spaces.DatabaseSpaceFactory;
import monopoly.model.spaces.*;

/*
Game:
Implementering af Game model objektet, med ORM for databasen.

@author Joakim Levorsen, S185023
*/
public class Game extends Model implements Subject {
    private List<Player> players = null;
    private List<CardStack> stacks = null;
    private List<Space> board = null;

    public enum Properties {
        CURRENT_TURN("currentTurn"), SAVE_NAME("saveName");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    public static Game newGame(String saveName) {
        Game g = new Game();
        g.set(Game.Properties.CURRENT_TURN.getProperty(), 0);
        g.set(Game.Properties.SAVE_NAME.getProperty(), saveName);

        return g;
    }

    public void deleteThisGame() {
        this.deleteCascade();
    }

    public static List<Game> gameList() {
        return Game.findAll().orderBy(Game.Properties.CURRENT_TURN.getProperty() + " asc").load();
    }

    public void addPlayer(Player player) {
        this.add(player);
        this.players.add(player);

    }

    public List<Player> getPlayers() {
        if (this.players == null) {
            this.players = this.getAll(Player.class).load();
        }
        return this.players;
    }

    public Player getPlayerForID(int id) {
        List<Player> players = this.getPlayers();
        for (Player p : players) {
            if ((int) p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public List<CardStack> getCardStacks() {
        if (this.stacks == null) {
            this.stacks = this.getAll(CardStack.class).load();
        }
        return this.stacks;
    }

    public List<Space> getBoard() {
        if (this.board == null) {
            this.board = DatabaseSpaceFactory.getSpacesFor(this);
        }
        return this.board;
    }

    public CardStack getStackForID(int id) {
        List<CardStack> stacks = this.getCardStacks();
        for (CardStack c : stacks) {
            if ((int) c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public String getGameName() {
        return (String) this.get(Game.Properties.SAVE_NAME.getProperty());
    }

    public int getCurrentTurn() {
        return (int) this.get(Game.Properties.CURRENT_TURN.getProperty());
    }

    public List<Space> getOwnedSpaces(Player player) {
        List<Space> board = this.getBoard();
        List<Space> ownedProperty = new ArrayList<Space>();
        for (Space boardSpace : board) {
            if (boardSpace instanceof PropertySpace) {
                long ownerID = ((PropertySpace) boardSpace).getOwner(this).getLongId();
                if (player.getLongId() == ownerID)
                    ownedProperty.add(boardSpace);
            } else if (boardSpace instanceof StationSpace) {
                long ownerID = ((StationSpace) boardSpace).getOwner(this).getLongId();
                if (player.getLongId() == ownerID)
                    ownedProperty.add(boardSpace);
            }
        }
        return ownedProperty;
    }

    public void setCurrentTurn(int turn) {
        this.set(Game.Properties.CURRENT_TURN.getProperty(), turn);
    }

    /**
     * Variabler og metoder til at implementere Subject
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     *
     */
    private Set<Observer> observers = new HashSet<Observer>();

    final public void addObserver(Observer observer) {
        observers.add(observer);
    }

    final public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /*
     * @author Helle Achari, s180317
     */

    public Set<Observer> getObservers() {
        return observers;
    }
}
