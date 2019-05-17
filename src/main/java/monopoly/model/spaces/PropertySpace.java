package monopoly.model.spaces;

import monopoly.model.*;

import java.awt.*;

/*
PropertySpace:
Et objekt til at repræsentere ejendoms felter.

@author Joakim Levorsen, S185023
*/
public class PropertySpace extends StationSpace {

    public enum Properties {
        BOARD_POSITION("boardPosition"), NAME("name"), MORTGAGED("mortgaged"), PRICE("price"), BASE_RENT("baseRent"),
        OWNER("owner"), BUILD_LEVEL("buildLevel"), COLOR("color");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    public static PropertySpace create(int position, String name, int price, int baseRent, String color) {
        PropertySpace space = new PropertySpace();
        space = (PropertySpace)(Space.setValues(space, name, color));
        space.set(PropertySpace.Properties.BOARD_POSITION.getProperty(), position);
        space.set(PropertySpace.Properties.MORTGAGED.getProperty(), false);
        space.set(PropertySpace.Properties.PRICE.getProperty(), price);
        space.set(PropertySpace.Properties.BASE_RENT.getProperty(), baseRent);
        space.set(PropertySpace.Properties.BUILD_LEVEL.getProperty(), 0);
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
            if (!(myOwner == null && otherOwner == null)) {
                // Since only one space has an owner, they can´t be the same.
                return false;
            }
        } else {
            // This means both have an owner
            if (!myOwner.getLongId().equals(otherOwner.getLongId())) {
                // Not the same owner, not the same field.
                return false;
            }
        }
        return true;
    }

    /*
     * getName: Henter lejen for ejendommen.
     * 
     * @Author Anders Brandt, s185016
     * 
     * @Author Joakim Levorsen, s185023
     */
    public int getRent(Game game) {
        int baseRent = this.getInteger(Properties.BASE_RENT.getProperty());
        baseRent += (this.getHousesBuilt() * baseRent) / 2;
        return baseRent;
    }

    /*
     * getHousesBuilt: Henter hvor mange huse der er bygget på ejendommen.
     *
     * @Author Anders Brandt, s185016
     */
    public int getHousesBuilt() {
        return this.getInteger(Properties.BUILD_LEVEL.getProperty());
    }

    /*
     * SetBuildLevel: Sætter antallet af huse bygget på ejendommen.
     * 
     * @author Cecilie Krog Drejer, s185032
     */

    public void setBuildLevel(int amount) {
        this.set(PropertySpace.Properties.BUILD_LEVEL.getProperty(), amount);
    }

    /**
     * Variabler og metoder til at implementere Subject
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     *
     */
}
