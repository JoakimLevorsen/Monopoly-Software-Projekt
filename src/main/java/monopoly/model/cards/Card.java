package monopoly.model.cards;

import org.javalite.activejdbc.Model;

import monopoly.controller.GameController;
import monopoly.model.Game;
import monopoly.model.Player;

public abstract class Card extends Model implements Comparable<Card> {
    public abstract void execute(GameController gameController, Player player);

    public abstract void setStackPosition(int i);

    public abstract int getStackPosition();

    public int compareTo(Card that) {
        return (this.getStackPosition() - that.getStackPosition());
    }
}