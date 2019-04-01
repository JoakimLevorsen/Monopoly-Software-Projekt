package monopoly.model.cards;

import org.javalite.activejdbc.Model;

import monopoly.model.Game;
import monopoly.model.Player;

public abstract class Card extends Model {
    public abstract void execute(Game game, Player player);
}