package monopoly.model.spaces;

import designpatterns.Observer;
import monopoly.controller.GameController;
import monopoly.model.Player;

import java.util.HashSet;
import java.util.Set;

/**
FreeParkingSpace:
Et objekt til at repræsentere gratis parkering feltet.

@author Joakim Levorsen, S185023
*/
public class FreeParkingSpace extends Space {

    public enum Properties {
        BOARD_POSITION("boardPosition"), TREASURE("treasure");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    /**
     * performAction: Udbetaler gevinsten til spilleren samt nulstiller gevinst
     * beløbet.
     *
     * @author Anders Brandt, s185016
     */
    @Override
    public void performAction(GameController controller, Player player) {
        player.changeAccountBalance(getTreasure());
        resetTreasure();
    }

    public static FreeParkingSpace create(int position, String name, String color) {
        FreeParkingSpace space = new FreeParkingSpace();
        space = (FreeParkingSpace) (Space.setValues(space, name, color));
        space.set(FreeParkingSpace.Properties.BOARD_POSITION.getProperty(), position);
        space.set(FreeParkingSpace.Properties.TREASURE.getProperty(), 0);
        return space;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FreeParkingSpace))
            return false;
        FreeParkingSpace other = (FreeParkingSpace) obj;
        return other.getLongId().equals(this.getLongId()) && this.getTreasure() == other.getTreasure()
                && this.getBoardPosition() == other.getBoardPosition();
    }

    public int getTreasure() {
        return this.getInteger(FreeParkingSpace.Properties.TREASURE.getProperty()).intValue();
    }

    public void resetTreasure() {
        this.set(FreeParkingSpace.Properties.TREASURE.getProperty(), 0);
    }

    public void addToTreasure(int amount) {
        int newBalance = this.getTreasure() + amount;
        this.set(FreeParkingSpace.Properties.TREASURE.getProperty(), newBalance);
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

    /**
     * @author Helle Achari, s180317
     */

    public Set<Observer> getObservers() {
        return observers;
    }

}
