package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.Player;

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
        // TODO: Implement jail routine.
    }

    public int getBoardPosition() {
        return this.getInteger(GoToJailSpace.Properties.BOARD_POSITION.getProperty()).intValue();
    }

}