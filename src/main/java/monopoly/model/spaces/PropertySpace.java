package monopoly.model.spaces;

import monopoly.model.Game;
import monopoly.model.Player;

/**
 * PropertySpace: Et objekt til at repræsentere ejendoms felter
 * 
 * @author Joakim Bøegh Levorsen, s185023
 * @author Cecilie Krog Drejer, s185032
 * @author Anders Brandt, s185016
 */
public class PropertySpace extends StationSpace {
    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
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

    /**
     * Create: Opretter et PropertySpace
     * 
     * @param position Feltets placering på spillebrættet
     * @param name Feltets navn
     * @param price Ejendommens pris
     * @param baseRent Ejendommens basisleje
     * @param color Feltets farve
     *
     * @author Joakim Bøegh Levorsen, s185023
     * @author Cecilie Krog Drejer, s185032
     * 
     * @return Returnerer et PropertySpace
     */
    public static PropertySpace create(int position, String name, int price, int baseRent, String color) {
        PropertySpace space = new PropertySpace();
        space = (PropertySpace) (Space.setValues(space, name, color));
        space.set(PropertySpace.Properties.BOARD_POSITION.getProperty(), position);
        space.set(PropertySpace.Properties.MORTGAGED.getProperty(), false);
        space.set(PropertySpace.Properties.PRICE.getProperty(), price);
        space.set(PropertySpace.Properties.BASE_RENT.getProperty(), baseRent);
        space.set(PropertySpace.Properties.BUILD_LEVEL.getProperty(), 0);
        return space;
    }

    /**
     * Equals: Bestemmer om et PropertySpace ligner et andet
     * 
     * @param obj Det objekt, som det pågældende PropertySpace skal sammenlignes med
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer om de to ojekter er ens eller ej 
     */
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

    /**
     * GetRent: Henter lejen for ejendommen
     * 
     * @author Anders Brandt, s185016
     * @author Joakim Levorsen, s185023
     */
    @Override
    public int getRent(Game game) {
        int baseRent = this.getInteger(Properties.BASE_RENT.getProperty());
        baseRent += (this.getHousesBuilt() * baseRent) / 2;
        return baseRent;
    }

    /**
     * GetHousesBuilt: Henter antallet af huse der er bygget på ejendommen
     *
     * @author Anders Brandt, s185016
     * 
     * @return Returnerer antallet af huse bygget på ejendommen
     */
    public int getHousesBuilt() {
        return this.getInteger(Properties.BUILD_LEVEL.getProperty());
    }

    /**
     * SetBuildLevel: Sætter antallet af huse bygget på ejendommen
     * 
     * @param amount Antallet af huse, der skal være på ejendommen
     * 
     * @author Cecilie Krog Drejer, s185032
     */
    public void setBuildLevel(int amount) {
        this.set(PropertySpace.Properties.BUILD_LEVEL.getProperty(), amount);
    }
}
