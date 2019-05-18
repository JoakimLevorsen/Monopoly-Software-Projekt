package monopoly.model.spaces;

import designpatterns.Observer;
import monopoly.controller.GameController;
import monopoly.model.Player;

import java.util.HashSet;
import java.util.Set;

/**
StartSpace:
Et objekt til at repræsentere start feltet.

@author Joakim Levorsen, S185023
*/
public class StartSpace extends Space {

    public enum Properties {
        BOARD_POSITION("boardPosition"), PAYMENT("payment");

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
        player.changeAccountBalance(this.getPayment());
    }

    public static StartSpace create(int position, int payment, String name, String color) {
        StartSpace space = new StartSpace();
        space = (StartSpace) (Space.setValues(space, name, color));
        space.set(StartSpace.Properties.BOARD_POSITION.getProperty(), position);
        space.set(StartSpace.Properties.PAYMENT.getProperty(), payment);
        return space;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StartSpace))
            return false;
        StartSpace other = (StartSpace) obj;
        return other.getLongId().equals(this.getLongId()) && this.getBoardPosition() == other.getBoardPosition();
    }

    public int getPayment() {
        return this.getInteger(StartSpace.Properties.PAYMENT.getProperty()).intValue();
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
