package monopoly.model;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import designpatterns.Observer;
import org.javalite.activejdbc.Model;

import designpatterns.Subject;

/*
Player:
Implementering af Player model objektet, med ORM for databasen.

@author Joakim Levorsen, S185023
*/
public class Player extends Model implements Subject {
    public enum Properties {
        NAME("name"), PLAYER_INDEX("playerIndex"), BOARD_POSITION("boardPosition"), ACCOUNT_BALANCE("accountBalance");

        private final String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    private Color color;

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
    }

    public int getAccountBalance() {
        return (int) this.get(Player.Properties.ACCOUNT_BALANCE.getProperty());
    }

    public void setAccountBalance(int newBalance) {
        this.set(Player.Properties.ACCOUNT_BALANCE.getProperty(), newBalance);
    }

    public void changeAccountBalance(int by) {
        int newBalance = this.getAccountBalance() + by;
        this.set(Player.Properties.ACCOUNT_BALANCE.getProperty(), newBalance);
    }

    public boolean IsBroke(Player player){
        if (player.getAccountBalance() < 0){
            return true;
        }
        else return false;
    }

    /*
     * getRent: Henter farven for spilleren.
     *
     * @Author Anders Brandt, s185016
     */
    // TODO: Implementer
    public Color getColor() {
        return color;
    }

    /*
     * getRent: sætter farven for spilleren.
     *
     * @Author Anders Brandt, s185016
     */
    // TODO: Implementer
    public void setColor(Color color) {
        this.color = color;
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
    @author Helle Achari, s180317
     */

    public Set<Observer> getObservers() {
        return observers;
    }
}
