package monopoly.model;

import designpatterns.Observer;
import designpatterns.Subject;
import gui_fields.*;
import monopoly.model.cards.CardStack;
import monopoly.model.spaces.*;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.ValidationException;
import org.json.JSONException;
import org.json.JSONObject;
import resources.json.JSONFile;
import resources.json.JSONKey;
import resources.json.ResourceManager;

import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Game: Implementering af Game model objektet, med ORM for databasen.
 *
 * @author Joakim Bøegh Levorsen, s185023
 * @author Anders Brandt, s185016
 * @author Cecilie Krog Drejer, s185032
 * @author Helle Achari, s180317
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Game extends Model implements Subject {
    private List<Player> players = null;
    private List<CardStack> stacks = null;
    private List<Space> board = null;

    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
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

    /**
     * NewGame: Opretter et nyt spil
     * 
     * @param saveName Navn, som spillet skal gemmes under
     * @param languagePack .json-fil, som tekst skal læses fra
     * @param jsonData Data fra JSON, som bruges til at lave felter og kort
     * @param playerCount Antal spillere
     *
     * @author Joakim Bøegh Levorsen, s185023
     * @author Cecilie Krog Drejer, s185032
     * 
     * @return Returnerer et nyt spil
     */
    public static Game newGame(String saveName, JSONFile languagePack, JSONObject jsonData, int playerCount) {
        Game g = new Game();
        g.set(Game.Properties.CURRENT_TURN.getProperty(), 0);
        g.set(Game.Properties.SAVE_NAME.getProperty(), saveName);
        g.set(Properties.JSON_PACK.getProperty(), languagePack.getPackName());
        g.save();
        CardStack chanceStack = CardStack.create(jsonData, g, true, 0);
        CardStack communityStack = CardStack.create(jsonData, g, false, 0);
        JSONSpaceFactory.createSpaces(jsonData, g, chanceStack, communityStack);
        String[] colors = { "FF0000", "0000FF", "008000", "FFFF00" };
        for (int i = 0; i < playerCount; i++) {
            Player newPlayer = Player.newPlayer(jsonData.getString(JSONKey.PLAYER.getKey()) + (i + 1), i, 2000, colors[i]);
            g.addPlayer(newPlayer);
        }
        return g;
    }

    /**
     * DeleteThisGame: Sletter et spil og alle dets børn fra databasen.
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    public void deleteThisGame() {
        this.deleteCascade();
    }

    public static List<Game> gameList() {
        return Game.findAll().orderBy(Game.Properties.CURRENT_TURN.getProperty() + " asc").load();
    }

    /**
     * SaveIt: Overskriver saveIt() for game, men kalder den på alle dens "børne"-elementer.
     *
     * @author Anders Brandt, s185016
     * 
     * @return Returnerer hvorvidt der blev gemt succesfuldt
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

    /**
     * ExportGUIFields: Opretter spillebrættet til GUI'en med forskellige felttyper
     *
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer spillebrættet i form af et array af GUI-felter
     */
    public GUI_Field[] exportGUIFields() {
        List<Space> board = this.getBoard();
        GUI_Field[] guiBoard = new GUI_Field[board.size()];
        for (int i = 0; i < board.size(); i++) {
            Space space = board.get(i);
            if (space instanceof PropertySpace) {
                PropertySpace pS = (PropertySpace) space;
                guiBoard[i] = new GUI_Street(pS.getName(),
                        getLanguageData().getString(JSONKey.RENT.getKey()) + " " + String.valueOf(pS.getRent(this)),
                        pS.getName(), "", pS.getColor(), Color.WHITE);
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

    /**
     * AddPlayer: Tilføjer en spiller til spillet
     * 
     * @param player Spiller, som skal tilføjes
     * 
     * @author Joakim Bøegh Levorsen, s185023
     */
    public void addPlayer(Player player) {
        if (this.players == null)
            this.getPlayers();
        this.players.add(player);
        this.add(player);
    }

    /**
     * GetPlayers: Henter spillere fra databasen
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer en liste af de spillere, der er med i spillet
     */
    public List<Player> getPlayers() {
        if (this.players == null) {
            this.players = this.getAll(Player.class).load();
        }
        // Sender kopi af listen så man ikke kan ændre den
        return new ArrayList<Player>(this.players);
    }

    /**
     * GetPlayerForID: Henter en spiller fra databasen ud fra et ID
     * 
     * @param id ID for den spiller, man vil hente
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer en spiller
     */
    public Player getPlayerForID(long id) {
        List<Player> players = this.getPlayers();
        for (Player p : players) {
            if (p.getLongId().longValue() == id) {
                return p;
            }
        }
        return null;
    }

    /**
     * GetSpacesForType: Finder alle felter af en bestemt type på brættet
     * 
     * @param type Typen af felt på klasse-form (eksempelvis CardSpace.class)
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer en liste af felterne af den angivne type
     */
    public <S extends Space> List<S> getSpacesForType(Class<S> type) {
        List<S> matches = new ArrayList<S>();
        for (Space space : getBoard()) {
            if (type.isInstance(space)) {
                matches.add((S) space);
            }
        }
        return matches;
    }

    /**
     * GetCardStacks: Henter kortbunker fra databasen
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer en liste af kortbunker
     */
    public List<CardStack> getCardStacks() {
        if (this.stacks == null) {
            this.stacks = this.getAll(CardStack.class).load();
        }
        return this.stacks;
    }

    /**
     * GetBoard: Henter spillebrættet fra databasen
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer en liste af felter
     */
    public List<Space> getBoard() {
        if (this.board == null) {
            this.board = DatabaseSpaceFactory.getSpacesFor(this);
        }
        return this.board;
    }

    /**
     * GetStackForID: Henter en kortbunke ud fra et ID
     * 
     * @param id Kortbunkens ID
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer en kortbunke
     */
    public CardStack getStackForID(long id) {
        List<CardStack> stacks = this.getCardStacks();
        for (CardStack c : stacks) {
            if (c.getLongId().longValue() == id) {
                return c;
            }
        }
        return null;
    }

    /**
     * GetGameName: Henter spillets navn
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer spillets navn i form af en String
     */
    public String getGameName() {
        return (String) this.get(Properties.SAVE_NAME.getProperty());
    }

    /**
     * GetUpdateTime: Henter det tidspunkt, spillet sidst er gemt
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer det tidspunkt, spillet sidst er gemt, som Timestamp
     */
    public Timestamp getUpdateTime() {
        return this.getTimestamp(Properties.UPDATED_AT.getProperty());
    }

    /**
     * GetCurrentTurn: Henter hvilken spiller, der pt. har tur, i form af et index
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer et spillerindex
     */
    public int getCurrentTurn() {
        return (int) this.get(Properties.CURRENT_TURN.getProperty());
    }

    /**
     * SetCurrentTurn: Sætter hvilken spiller, der pt. har tur, i form af et index
     * 
     * @param turn Index på den spiller, der skal have tur
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    public void setCurrentTurn(int turn) {
        this.set(Game.Properties.CURRENT_TURN.getProperty(), turn);
        this.updated();
    }

    /**
     * GetLanguageData: Henter data fra en .json-fil
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer data fra .json-fil som JSONObject
     * 
     * @throws JSONException
     */
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

    /**
     * Variabler og metoder til at implementere Subject
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     *
     */
    final public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Variabler og metoder til at implementere Subject
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     *
     */
    final public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * GetObservers: Henter observers
     * 
     * @author Helle Achari, s180317
     * 
     * @return Returnerer et Set af Observers
     */

    public Set<Observer> getObservers() {
        return observers;
    }
}
