package monopoly.model.cards;

import monopoly.controller.MovementController;
import monopoly.model.Player;

/*
GetLoseMoneyCard:
Implementering af Get/Lose Money Card

@author Cecilie Krog Drejer, s185032
*/

public class GetLoseMoneyCard extends Card {

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

    public static GetLoseMoneyCard create(String text, int amount) {
        GetLoseMoneyCard getLoseMoneyCard = new GetLoseMoneyCard();
        getLoseMoneyCard.set(GetLoseMoneyCard.Properties.TEXT.getProperty(), text);
        getLoseMoneyCard.set(GetLoseMoneyCard.Properties.AMOUNT.getProperty(), amount);
        return getLoseMoneyCard;
    }

    /*
     * GetText: Returnerer teksten der står på kortet.
     *
     * @author Anders Brandt, S185016
     */
    public String getText() {
        return this.getString(Properties.TEXT.getProperty());
    }

    public int getAmount() {
        return this.getInteger(GetLoseMoneyCard.Properties.AMOUNT.getProperty());
    }

    @Override
    public void setStackPosition(int i) {
        this.set(GetLoseMoneyCard.Properties.STACK_POSITION.getProperty(), i);
    }

    @Override
    public int getStackPosition() {
        return this.getInteger(GetLoseMoneyCard.Properties.STACK_POSITION.getProperty());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GetLoseMoneyCard)) return false;
        return ((GetLoseMoneyCard)obj).getLongId().equals(this.getLongId());
    }

    public void execute(MovementController moveController, Player player) {
        player.changeAccountBalance(this.getAmount());

    }
}
