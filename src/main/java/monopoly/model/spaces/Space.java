package monopoly.model.spaces;

import designpatterns.Subject;
import monopoly.controller.GameController;
import monopoly.model.Player;
import org.javalite.activejdbc.Model;

import java.awt.*;

/*
Space:
Et objekt til at repræsentere hvad et startfelt skal have af metoder.

@author Joakim Levorsen, S185023
*/
public abstract class Space extends Model implements Comparable<Space>, Subject {
    public abstract void performAction(GameController controller, Player player);

    public int getBoardPosition() {
        return this.getInteger(CardSpace.Properties.BOARD_POSITION.getProperty()).intValue();
    }

    public static Space setValues(Space space, String name, String color) {
        space.set(PropertySpace.Properties.NAME.getProperty(), name);
        space.set(PropertySpace.Properties.COLOR.getProperty(), color);
        return space;
    }

    /*
     * getName: Henter navnet på stationen.
     * 
     * @Author Anders Brandt, s185016
     */
    public String getName() {
        return this.getString(StationSpace.Properties.NAME.getProperty());
    }

    /*
     * getColour: Henter hexkoden for ejendommen og ændrer det til rgb.
     *
     * @Author Anders Brandt, s185016
     */
    public Color getColor() {
        String hex = this.getString(PropertySpace.Properties.COLOR.getProperty());
        return new Color(Integer.valueOf(hex.substring(0, 2), 16), Integer.valueOf(hex.substring(2, 4), 16),
                Integer.valueOf(hex.substring(4, 6), 16));
    }

    public int compareTo(Space otherSpace) {
        return this.getBoardPosition() - otherSpace.getBoardPosition();
    }

    @Override
    public abstract boolean equals(Object obj);

}
