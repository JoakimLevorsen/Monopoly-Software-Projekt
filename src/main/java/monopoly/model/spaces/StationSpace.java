package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.*;

public class StationSpace extends Space {

    public enum Properties {
        BOARD_POSITION("boardPosition"), NAME("name"), MORGAGED("morgaged"), PRICE("price"), baseRent("baseRent"), OWNER("owner");

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
        this.getId();
        return this.getInteger(StationSpace.Properties.BOARD_POSITION.getProperty()).intValue();
    }

    public Player getOwner(Game game) {
        Player owner = this.parent(Player.class);
        if (owner != null) {
            int id = (int)owner.getId();
            return game.getPlayerForID(id);
        }
        return null;
    }

    //TODO: Tilføj resterende metoder

}