package monopoly.model.cards;

import monopoly.controller.MovementController;
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

    public static GoToJailCard create(String text, int space) {
        GoToJailCard goToJailCard = new GoToJailCard();
        goToJailCard.set(GoToJailCard.Properties.TEXT.getProperty(), text);
        goToJailCard.set(GoToJailCard.Properties.SPACE.getProperty(), space);
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



    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GoToJailCard)) return false;
        return ((GoToJailCard)obj).getLongId().equals(this.getLongId());
    }

    /*
     * GetText: Returnerer teksten der står på kortet.
     *
     * @author Anders Brandt, S185016
     */
    public String getText() {
        return this.getString(Properties.TEXT.getProperty());
    }

    /*
     * execute: sørger for at spilleren bliver rykket hen til fængsels feltet samt
     * ryger i fængsel status.
     *
     * @Author Anders Brandt, s185016
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
