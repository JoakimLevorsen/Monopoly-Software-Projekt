package monopoly.model;

import java.util.List;

import designpatterns.Subject;

public class Player extends Subject {
    public enum Properties {
        NAME("name"),
        PLAYER_INDEX("playerIndex"),
        BOARD_POSITION("boardPosition"),
        ACCOUNT_BALANCE("accountBalance");

        private final String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    public static Player newPlayer(String name, int index, int balance) {
        return new Player().set(Player.Properties.NAME.getProperty(), name).set(Player.Properties.PLAYER_INDEX.getProperty(), index).set(Player.Properties.BOARD_POSITION.getProperty(), 0).set(Player.Properties.ACCOUNT_BALANCE.getProperty(), balance);
    }

    public static List<Player> playerList() {
        return Player.findAll().load();
    }

    public String getName() {
        return (String)this.get(Player.Properties.NAME.getProperty());
    }

    public int getPlayerIndex() {
        return (int)this.get(Player.Properties.PLAYER_INDEX.getProperty());
    }

    public int getBoardPosition() {
        return (int)this.get(Player.Properties.BOARD_POSITION.getProperty());
    }

    public int getAccountBalance() {
        return (int)this.get(Player.Properties.ACCOUNT_BALANCE.getProperty());
    }

    // TODO: Tilføj klasse til at finde GetOutOfJailCard
}