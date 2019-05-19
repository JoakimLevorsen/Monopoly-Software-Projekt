package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.Player;

/**
 * StartSpace: Et objekt til at repræsentere start feltet
 *
 * @author Joakim Bøegh Levorsen, s185023
 */
public class StartSpace extends Space {
    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    public enum Properties {
        BOARD_POSITION("boardPosition"), PAYMENT("payment");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    /**
     * PerformAction: Udbetaler et beløb til spilleren
     *
     * @param controller en GameController
     * @param player Spilleren, som lander på feltet
     * 
     * @author Joakim Bøegh Levorsen, s185023
     */
    @Override
    public void performAction(GameController controller, Player player) {
        player.changeAccountBalance(this.getPayment());
    }

    /**
     * Create: Opretter et StartSpace
     * 
     * @param position Feltets placering på spillebrættet
     * @param payment Det beløb, som udbetales når en spiller passerer/lander på feltet
     * @param name Feltets navn
     * @param color Feltets farve
     *
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer et StartSpace
     */
    public static StartSpace create(int position, int payment, String name, String color) {
        StartSpace space = new StartSpace();
        space = (StartSpace) (Space.setValues(space, name, color));
        space.set(StartSpace.Properties.BOARD_POSITION.getProperty(), position);
        space.set(StartSpace.Properties.PAYMENT.getProperty(), payment);
        return space;
    }

    /**
     * Equals: Bestemmer om et StartSpace ligner et andet
     * 
     * @param obj Det objekt, som det pågældende StartSpace skal sammenlignes med
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer om de to ojekter er ens eller ej 
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StartSpace))
            return false;
        StartSpace other = (StartSpace) obj;
        return other.getLongId().equals(this.getLongId()) && this.getBoardPosition() == other.getBoardPosition();
    }

    /**
     * GetPayment: Henter det beløb, der skal udbetales når en spiller lander på/passerer feltet
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer beløbet
     */
    public int getPayment() {
        return this.getInteger(StartSpace.Properties.PAYMENT.getProperty()).intValue();
    }
}
