package monopoly.model.cards;

import org.javalite.activejdbc.Model;

import monopoly.controller.GameController;
import monopoly.model.Game;
import monopoly.model.Player;
import monopoly.model.spaces.*;

/*
GoToJailCard:
Implementering af Go To Jail Card

@author Cecilie Krog Drejer, s185032
*/

public class GoToJailCard extends Card {

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

    public static GoToJailCard create(String text, Space space) {
        GoToJailCard goToJailCard = new GoToJailCard();
        goToJailCard.set(GoToJailCard.Properties.TEXT.getProperty(), text);
        goToJailCard.set(GoToJailCard.Properties.SPACE.getProperty(), space);
        // TODO: ^ Man må ikke kører set med et objekt?
        return goToJailCard;
    }

    @Override
    public void setStackPosition(int i) {
        this.set(GoToJailCard.Properties.STACK_POSITION.getProperty(), i);
    }

    @Override
    public int getStackPosition() {
        return this.getInteger(GoToJailCard.Properties.STACK_POSITION.getProperty());
    }

    public void execute(GameController gameController, Player player) {
        //TODO: move player to jail and jail them (use jail() method from JailSpace)
    }
}