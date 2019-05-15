package monopoly.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import designpatterns.Observer;
import org.javalite.activejdbc.Model;
import org.json.JSONException;
import org.json.JSONObject;

import designpatterns.Subject;
import monopoly.model.cards.CardStack;
import monopoly.model.spaces.DatabaseSpaceFactory;
import resources.json.JSONFile;
import resources.json.JSONKey;
import resources.json.ResourceManager;
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
        CURRENT_TURN("currentTurn"), SAVE_NAME("saveName"), JSON_PACK("jsonPack");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    public static Game newGame(String saveName, JSONFile languagePack, JSONObject jsonData, int playerCount) {
        Game g = new Game();
        g.set(Game.Properties.CURRENT_TURN.getProperty(), 0);
        g.set(Game.Properties.SAVE_NAME.getProperty(), saveName);
        g.set(Properties.JSON_PACK.getProperty(), languagePack.getPackName());
        CardStack chanceStack = CardStack.create(jsonData.getJSONObject(JSONKey.CHANCE_CARDS.getKey()), g, true, 0);
        CardStack communityStack = CardStack.create(jsonData.getJSONObject(JSONKey.COMMUNITY_CHEST_CARDS.getKey()), g,
                false, 0);
        JSONSpaceFactory.createSpaces(jsonData, g, chanceStack, communityStack);
        for (int i = 0; i < playerCount; i++) {
            Player newPlayer = Player.newPlayer("Player " + (i + 1), i, 2000);
            g.addPlayer(newPlayer);
        }
        return g;
    }

    public void deleteThisGame() {
        this.deleteCascade();
    }

    public static List<Game> gameList() {
        return Game.findAll().orderBy(Game.Properties.CURRENT_TURN.getProperty() + " asc").load();
    }

    /*
     * saveIt: Overskriver saveIt for game, men kalder den på alle dens "børne"
     * elementer.
     *
     * @Author Anders Brandt, s185016
     */
    @Override
    public boolean saveIt() {

        if (super.saveIt() == false) {
            return false;
        }

        if (players != null) {
            for (Player player : players) {
                if (player.saveIt() == false) {
                    return false;
                }
            }
        }

        if (stacks != null) {
            for (CardStack cardStack : stacks) {
                if (cardStack.saveIt() == false) {
                    return false;
                }
            }
        }

        if (board != null) {
            for (Space space : board) {
                if (space.saveIt() == false) {
                    return false;
                }
            }
        }
        return true;
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

    public <C extends Space> List<C> getSpacesForType(Class<C> type) {
        List<C> matches = new ArrayList<C>();
        for (Space space : getBoard()) {
            if (type.isInstance(space)) {
                matches.add((C) space);
            }
        }
        return matches;
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
        this.updated();
    }

    public JSONObject getLanguageData() throws JSONException {
        String packName = this.getString(Properties.JSON_PACK.getProperty());
        JSONFile file = JSONFile.getFile(packName);
        if (file == null)
            throw new JSONException("Json pakken med navnet " + packName + " was not found");
        ResourceManager rm = new ResourceManager();
        return rm.readFile(file);
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
