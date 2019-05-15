package monopoly.model.cards;

import monopoly.controller.MovementController;
import monopoly.model.Game;
import monopoly.model.Player;

/*
GetOutOfJailCard:
Implementering af Get Out of Jail Free Card

@author Cecilie Krog Drejer, s185032
*/

public class GetOutOfJailCard extends Card {

    public enum Properties {
        TEXT("text"), OWNER("owner"), STACK_POSITION("stackPosition");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }

    }

    public static GetOutOfJailCard create(String text) {
        GetOutOfJailCard getOutOfJailCard = new GetOutOfJailCard();
        getOutOfJailCard.set(GetOutOfJailCard.Properties.TEXT.getProperty(), text);
        return getOutOfJailCard;
    }

    /*
     * Get Owner: Returnerer ejeren af det pågældende Get Out of Jail Free Card.
     *
     * @author Joakim Levorsen, S185023
     */
    public Player getOwner(Game game) {
        Player owner = this.parent(Player.class);
        if (owner != null) {
            int id = (int) owner.getId();
            return game.getPlayerForID(id);
        }
        return null;
    }

    /*
     * GetText: Returnerer teksten der står på kortet.
     *
     * @author Anders Brandt, S185016
     */
    public String getText() {
        return this.getString(Properties.TEXT.getProperty());
    }

    public void setOwner(Player player) {
        this.add(player);
    }

    @Override
    public void setStackPosition(int i) {
        this.set(GetOutOfJailCard.Properties.STACK_POSITION.getProperty(), i);
    }

    @Override
    public int getStackPosition() {
        return this.getInteger(GetOutOfJailCard.Properties.STACK_POSITION.getProperty());
    }

    public void execute(MovementController moveController, Player player) {
        setOwner(player);

    }
}
