package monopoly.model.cards;

import org.javalite.activejdbc.Model;

import monopoly.controller.GameController;
import monopoly.model.Game;
import monopoly.model.Player;
import monopoly.model.spaces.JailSpace;

/*
GetOutOfJailCard:
Implementering af Get Out of Jail Free Card

@author Cecilie Krog Drejer, s185032
*/

public class GetOutOfJailCard extends Card {

    public enum Properties {
        TEXT("text"), OWNER("owner");

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
    Get Owner:
    Returnerer ejeren af det pågældende Get Out of Jail Free Card.
    
    @author Joakim Levorsen, S185023
    */
    public Player getOwner(Game game) {
        Player owner = this.parent(Player.class);
        if (owner != null) {
            int id = (int) owner.getId();
            return game.getPlayerForID(id);
        }
        return null;
    }

    public void setOwner(Player player) {
        this.add(player);
    }

    public void execute(GameController gameController, Player player) {
        setOwner(player);
    }
}