package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.Player;

public class JailSpace extends Space {

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

    @Override
    public void performAction(GameController controller, Player player) {
        // TODO: Maybe implement behavior here depending on rules.
    }

    public int getBoardPosition() {
        return this.getInteger(FreeParkingSpace.Properties.BOARD_POSITION.getProperty()).intValue();
    }

    public void jail(Player player) {
        this.add(player);
    }

    public void release(Player player) {
        this.remove(player);
    }

}