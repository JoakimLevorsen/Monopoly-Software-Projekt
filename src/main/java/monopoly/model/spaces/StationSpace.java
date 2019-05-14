package monopoly.model.spaces;

import designpatterns.Observer;
import monopoly.controller.GameController;
import monopoly.model.*;

import java.util.HashSet;
import java.util.Set;

/*
StationSpace:
Et objekt til at repræsentere stationer.

@author Joakim Levorsen, S185023
*/
public class StationSpace extends Space {

    public enum Properties {
        BOARD_POSITION("boardPosition"), NAME("name"), MORTGAGED("mortgaged"), PRICE("price"), BASE_RENT("baseRent"),
        OWNER("owner");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    @Override
    public void performAction(GameController controller, Player player) {
        // TODO: Maybe implement behavior here depending on rules.
    }

    public static StationSpace create(int position, String name, int price, int baseRent) {
        StationSpace space = new StationSpace();
        space.set(StationSpace.Properties.BOARD_POSITION.getProperty(), position);
        space.set(StationSpace.Properties.NAME.getProperty(), name);
        space.set(StationSpace.Properties.MORTGAGED.getProperty(), false);
        space.set(StationSpace.Properties.PRICE.getProperty(), price);
        space.set(StationSpace.Properties.BASE_RENT.getProperty(), baseRent);
        return space;
    }

    //TODO: look equals through
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StationSpace))
            return false;
        StationSpace other = (StationSpace) obj;
        return this.getBoardPosition() == other.getBoardPosition();
    }

    public int getBoardPosition() {
        this.getId();
        return this.getInteger(StationSpace.Properties.BOARD_POSITION.getProperty()).intValue();
    }

    /*
    SetOwner:
    Sætter en spiller som ejer af stationen.

    @author Cecilie Krog Drejer, s185032
    */

    public void setOwner(Player player) {
        player.add(this);
    }

    /*
    RemoveOwner:
    Fjerner den nuværende ejer af stationen.

    @author Cecilie Krog Drejer, s185032
    */

    public void removeOwner(Game game) {
        Player owner = this.getOwner(game);
        if (owner == null) {
            return;
        }
        owner.remove(this);
    }

    public Player getOwner(Game game) {
        Player owner = this.parent(Player.class);
        if (owner != null) {
            int id = (int) owner.getId();
            return game.getPlayerForID(id);
        }
        return null;
    }

    /*
     * getName: Henter navnet på stationen.
     * 
     * @Author Anders Brandt, s185016
     */
    public String getName() {
        return this.getString(Properties.NAME.getProperty());
    }

    /*
     * getName: Henter lejen for stationen.
     * 
     * @Author Anders Brandt, s185016
     */
    // TODO: Tiføj så den udregner hvad lejen skal være, ud fra hvor mange stationer
    // spilleren ejer.
    public String getRent() {
        return this.getString(Properties.BASE_RENT.getProperty());
    }

    // TODO: Tilføj resterende metoder
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

    /*
    * @author Helle Achari, s180317
    */

    public int getPrice() {
        return this.getInteger(Properties.PRICE.getProperty());
    }
}
