package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.Player;
import resources.json.JSONKey;

/**
 * TaxSpace: Et objekt til at repræsentere skattefeltet
 * 
 * @author Anders Brandt, s185016
 * @author Joakim Bøegh Levorsen, s185023
 */
public class TaxSpace extends Space {
    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    public enum Properties {
        BOARD_POSITION("boardPosition"), TAX("tax"), TAX_MESSAGE("taxMessage");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    /**
     * PerformAction: Trækker skat fra spillerens konto samt tilføjer beløbet til
     * gevinsten på gratis parkering
     *
     * @param controller en GameController
     * @param player Spilleren, som lander på feltet
     * 
     * @author Anders Brandt, s185016
     */
    @Override
    public void performAction(GameController controller, Player player) {
        player.changeAccountBalance(-getTax());

        for (Space space : controller.getGame().getBoard()) {
            if (space instanceof FreeParkingSpace) {
                ((FreeParkingSpace) space).addToTreasure(getTax());
            }
        }
        String message = controller.getGame().getLanguageData().getString(JSONKey.TAX_MESSAGE.getKey());
        controller.view.getGUI().showMessage(message);
    }

    /**
     * Create: Opretter et TaxSpace
     * 
     * @param position Feltets placering på spillebrættet
     * @param tax Beløbet, som skal betales i skat
     * @param name Feltets navn
     * @param color Feltets farve
     *
     * @author Joakim Bøegh Levorsen, s185023
     * @author Anders Brandt, s185016
     * 
     * @return Returnerer et TaxSpace
     */
    public static TaxSpace create(int position, int tax, String name, String color) {
        TaxSpace space = new TaxSpace();
        space = (TaxSpace) (Space.setValues(space, name, color));
        space.set(Properties.BOARD_POSITION.getProperty(), position);
        space.set(Properties.TAX.getProperty(), tax);
        return space;
    }

    /**
     * Equals: Bestemmer om et TaxSpace ligner et andet
     * 
     * @param obj Det objekt, som det pågældende TaxSpace skal sammenlignes med
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer om de to ojekter er ens eller ej 
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TaxSpace))
            return false;
        TaxSpace other = (TaxSpace) obj;
        return other.getLongId().equals(this.getLongId()) && this.getBoardPosition() == other.getBoardPosition();
    }

    /**
     * GetTax: Henter det beløb, som skal betales på det pågældende TaxSpace
     * 
     * @author Anders Brandt, s185016
     * 
     * @return Returnerer beløbet
     */
    public int getTax() {
        return this.getInteger(Properties.TAX.getProperty()).intValue();
    }
}
