package monopoly.model.spaces;

import designpatterns.Observer;
import monopoly.controller.GameController;
import monopoly.model.Player;

import java.util.HashSet;
import java.util.Set;

/*
JailSpace:
Et objekt til at repræsentere fængsels feltet.

@author Joakim Levorsen, S185023
*/
public class JailSpace extends Space {

    public enum Properties {
        BOARD_POSITION("boardPosition");

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
        // Da man bare på besøg, sker der ikke noget når man lander på dette felt.
    }

    public static JailSpace create(int position, String name, String color) {
        JailSpace space = new JailSpace();
        space = (JailSpace)(Space.setValues(space, name, color));
        space.set(JailSpace.Properties.BOARD_POSITION.getProperty(), position);
        return space;
    }

    @Override
    public boolean equals(Object obj) {
        // FIX THIS, URGENT
        return true;
    }

    public int getBoardPosition() {
        return this.getInteger(JailSpace.Properties.BOARD_POSITION.getProperty()).intValue();
    }

    public void jail(Player player) {
        this.add(player);
    }

    public void release(Player player) {
        this.remove(player);
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
}
