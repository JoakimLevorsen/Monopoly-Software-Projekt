package monopoly.model;

import java.util.List;

import designpatterns.Subject;

/*
Game:
Implementering af Game model objektet, med ORM for databasen.

@author Joakim Levorsen, S185023
*/
public class Game extends Subject {
    private List<Player> players = null;

    public enum Properties {
        CURRENT_TURN("currentTurn"),
        SAVE_NAME("saveName");

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
        g.saveIt();
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
        this.saveIt();
    }

    public List<Player> getPlayers() {
        if (this.players == null) {
            this.players = this.getAll(Player.class);
        }
        return this.players;
    }

    public String getGameName() {
        return (String)this.get(Game.Properties.SAVE_NAME.getProperty());
    }

    public int getCurrentTurn() {
        return (int)this.get(Game.Properties.CURRENT_TURN.getProperty());
    }

    public void setCurrentTurn(int turn) {
        this.set(Game.Properties.CURRENT_TURN.getProperty(), turn);
    }
}