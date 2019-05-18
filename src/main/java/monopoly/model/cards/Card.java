package monopoly.model.cards;

import monopoly.controller.MovementController;
import monopoly.model.Player;
import org.javalite.activejdbc.Model;

/**
Card:
Implementering af abstract kort-klasse.

@author Joakim Levorsen, s185023
@author Cecilie Krog Drejer, s185032
*/

public abstract class Card extends Model implements Comparable<Card> {
    public abstract void execute(MovementController moveController, Player player);

    public abstract void setStackPosition(int i);

    public abstract int getStackPosition();

    public int compareTo(Card that) {
        return (this.getStackPosition() - that.getStackPosition());
    }

    public abstract String getText();

}
