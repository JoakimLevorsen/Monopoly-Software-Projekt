package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.Player;

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
        // TODO: Implement jail routine.
    }

    public static GoToJailSpace create(int position) {
        GoToJailSpace space = new GoToJailSpace();
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

    public int getBoardPosition() {
        return this.getInteger(GoToJailSpace.Properties.BOARD_POSITION.getProperty()).intValue();
    }

}