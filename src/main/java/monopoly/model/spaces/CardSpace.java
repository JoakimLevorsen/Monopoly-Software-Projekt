package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.Player;
import monopoly.model.cards.CardStack;

/*
CardSpace:
Implementering af CardSpace

@author Joakim Levorsen, S185023
*/
public class CardSpace extends Space {

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

    public int getBoardPosition() {
        return this.getInteger(CardSpace.Properties.BOARD_POSITION.getProperty()).intValue();
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
        return other.getId().equals(this.getId()) && this.getBoardPosition() == other.getBoardPosition();
    }

    @Override
    public void performAction(GameController controller, Player player) {
        // TODO: Implement take card rutine
    }
}