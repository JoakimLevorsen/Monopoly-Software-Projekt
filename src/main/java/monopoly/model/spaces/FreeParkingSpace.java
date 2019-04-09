package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.Player;

public class FreeParkingSpace extends Space {

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

    public static FreeParkingSpace create(int position) {
        FreeParkingSpace space = new FreeParkingSpace();
        space.set(FreeParkingSpace.Properties.BOARD_POSITION.getProperty(), position);
        space.set(FreeParkingSpace.Properties.TREASURE.getProperty(), 0);
        return space;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FreeParkingSpace))
            return false;
        FreeParkingSpace other = (FreeParkingSpace) obj;
        return other.getLongId() == this.getLongId() && this.getTreasure() == other.getTreasure()
                && this.getBoardPosition() == other.getBoardPosition();
    }

    public int getBoardPosition() {
        return this.getInteger(FreeParkingSpace.Properties.BOARD_POSITION.getProperty()).intValue();
    }

    public int getTreasure() {
        return this.getInteger(FreeParkingSpace.Properties.TREASURE.getProperty()).intValue();
    }

    public void resetTreasure() {
        this.set(FreeParkingSpace.Properties.TREASURE.getProperty(), 0);
    }

    public void addToTreasure(int amount) {
        int newBalance = this.getTreasure() + amount;
        this.set(FreeParkingSpace.Properties.TREASURE.getProperty(), newBalance);
    }

}