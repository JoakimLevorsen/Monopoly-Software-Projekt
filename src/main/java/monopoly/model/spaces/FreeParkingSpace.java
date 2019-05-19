package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.Player;

/**
 * FreeParkingSpace: Et objekt til at repræsentere gratis parkering feltet
 *
 * @author Joakim Bøegh Levorsen, s185023
 * @author Anders Brandt, s185016
 */
public class FreeParkingSpace extends Space {
    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
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

    /**
     * PerformAction: Udbetaler gevinsten til spilleren samt nulstiller gevinstbeløbet
     *
     * @param controller en GameController
     * @param player Spilleren, som lander på feltet
     * 
     * @author Anders Brandt, s185016
     */
    @Override
    public void performAction(GameController controller, Player player) {
        player.changeAccountBalance(getTreasure());
        resetTreasure();
    }

    /**
     * Create: Opretter et FreeParkingSpace
     * 
     * @param position Feltets placering på spillebrættet
     * @param name Feltets navn
     * @param color Feltets farve
     *
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer et FreeParkingSpace
     */
    public static FreeParkingSpace create(int position, String name, String color) {
        FreeParkingSpace space = new FreeParkingSpace();
        space = (FreeParkingSpace) (Space.setValues(space, name, color));
        space.set(FreeParkingSpace.Properties.BOARD_POSITION.getProperty(), position);
        space.set(FreeParkingSpace.Properties.TREASURE.getProperty(), 0);
        return space;
    }

    /**
     * Equals: Bestemmer om et FreeParkingSpace ligner et andet
     * 
     * @param obj Det objekt, som det pågældende FreeParkingSpace skal sammenlignes med
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer om de to ojekter er ens eller ej 
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FreeParkingSpace))
            return false;
        FreeParkingSpace other = (FreeParkingSpace) obj;
        return other.getLongId().equals(this.getLongId()) && this.getTreasure() == other.getTreasure()
                && this.getBoardPosition() == other.getBoardPosition();
    }

    /**
     * GetTreasure: Henter pengebeløbet, der ligger på feltet
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer pengebeløbet
     */
    public int getTreasure() {
        return this.getInteger(FreeParkingSpace.Properties.TREASURE.getProperty()).intValue();
    }

    /**
     * ResetTreasure: Sætter pengebeløbet, der ligger på feltet, lig 0
     * 
     * @author Joakim Bøegh Levorsen, s185023
     */
    public void resetTreasure() {
        this.set(FreeParkingSpace.Properties.TREASURE.getProperty(), 0);
    }

    /**
     * AddToTreasure: Tilføjer til pengebeløbet, der ligger på feltet
     * 
     * @param amount Beløb, der skal tilføjes til skatten
     * 
     * @author Joakim Bøegh Levorsen, s185023
     */
    public void addToTreasure(int amount) {
        int newBalance = this.getTreasure() + amount;
        this.set(FreeParkingSpace.Properties.TREASURE.getProperty(), newBalance);
    }
}
