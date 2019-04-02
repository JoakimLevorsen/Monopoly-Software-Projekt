package monopoly.model.spaces;

import org.javalite.activejdbc.Model;

import designpatterns.Subject;
import monopoly.controller.*;
import monopoly.model.*;

public abstract class Space extends Model implements Comparable<Space> {
    public abstract void performAction(GameController controller, Player player);

    public abstract int getBoardPosition();

    public int compareTo(Space otherSpace) {
        return this.getBoardPosition() - otherSpace.getBoardPosition();
    }
}