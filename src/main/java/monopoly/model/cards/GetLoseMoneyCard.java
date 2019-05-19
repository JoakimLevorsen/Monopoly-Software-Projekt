package monopoly.model.cards;

import monopoly.controller.MovementController;
import monopoly.model.Player;

/**
 * GetLoseMoneyCard: Implementering af korttype til at betale penge til banken/få penge fra banken
 *
 * @author Cecilie Krog Drejer, s185032
 * @author Anders Brandt, s185016
 */
public class GetLoseMoneyCard extends Card {
    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    public enum Properties {
        TEXT("text"), AMOUNT("amount"), STACK_POSITION("stackPosition");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    /**
     * Create: Opretter et GetLoseMoneyCard
     * 
     * @param text Tekst, der står på kortet
     * @param amount Beløb, der skal betales til banken/som banken betale
     * 
     * @author Cecilie Krog Drejer, s185032
     * 
     * @return Returnerer et GetLoseMoneyCard
     */
    public static GetLoseMoneyCard create(String text, int amount) {
        GetLoseMoneyCard getLoseMoneyCard = new GetLoseMoneyCard();
        getLoseMoneyCard.set(GetLoseMoneyCard.Properties.TEXT.getProperty(), text);
        getLoseMoneyCard.set(GetLoseMoneyCard.Properties.AMOUNT.getProperty(), amount);
        return getLoseMoneyCard;
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
     * GetAmount: Henter beløbet, der skal betales/som banken udbetaler
     * 
     * @author Cecilie Krog Drejer, s185032
     * 
     * @return Returnerer beløbet
     */
    public int getAmount() {
        return this.getInteger(GetLoseMoneyCard.Properties.AMOUNT.getProperty());
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
        this.set(GetLoseMoneyCard.Properties.STACK_POSITION.getProperty(), i);
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
        return this.getInteger(GetLoseMoneyCard.Properties.STACK_POSITION.getProperty());
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
        if (!(obj instanceof GetLoseMoneyCard))
            return false;
        return ((GetLoseMoneyCard) obj).getLongId().equals(this.getLongId());
    }

    /**
     * Execute: Ændrer spillerens balance med det pågældende beløb
     * 
     * @param moveController en MovementController
     * @param player Spilleren, som trækker kortet
     * 
     * @author Cecilie Krog Drejer, s185032
     */
    public void execute(MovementController moveController, Player player) {
        player.changeAccountBalance(this.getAmount());
    }
}
