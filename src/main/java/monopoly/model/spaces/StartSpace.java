package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.Player;

/*
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

    public static StartSpace create(int position, int payment) {
        StartSpace space = new StartSpace();
        space.set(StartSpace.Properties.BOARD_POSITION.getProperty(), position);
        space.set(StartSpace.Properties.PAYMENT.getProperty(), payment);
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
        return this.getInteger(StartSpace.Properties.BOARD_POSITION.getProperty()).intValue();
    }

    private int getPayment() {
        return this.getInteger(StartSpace.Properties.PAYMENT.getProperty()).intValue();
    }

}