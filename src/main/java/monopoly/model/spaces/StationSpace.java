package monopoly.model.spaces;

import designpatterns.Observer;
import monopoly.controller.GameController;
import monopoly.model.Game;
import monopoly.model.Player;

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
        OWNER("player_id");

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
        Player owner = this.getOwner(controller.getGame());
        if (owner != null && !isMortgaged()) {
            if (!owner.equals(player)) {
                controller.cashController.payment(player, this.getRent(controller.getGame()), owner);
                controller.view.getGUI().showMessage(
                        player.getName() + " betaler " + getRent(controller.getGame()) + " til " + owner.getName());
            }
        } else
            controller.propertyController.offerProperty(this, player);
    }

    public static StationSpace create(int position, String name, int price, int baseRent, String color) {
        StationSpace space = new StationSpace();
        space = (StationSpace) (Space.setValues(space, name, color));
        space.set(StationSpace.Properties.BOARD_POSITION.getProperty(), position);
        space.set(StationSpace.Properties.NAME.getProperty(), name);
        space.set(StationSpace.Properties.MORTGAGED.getProperty(), false);
        space.set(StationSpace.Properties.PRICE.getProperty(), price);
        space.set(StationSpace.Properties.BASE_RENT.getProperty(), baseRent);
        return space;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StationSpace))
            return false;
        StationSpace other = (StationSpace) obj;
        return this.getBoardPosition() == other.getBoardPosition();
    }

    /*
     * SetOwner: Sætter en spiller som ejer af stationen.
     * 
     * @author Cecilie Krog Drejer, s185032
     */

    public void setOwner(Player player) {
        player.add(this);
    }

    /*
     * RemoveOwner: Fjerner den nuværende ejer af stationen.
     * 
     * @author Cecilie Krog Drejer, s185032
     */

    public void removeOwner(Game game) {
        this.set(Properties.OWNER.getProperty(), null);
    }

    public Player getOwner(Game game) {
        Player owner = this.parent(Player.class);
        if (owner != null) {
            long id = owner.getLongId();
            return game.getPlayerForID(id);
        }
        return null;
    }

    /*
     * getName: Henter lejen for stationen.
     * 
     * @Author Anders Brandt, s185016
     */
    public int getRent(Game game) {
        int baseRent = this.getInteger(Properties.BASE_RENT.getProperty());
        int amountOwned = 0;
        Player owner = this.getOwner(game);
        if (owner == null) {
            return 0;
        }
        for (Space space : game.getBoard()) {
            if (space instanceof StationSpace && !(space instanceof PropertySpace)) {
                Player otherOwner = ((StationSpace) space).getOwner(game);
                if (otherOwner != null && otherOwner.equals(owner))
                    amountOwned++;
            }
        }
        return baseRent * amountOwned;
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

    /*
     * @author Helle Achari, s180317
     */

    public int getPrice() {
        return this.getInteger(Properties.PRICE.getProperty());
    }

    public boolean isMortgaged() {
        return this.getBoolean(Properties.MORTGAGED.getProperty());
    }

    public void setMortgaged(boolean mortgaged) {
        this.set(Properties.MORTGAGED.getProperty(), mortgaged);
    }
}
