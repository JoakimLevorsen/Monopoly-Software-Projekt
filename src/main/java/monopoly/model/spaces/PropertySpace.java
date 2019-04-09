package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.*;

import java.awt.*;

public class PropertySpace extends Space {

    public enum Properties {
        BOARD_POSITION("boardPosition"), NAME("name"), MORTGAGED("mortgaged"), PRICE("price"), BASE_RENT("baseRent"),
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
        space.set(PropertySpace.Properties.MORTGAGED.getProperty(), false);
        space.set(PropertySpace.Properties.BUILD_LEVEL.getProperty(), 0);
        space.set(PropertySpace.Properties.COLOUR.getProperty(), colour);
        return space;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PropertySpace))
            return false;
        PropertySpace other = (PropertySpace) obj;
        Player myOwner = this.parent(Player.class);
        Player otherOwner = other.parent(Player.class);
        if (myOwner == null || otherOwner == null) {
            // This means atleast one of these do not have an owner.
            if (!(myOwner == null && otherOwner == null))  {
                // Since only one space has an owner, they can´t be the same.
                return false;
            }
        } else {
            // This means both have an owner
            if (myOwner.getLongId() != otherOwner.getLongId()) {
                // Not the same owner, not the same field.
                return false;
            }
        }
        // TODO: Mange flere gettere
        return true;
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
    /* getColour:
    Henter hexkoden for ejendommen og ændrer det til rgb.
    @Author Anders Brandt, s185016 */
    public Color getColour() {
        String hex = this.getString(PropertySpace.Properties.COLOUR.getProperty());
        return new Color(
            Integer.valueOf( hex.substring(0 , 2), 16),
            Integer.valueOf( hex.substring(2 , 4), 16),
            Integer.valueOf( hex.substring(4 , 6), 16));
    }
    /* getName:
    Henter navnet på ejendommen.
    @Author Anders Brandt, s185016 */
    public String getName() { return this.getString(PropertySpace.Properties.NAME.getProperty()); }
    /* getHousesBuilt:
    Henter hvor mange huse der er bygget på ejendommen.
    @Author Anders Brandt, s185016 */
    public String getHousesBuilt() { return this.getString(Properties.BUILD_LEVEL.getProperty()); }
    /* getRent:
    Henter lejen for ejendommen.
    @Author Anders Brandt, s185016 */
    //Denne skal ændres så den udregner hvad lejen skal være, ud fra hvor mange huse der er på ejendommen.
    public String getRent() { return this.getString(Properties.BASE_RENT.getProperty());}

    // TODO: Tilføj resterende metoder

}
