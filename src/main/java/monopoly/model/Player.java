package monopoly.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;

import designpatterns.Subject;
import monopoly.model.spaces.PropertySpace;
import monopoly.model.spaces.StationSpace;

/*
Player:
Implementering af Player model objektet, med ORM for databasen.

@author Joakim Levorsen, S185023
*/
public class Player extends Model {
    private Color color;
    private ArrayList<PropertySpace> ownedProperties;
    private ArrayList<StationSpace> ownedStations;
    
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

    public ArrayList<PropertySpace> getOwnedProperties() {
        return ownedProperties;
    }

    public void addToOwnedProperties(PropertySpace property) {
        ownedProperties.add(property);
    }

    public ArrayList<StationSpace> getOwnedStations() {
        return ownedStations;
    }

    public void addToOwnedStations(StationSpace station) {
        ownedStations.add(station);
    }

    public boolean isBroke(Player player){
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
}
