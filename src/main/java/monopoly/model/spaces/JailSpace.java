package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.Player;

/**
 * JailSpace: Et objekt til at repræsentere fængsels feltet.
 *
 * @author Joakim Bøegh Levorsen, s185023
 */
public class JailSpace extends Space {
    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
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

    /**
     * PerformAction: Gør intet, da man bare er på besøg hvis man lander på JailSpace
     * 
     * @param controller en GameController
     * @param player Spilleren, der landet på feltet
     */
    @Override
    public void performAction(GameController controller, Player player) {
        // Da man bare på besøg, sker der ikke noget når man lander på dette felt.
    }

    /**
     * Create: Opretter et JailSpace
     * 
     * @param position Feltets placering på spillebrættet
     * @param name Feltets navn
     * @param color Feltets farve
     *
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer et JailSpace
     */
    public static JailSpace create(int position, String name, String color) {
        JailSpace space = new JailSpace();
        space = (JailSpace) (Space.setValues(space, name, color));
        space.set(JailSpace.Properties.BOARD_POSITION.getProperty(), position);
        return space;
    }

    /**
     * Equals: Bestemmer om et JailSpace ligner et andet
     * 
     * @param obj Det objekt, som det pågældende JailSpace skal sammenlignes med
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer om de to ojekter er ens eller ej 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JailSpace) {
            JailSpace other = (JailSpace) obj;
            return other.getLongId().equals(this.getLongId());
        }
        return false;
    }

    /**
     * Jail: Fængsler en spiller
     * 
     * @param player Spilleren, der skal fængsles
     * 
     * @author Joakim Bøegh Levorsen, s185023
     */
    public void jail(Player player) {
        this.add(player);
    }

    /**
     * Release: Løslader en spiller
     * 
     * @param player Spilleren, der skal løslades
     * 
     * @author Joakim Bøegh Levorsen, s185023
     */
    public void release(Player player) {
        player.getOutOfJail();
    }
}
