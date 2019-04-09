package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.*;

public class StationSpace extends Space {

    public enum Properties {
        BOARD_POSITION("boardPosition"), NAME("name"), MORGAGED("morgaged"), PRICE("price"), BASE_RENT("baseRent"),
        OWNER("owner");

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

    public static StationSpace create(int position, int baseRent, String name) {
        // TODO: Correct values
        StationSpace space = new StationSpace();
        space.set(StationSpace.Properties.BASE_RENT.getProperty(), baseRent);
        space.set(StationSpace.Properties.BOARD_POSITION.getProperty(), position);
        space.set(StationSpace.Properties.PRICE.getProperty(), baseRent);
        space.set(StationSpace.Properties.NAME.getProperty(), name);
        space.set(StationSpace.Properties.MORGAGED.getProperty(), false);
        return space;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StationSpace))
            return false;
        StationSpace other = (StationSpace) obj;
        return other.getLongId() == this.getLongId() && this.getBoardPosition() == other.getBoardPosition();
    }

    public int getBoardPosition() {
        this.getId();
        return this.getInteger(StationSpace.Properties.BOARD_POSITION.getProperty()).intValue();
    }

    public Player getOwner(Game game) {
        Player owner = this.parent(Player.class);
        if (owner != null) {
            int id = (int) owner.getId();
            return game.getPlayerForID(id);
        }
        return null;
    }

    // TODO: Tilføj resterende metoder

}