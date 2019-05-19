package monopoly.model.cards;

import monopoly.controller.MovementController;
import monopoly.model.Player;
import monopoly.model.spaces.JailSpace;
import monopoly.model.spaces.Space;

/**
 * GoToJailCard: Implementering af Go To Jail Card
 *
 * @author Cecilie Krog Drejer, s185032
 * @author Joakim Bøegh Levorsen, s185023
 * @author Anders Brandt, s185016
 */
public class GoToJailCard extends Card {
    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    public enum Properties {
        TEXT("text"), SPACE("space"), STACK_POSITION("stackPosition");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    /**
     * Create: Opretter et GoToJailCard
     * 
     * @param text Tekst, der står på kortet
     * @param space Index på det felt, der henvises til
     * 
     * @author Cecilie Krog Drejer, s185032
     * 
     * @return Returnerer et GoToJailCard
     */
    public static GoToJailCard create(String text, int space) {
        GoToJailCard goToJailCard = new GoToJailCard();
        goToJailCard.set(GoToJailCard.Properties.TEXT.getProperty(), text);
        goToJailCard.set(GoToJailCard.Properties.SPACE.getProperty(), space);
        return goToJailCard;
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
        this.set(GoToJailCard.Properties.STACK_POSITION.getProperty(), i);
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
        return this.getInteger(GoToJailCard.Properties.STACK_POSITION.getProperty());
    }

    /**
     * Equals: Bestemmer om et GetLoseMoneyCard ligner et andet
     * 
     * @param obj Det objekt, som det pågældende GetLoseMoneyCard skal sammenlignes med
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer om de to ojekter er ens eller ej 
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GoToJailCard))
            return false;
        return ((GoToJailCard) obj).getLongId().equals(this.getLongId());
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
     * Execute: Sørger for at spilleren bliver rykket hen til fængsels feltet samt
     * fængsler spilleren
     * 
     * @param moveController en MovementController
     * @param player Spilleren, som trækker kortet
     *
     * @author Anders Brandt, s185016
     */
    public void execute(MovementController moveController, Player player) {
        JailSpace target = null;
        for (Space space : moveController.controller.getGame().getBoard()) {
            if (space instanceof JailSpace) {
                target = (JailSpace) space;
            }
        }
        moveController.goTo(target, true, player);
        target.jail(player);
    }
}
