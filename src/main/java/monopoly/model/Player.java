package monopoly.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import designpatterns.Observer;
import org.javalite.activejdbc.Model;

import designpatterns.Subject;
import monopoly.model.spaces.*;

/*
Player:
Implementering af Player model objektet, med ORM for databasen.

@author Joakim Levorsen, S185023
*/

public class Player extends Model implements Subject {
    private Color color;
    private ArrayList<StationSpace> ownedProperties;

    public enum Properties {
        NAME("name"), PLAYER_INDEX("playerIndex"), BOARD_POSITION("boardPosition"), ACCOUNT_BALANCE("accountBalance"), JAIL_SPACE("jailSpace"), BROKE("broke");

        private final String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    public static Player newPlayer(String name, int index, int balance) {
        return new Player().set(Player.Properties.NAME.getProperty(), name)
                .set(Player.Properties.PLAYER_INDEX.getProperty(), index)
                .set(Player.Properties.BOARD_POSITION.getProperty(), 0)
                .set(Player.Properties.ACCOUNT_BALANCE.getProperty(), balance);
    }

    public static List<Player> playerList() {
        return Player.findAll().load();
    }

    public String getName() {
        return (String) this.get(Player.Properties.NAME.getProperty());
    }

    public int getPlayerIndex() {
        return (int) this.get(Player.Properties.PLAYER_INDEX.getProperty());
    }

    public int getBoardPosition() {
        return (int) this.get(Player.Properties.BOARD_POSITION.getProperty());
    }

    public void setBoardPosition(int position) {
        this.set(Player.Properties.BOARD_POSITION.getProperty(), position);
        this.updated();
    }

    public int getAccountBalance() {
        return (int) this.get(Player.Properties.ACCOUNT_BALANCE.getProperty());
    }

    /*
     * getPrisonStatus: Henter om spilleren er på fængsels feltet eller ej,
     *
     * @Author Anders Brandt, s185016
     */
    public boolean isInJail() {return this.get(Properties.JAIL_SPACE.getProperty()) != null;}

    public void setAccountBalance(int newBalance) {
        this.set(Player.Properties.ACCOUNT_BALANCE.getProperty(), newBalance);
        this.updated();
    }

    public void changeAccountBalance(int by) {
        int newBalance = this.getAccountBalance() + by;
        this.set(Player.Properties.ACCOUNT_BALANCE.getProperty(), newBalance);
        this.updated();
    }

    /*
     * GetOwnedProperties: Henter og returnerer liste af ejendomme ejet af brugeren.
     * 
     * @author Cecilie Krog Drejer, s185032
     */

    public ArrayList<StationSpace> getOwnedProperties(Game game) {
        if (ownedProperties == null) {
            List<Space> allProperties = game.getBoard();
            ownedProperties = new ArrayList<StationSpace>();
            for (Space space : allProperties) {
                if (space instanceof StationSpace) {
                    StationSpace thisSpace = (StationSpace)space;
                    if (thisSpace.getOwner(game) != null && thisSpace.getOwner(game).equals(this)) {
                        ownedProperties.add(thisSpace);
                    }
                }
            }        
        }
        return ownedProperties;
    }

    /*
     * AddToOwnedProperties: Tilføjer ejendom til liste af ejendomme ejet af
     * brugeren.
     * 
     * @author Cecilie Krog Drejer, s185032
     */

    public void addToOwnedProperties(StationSpace property, Game game) {
        this.getOwnedProperties(game).add(property);
        this.add(property);
    }

    /*
     * RemoveFromOwnedProperties: Fjerner ejendom fra liste af ejendomme ejet af brugeren.
     *
     * @author Cecilie Krog Drejer, s185032
     */

    public void removeFromOwnedProperties(StationSpace property, Game game) {
        this.getOwnedProperties(game).remove(property);
        this.remove(property);
    }

    public boolean hasOverdrawnAccount(){
        return this.getAccountBalance() < 0;
    }

    /*
     * getColor: Henter farven for spilleren.
     *
     * @Author Anders Brandt, s185016
     */
    public Color getColor() {
        return color;
    }

    /*
     * setColor: sætter farven for spilleren.
     *
     * @Author Anders Brandt, s185016
     */
    public void setColor(Color color) {
        this.color = color;
        this.updated();
    }

    /*
     * isBroke: Viser om spiller er gået konkurs
     *
     * @author Joakim Levorsen, s185023
     */
    public boolean isBroke() {
        return this.getBoolean(Properties.BROKE.getProperty());
    }

    /*
     * setBrokeStatus: Sætter konkurs status.
     *
     * @author Joakim Levorsen, s185023
     */
    public void setBrokeStatus(boolean to) {
        this.set(Properties.BROKE.getProperty(), to);
        this.updated();
    }

    // TODO: Tilføj klasse til at finde GetOutOfJailCard

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
