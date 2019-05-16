package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.Game;
import monopoly.model.Player;
import monopoly.model.cards.Card;
import monopoly.model.cards.CardStack;
import designpatterns.Observer;

import java.util.HashSet;
import java.util.Set;

/*
CardSpace:
Implementering af CardSpace

@author Joakim Levorsen, S185023
*/
public class CardSpace extends Space {

    public enum Properties {
        BOARD_POSITION("boardPosition"), CARD_STACK_ID("card_stack_id");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    public int getBoardPosition() {
        return this.getInteger(CardSpace.Properties.BOARD_POSITION.getProperty()).intValue();
    }

    public long getCardStackId() {
        return this.getLong(Properties.CARD_STACK_ID.getProperty()).longValue();
    }

    public CardStack getStack(Game game) {
        return game.getStackForID(this.getCardStackId());
    }

    public static CardSpace create(int position, CardStack stack) {
        CardSpace space = new CardSpace();
        space.set(CardSpace.Properties.BOARD_POSITION.getProperty(), position);
        stack.add(space);
        return space;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CardSpace))
            return false;
        CardSpace other = (CardSpace) obj;
        return this.getBoardPosition() == other.getBoardPosition();
    }

    /*
     * performAction: trækker et chancekort og ekskverer det.
     *
     * @Author Anders Brandt, s185016
     */
    @Override
    public void performAction(GameController controller, Player player) {

        for (CardStack stack : controller.getGame().getCardStacks()) {
            if (stack.getLongId() == this.getCardStackId()) {
                Card drawnCard = stack.drawCard();
                controller.view.getGUI().displayChanceCard(drawnCard.getText());
                drawnCard.execute(controller.movementController, player);
            }

        }
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
