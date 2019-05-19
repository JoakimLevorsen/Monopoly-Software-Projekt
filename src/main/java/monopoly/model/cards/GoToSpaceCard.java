package monopoly.model.cards;

import monopoly.controller.MovementController;
import monopoly.model.Player;

/**
 * GoToSpaceCard: Implementering af Go To Space Card
 *
 * @author Cecilie Krog Drejer, s185032
 * @author Joakim Bøegh Levorsen, s185023
 * @author Anders Brandt, s185016
 */
public class GoToSpaceCard extends Card {
    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    public enum Properties {
        TEXT("text"), SPACE("finalBoardPosition"), STACK_POSITION("stackPosition");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    /**
     * Create: Opretter et GoToSpaceCard
     * 
     * @param text Tekst, der står på kortet
     * @param space Index på det felt, der henvises til
     * 
     * @author Cecilie Krog Drejer, s185032
     * 
     * @return Returnerer et GoToSpaceCard
     */
    public static GoToSpaceCard create(String text, int space) {
        GoToSpaceCard goToSpaceCard = new GoToSpaceCard();
        goToSpaceCard.set(GoToSpaceCard.Properties.TEXT.getProperty(), text);
        goToSpaceCard.set(GoToSpaceCard.Properties.SPACE.getProperty(), space);
        return goToSpaceCard;
    }

    /**
     * GetSpace: Henter index for det felt, kortet henviser til
     * 
     * @author Cecilie Krog Drejer, s185032
     * 
     * @return Returnerer index for et felt
     */
    public int getSpace() {
        return this.getInteger(GoToSpaceCard.Properties.SPACE.getProperty());
    }

    /**
     * SetStackPosition: Sætter kortets placering i kortbunken
     * 
     * @param i Index, der angiver kortets ønskede placering
     * 
     * @author Cecilie Krog Drejer, s185032
     */
    @Override
    public void setStackPosition(int i) {
        this.set(GoToSpaceCard.Properties.STACK_POSITION.getProperty(), i);
    }

    /**
     * GetStackPosition: Henter kortets placering i kortbunken
     * 
     * @author Cecilie Krog Drejer, s185032
     * 
     * @return Returnerer index for kortets placering i bunken
     */
    @Override
    public int getStackPosition() {
        return this.getInteger(GoToSpaceCard.Properties.STACK_POSITION.getProperty());
    }

    /**
     * Equals: Bestemmer om et GoToSpaceCard ligner et andet
     * 
     * @param obj Det objekt, som det pågældende GoToSpaceCard skal sammenlignes med
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer om de to ojekter er ens eller ej 
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GoToSpaceCard))
            return false;
        return ((GoToSpaceCard) obj).getLongId().equals(this.getLongId());
    }

    /**
     * GetText: Henter teksten der står på kortet
     *
     * @author Anders Brandt, s185016
     * 
     * @return Returnerer teksten som en String
     */
    public String getText() {
        return this.getString(Properties.TEXT.getProperty());
    }

    /**
     * Execute: Flytter spilleren til det pågældende felt
     * 
     * @param moveController en MovementController
     * @param player Spilleren, som trækker kortet
     * 
     * @author Anders Brandt, s185016
     * @author Cecilie Krog Drejer, s185032
     */
    public void execute(MovementController moveController, Player player) {
        moveController.goTo(moveController.controller.getGame().getBoard().get(getSpace()), false, player);
    }
}
