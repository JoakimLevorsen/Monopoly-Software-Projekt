package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.Player;

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

    public int getBoardPosition() {
        return this.getInteger(StartSpace.Properties.BOARD_POSITION.getProperty()).intValue();
    }

    private int getPayment() {
        return this.getInteger(StartSpace.Properties.PAYMENT.getProperty()).intValue();
    }

}