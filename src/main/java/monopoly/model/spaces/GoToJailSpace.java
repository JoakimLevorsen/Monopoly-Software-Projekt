package monopoly.model.spaces;

import designpatterns.Observer;
import monopoly.controller.GameController;
import monopoly.model.Player;

import java.util.HashSet;
import java.util.Set;

/*
GoToJailSpace:
Et objekt til at repræsentere gå i fængsel feltet.

@author Joakim Levorsen, S185023
*/
public class GoToJailSpace extends Space {

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
        JailSpace target = null;
        for (Space space : controller.getGame().getBoard()) {
            if (space instanceof JailSpace) {
                target = (JailSpace) space;
            }
        }
        controller.movementController.goTo(target, true, player);
        target.jail(player);
    }

    public static GoToJailSpace create(int position, String name, String color) {
        GoToJailSpace space = new GoToJailSpace();
        space = (GoToJailSpace)(Space.setValues(space, name, color));
        space.set(GoToJailSpace.Properties.BOARD_POSITION.getProperty(), position);
        return space;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GoToJailSpace))
            return false;
        GoToJailSpace other = (GoToJailSpace) obj;
        return other.getLongId() == this.getLongId() && this.getBoardPosition() == other.getBoardPosition();
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
