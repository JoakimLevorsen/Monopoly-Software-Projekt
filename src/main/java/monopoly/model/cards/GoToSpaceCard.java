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
        TEXT("text"), SPACE("space");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    public static GoToSpaceCard create(String text, Space space) {
        GoToSpaceCard goToSpaceCard = new GoToSpaceCard();
        goToSpaceCard.set(GoToSpaceCard.Properties.TEXT.getProperty(), text);
        goToSpaceCard.set(GoToSpaceCard.Properties.SPACE.getProperty(), space);
        return goToSpaceCard;
    }

    public int getSpace() {
        return this.getInteger(GoToSpaceCard.Properties.SPACE.getProperty());
    }

    public void execute(GameController gameController, Player player) {
        int playerPosition = player.getBoardPosition();
        //TODO: move player to new space & make sure §200 is awarded, if player passes Start
    }
}