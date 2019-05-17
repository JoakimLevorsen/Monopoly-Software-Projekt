package monopoly.model.cards;

import org.javalite.activejdbc.annotations.BelongsTo;

import monopoly.controller.MovementController;
import monopoly.model.Game;
import monopoly.model.Player;

/*
GetOutOfJailCard:
Implementering af Get Out of Jail Free Card

@author Cecilie Krog Drejer, s185032
*/

@BelongsTo(parent = Player.class, foreignKeyName = "player_id")
public class GetOutOfJailCard extends Card {

    public enum Properties {
        TEXT("text"), OWNER("player_id"), STACK_POSITION("stackPosition");

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
        Object playerID = this.get(Properties.OWNER.getProperty());
        if (playerID != null) {
            if (playerID instanceof Integer) {
                Integer id = (Integer)playerID;
                return game.getPlayerForID(id.longValue());
            } else {
                System.out.println("Woah unexpected type of player id");
            }
            
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
        if (player == null) {
            this.set(Properties.OWNER.getProperty(), null);
        }
        this.set(Properties.OWNER.getProperty(), player.getLongId());
    }

    @Override
    public void setStackPosition(int i) {
        this.set(GetOutOfJailCard.Properties.STACK_POSITION.getProperty(), i);
    }

    @Override
    public int getStackPosition() {
        return this.getInteger(GetOutOfJailCard.Properties.STACK_POSITION.getProperty());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GetOutOfJailCard)) return false;
        return ((GetOutOfJailCard)obj).getLongId().equals(this.getLongId());
    }

    public void execute(MovementController moveController, Player player) {
        setOwner(player);

    }
}
