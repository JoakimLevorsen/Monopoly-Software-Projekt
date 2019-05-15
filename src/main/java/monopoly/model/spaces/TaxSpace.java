package monopoly.model.spaces;

import designpatterns.Observer;
import monopoly.controller.GameController;
import monopoly.model.Player;

import java.util.HashSet;
import java.util.Set;

public class TaxSpace extends Space {
    public enum Properties {
        BOARD_POSITION("boardPosition"), TAX("tax");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    /*
     * performAction trækker skat fra spillerens konto samt tilføjer skatte beløbet
     * til gevinsten på gratis parkering.
     *
     * @Author Anders Brandt, s185016
     */
    @Override
    public void performAction(GameController controller, Player player) {
        player.changeAccountBalance(-getTax());

        for (Space space : controller.getGame().getBoard()) {
            if (space instanceof FreeParkingSpace) {
                ((FreeParkingSpace) space).addToTreasure(getTax());
            }
        }
    }

    public static TaxSpace create(int position, int tax) {
        TaxSpace space = new TaxSpace();
        space.set(Properties.BOARD_POSITION.getProperty(), position);
        space.set(Properties.TAX.getProperty(), tax);
        return space;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StartSpace))
            return false;
        StartSpace other = (StartSpace) obj;
        return other.getLongId() == this.getLongId() && this.getBoardPosition() == other.getBoardPosition();
    }

    public int getBoardPosition() {
        return this.getInteger(Properties.BOARD_POSITION.getProperty()).intValue();
    }

    public int getTax() {
        return this.getInteger(Properties.TAX.getProperty()).intValue();
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
