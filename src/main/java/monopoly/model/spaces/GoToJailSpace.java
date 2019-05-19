package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.Player;

/**
 * GoToJailSpace: Et objekt til at repræsentere gå i fængsel feltet.
 *
 * @author Joakim Bøegh Levorsen, s185023
 * @author Anders Brandt, s185016
 */
public class GoToJailSpace extends Space {
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
     * PerformAction: Sender en spiller til fængslet og fængsler dem
     * 
     * @param controller en GameController
     * @param player Spilleren, der landet på feltet
     *
     * @author Anders Brandt, s105016
     */
    @Override
    public void performAction(GameController controller, Player player) {
        JailSpace target = null;
        for (Space space : controller.getGame().getBoard()) {
            if (space instanceof JailSpace) {
                target = (JailSpace) space;
            }
        }
        controller.movementController.goTo(target, true, player);
        target.jail(player);
    }

    /**
     * Create: Opretter et GoToJailSpace
     * 
     * @param position Feltets placering på spillebrættet
     * @param name Feltets navn
     * @param color Feltets farve
     *
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer et GoToJailSpace
     */
    public static GoToJailSpace create(int position, String name, String color) {
        GoToJailSpace space = new GoToJailSpace();
        space = (GoToJailSpace) (Space.setValues(space, name, color));
        space.set(GoToJailSpace.Properties.BOARD_POSITION.getProperty(), position);
        return space;
    }

    /**
     * Equals: Bestemmer om et GoToJailSpace ligner et andet
     * 
     * @param obj Det objekt, som det pågældende GoToJailSpace skal sammenlignes med
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer om de to ojekter er ens eller ej 
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GoToJailSpace))
            return false;
        GoToJailSpace other = (GoToJailSpace) obj;
        return other.getLongId().equals(this.getLongId()) && this.getBoardPosition() == other.getBoardPosition();
    }
}
