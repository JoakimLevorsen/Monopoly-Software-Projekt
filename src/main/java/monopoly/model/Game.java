package monopoly.model;

import java.awt.Color;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import designpatterns.Observer;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.ValidationException;
import org.json.JSONException;
import org.json.JSONObject;

import designpatterns.Subject;
import gui_fields.*;
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
    private static String[] colors = { "FF0000", "0000FF", "008000", "FFFF00" };

    public enum Properties {
        CURRENT_TURN("currentTurn"), SAVE_NAME("saveName"), JSON_PACK("jsonPack"), UPDATED_AT("updated_at");

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
        g.save();
        CardStack chanceStack = CardStack.create(jsonData, g, true, 0);
        CardStack communityStack = CardStack.create(jsonData, g, false, 0);
        JSONSpaceFactory.createSpaces(jsonData, g, chanceStack, communityStack);
        for (int i = 0; i < playerCount; i++) {
            Player newPlayer = Player.newPlayer("Player " + (i + 1), i, 2000, colors[i]);
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

        try {
            super.saveIt();
        } catch (ValidationException e) {
            e.printStackTrace();
            return false;
        }

        if (players != null) {
            for (Player player : players) {
                try {
                    player.saveIt();
                } catch (ValidationException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        if (stacks != null) {
            for (CardStack cardStack : stacks) {
                try {
                    cardStack.saveIt();
                } catch (ValidationException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        if (board != null) {
            for (Space space : board) {
                try {
                    space.saveIt();
                } catch (ValidationException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    public GUI_Field[] exportGUIFields() {
        List<Space> board = this.getBoard();
        GUI_Field[] guiBoard = new GUI_Field[board.size()];
        for (int i = 0; i < board.size(); i++) {
            Space space = board.get(i);
            if (space instanceof PropertySpace) {
                PropertySpace pS = (PropertySpace) space;
                guiBoard[i] = new GUI_Street(pS.getName(), getLanguageData().getString(JSONKey.RENT.getKey()) + " "  + String.valueOf(pS.getRent(this)), pS.getName(), "",
                        pS.getColor(), Color.WHITE);
            } else if (space instanceof StationSpace) {
                StationSpace sS = (StationSpace) space;
                guiBoard[i] = new GUI_Shipping("default", sS.getName(), "", "", String.valueOf(sS.getRent(this)),
                        sS.getColor(), Color.WHITE);
            } else if (space instanceof FreeParkingSpace) {
                FreeParkingSpace fS = (FreeParkingSpace) space;
                guiBoard[i] = new GUI_Refuge("default", fS.getName(), fS.getName(), "", fS.getColor(), Color.BLACK);
            } else if (space instanceof JailSpace) {
                JailSpace jS = (JailSpace) space;
                guiBoard[i] = new GUI_Jail("default", jS.getName(), jS.getName(), "", jS.getColor(), Color.BLACK);
            } else if (space instanceof GoToJailSpace) {
                GoToJailSpace gS = (GoToJailSpace) space;
                guiBoard[i] = new GUI_Jail("default", gS.getName(), gS.getName(), "", gS.getColor(), Color.BLACK);
            } else if (space instanceof CardSpace) {
                CardSpace cS = (CardSpace) space;
                guiBoard[i] = new GUI_Chance("?", cS.getName(), "", cS.getColor(), Color.BLACK);
            } else if (space instanceof TaxSpace) {
                TaxSpace tS = (TaxSpace) space;
                guiBoard[i] = new GUI_Tax(tS.getName(), String.valueOf(tS.getTax()), "", tS.getColor(), Color.white);
            } else if (space instanceof StartSpace) {
                StartSpace sS = (StartSpace) space;
                guiBoard[i] = new GUI_Start(sS.getName(), sS.getName(), "", sS.getColor(), Color.BLACK);
            } else {
                System.out.println("Item not put on board");
            }
        }
        return guiBoard;
    }

    public void addPlayer(Player player) {
        if (this.players == null) this.getPlayers();
        this.players.add(player);
        this.add(player);
    }

    public List<Player> getPlayers() {
        if (this.players == null) {
            this.players = this.getAll(Player.class).load();
        }
        // Sender kopi af listen så man ikke kan ændre den
        return new ArrayList<Player>(this.players);
    }

    public Player getPlayerForID(long id) {
        List<Player> players = this.getPlayers();
        for (Player p : players) {
            if (p.getLongId().longValue() == id) {
                return p;
            }
        }
        return null;
    }

    public <S extends Space> List<S> getSpacesForType(Class<S> type) {
        List<S> matches = new ArrayList<S>();
        for (Space space : getBoard()) {
            if (type.isInstance(space)) {
                matches.add((S) space);
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

    public CardStack getStackForID(long id) {
        List<CardStack> stacks = this.getCardStacks();
        for (CardStack c : stacks) {
            if (c.getLongId().longValue() == id) {
                return c;
            }
        }
        return null;
    }

    public String getGameName() {
        return (String) this.get(Properties.SAVE_NAME.getProperty());
    }

    public Timestamp getUpdateTime() {
        return this.getTimestamp(Properties.UPDATED_AT.getProperty());
    }

    public int getCurrentTurn() {
        return (int) this.get(Properties.CURRENT_TURN.getProperty());
    }

    public List<Space> getOwnedSpaces(Player player) {
        List<Space> board = this.getBoard();
        List<Space> ownedProperty = new ArrayList<Space>();
        for (Space boardSpace : board) {
            if (boardSpace instanceof PropertySpace) {
                Player owner = ((PropertySpace) boardSpace).getOwner(this);
                if (owner != null) {
                    long ownerID = owner.getLongId();
                    if (player.getLongId().longValue() == ownerID)
                        ownedProperty.add(boardSpace);
                }
            } else if (boardSpace instanceof StationSpace) {
                Player owner = ((StationSpace) boardSpace).getOwner(this);
                if (owner != null) {
                    long ownerID = owner.getLongId();
                    if (player.getLongId().longValue() == ownerID)
                        ownedProperty.add(boardSpace);
                }
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
