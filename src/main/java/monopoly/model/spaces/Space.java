package monopoly.model.spaces;

import designpatterns.Subject;
import monopoly.controller.*;
import monopoly.model.*;

public abstract class Space extends Subject {
    public abstract void performAction(GameController controller, Player player);

    public abstract int getBoardPosition();
}