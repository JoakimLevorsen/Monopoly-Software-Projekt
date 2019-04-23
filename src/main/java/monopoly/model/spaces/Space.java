package monopoly.model.spaces;

import org.javalite.activejdbc.Model;

import designpatterns.Subject;
import monopoly.controller.*;
import monopoly.model.*;

/*
Space:
Et objekt til at repræsentere hvad et startfelt skal have af metoder.

@author Joakim Levorsen, S185023
*/
public abstract class Space extends Model implements Comparable<Space>, Subject{
    public abstract void performAction(GameController controller, Player player);

    public abstract int getBoardPosition();

    public int compareTo(Space otherSpace) {
        return this.getBoardPosition() - otherSpace.getBoardPosition();
    }

    @Override
    public abstract boolean equals(Object obj);

}
