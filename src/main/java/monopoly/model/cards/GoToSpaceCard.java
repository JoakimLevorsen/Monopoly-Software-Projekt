package monopoly.model.cards;

import org.javalite.activejdbc.Model;

import monopoly.controller.GameController;
import monopoly.model.Game;
import monopoly.model.Player;
import monopoly.model.spaces.*;

/*
GoToSpaceCard:
Implementering af Go To Space Card

@author Cecilie Krog Drejer, s185032
*/

public class GoToSpaceCard extends Card {

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

    public static GoToSpaceCard create(String text, int space) {
        GoToSpaceCard goToSpaceCard = new GoToSpaceCard();
        goToSpaceCard.set(GoToSpaceCard.Properties.TEXT.getProperty(), text);
        goToSpaceCard.set(GoToSpaceCard.Properties.SPACE.getProperty(), space);
        return goToSpaceCard;
    }

    public int getSpace() {
        return this.getInteger(GoToSpaceCard.Properties.SPACE.getProperty());
    }

    @Override
    public void setStackPosition(int i) {
        this.set(GoToSpaceCard.Properties.STACK_POSITION.getProperty(), i);
    }

    @Override
    public int getStackPosition() {
        return this.getInteger(GoToSpaceCard.Properties.STACK_POSITION.getProperty());
    }

    public void execute(GameController gameController, Player player) {
        int playerPosition = player.getBoardPosition();
        //TODO: move player to new space & make sure §200 is awarded, if player passes Start
    }
}