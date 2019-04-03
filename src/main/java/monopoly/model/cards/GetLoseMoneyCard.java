package monopoly.model.cards;

import org.javalite.activejdbc.Model;

import monopoly.controller.GameController;
import monopoly.model.Game;
import monopoly.model.Player;
import monopoly.model.spaces.*;

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

    public void execute(GameController gameController, Player player) {
        if (this.getAmount() > 0) {
            //TODO: increase player balance with absolute value of amount
        } else if (this.getAmount() < 0) {
            //TODO: deduct player balance with absolute value of amount
        }
    }
}