package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.*;

public class PropertySpace extends Space {

    public enum Properties {
        BOARD_POSITION("boardPosition"), NAME("name"), MORGAGED("morgaged"), PRICE("price"), BASE_RENT("baseRent"),
        OWNER("owner"), BUILD_LEVEL("buildLevel"), COLOUR("colour");

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

    public static PropertySpace create(int position, int baseRent, String name, String colour) {
        // TODO: Correct values
        PropertySpace space = new PropertySpace();
        space.set(PropertySpace.Properties.BASE_RENT.getProperty(), baseRent);
        space.set(PropertySpace.Properties.BOARD_POSITION.getProperty(), position);
        space.set(PropertySpace.Properties.PRICE.getProperty(), baseRent);
        space.set(PropertySpace.Properties.NAME.getProperty(), name);
        space.set(PropertySpace.Properties.MORGAGED.getProperty(), false);
        space.set(PropertySpace.Properties.BUILD_LEVEL.getProperty(), 0);
        space.set(PropertySpace.Properties.COLOUR.getProperty(), colour);
        return space;
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