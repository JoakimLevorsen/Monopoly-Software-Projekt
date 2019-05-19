package monopoly.model.spaces;

import designpatterns.*;
import monopoly.controller.GameController;
import monopoly.model.Player;
import org.javalite.activejdbc.Model;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Space: En abstrakt klasse til at repræsentere felterne i spillet
 *
 * @author Joakim Bøegh Levorsen, s185023
 * @author Anders Brandt, s185016
 * @author Helle Achari, s180317
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public abstract class Space extends Model implements Comparable<Space>, Subject {
    public abstract void performAction(GameController controller, Player player);

    @Override
    public abstract boolean equals(Object obj);

    /**
     * GetBoardPosition: Henter feltets placering på spillebrættet
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer feltets placering på brættet som index i et Space-array
     */
    public int getBoardPosition() {
        return this.getInteger(CardSpace.Properties.BOARD_POSITION.getProperty()).intValue();
    }

    /**
     * SetValues: Sætter værdier på et felt
     * 
     * @param space Det felt, for hvilket værdierne skal ændres
     * @param name Feltets navn
     * @param color Feltets farve
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer feltet
     */
    public static Space setValues(Space space, String name, String color) {
        space.set(PropertySpace.Properties.NAME.getProperty(), name);
        space.set(PropertySpace.Properties.COLOR.getProperty(), color);
        return space;
    }

    /**
     * GetName: Henter navnet på feltet.
     * 
     * @author Anders Brandt, s185016
     */
    public String getName() {
        return this.getString(StationSpace.Properties.NAME.getProperty());
    }

    /**
     * GetColour: Henter hexkoden for et felt og ændrer det til RGB.
     *
     * @author Anders Brandt, s185016
     * 
     * @return Returnerer farven som et Color-objekt
     */
    public Color getColor() {
        String hex = this.getString(PropertySpace.Properties.COLOR.getProperty());
        return new Color(Integer.valueOf(hex.substring(0, 2), 16), Integer.valueOf(hex.substring(2, 4), 16),
                Integer.valueOf(hex.substring(4, 6), 16));
    }

    /**
     * CompareTo: Sammenligner et Space med et andet
     * 
     * @param otherSpace Det felt, som der skal sammenlignes med
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer forskellen i felternes placering på spillebrættet
     */
    public int compareTo(Space otherSpace) {
        return this.getBoardPosition() - otherSpace.getBoardPosition();
    }

    /**
     * Variabler og metoder til at implementere Subject
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    private Set<Observer> observers = new HashSet<Observer>();

    final public void addObserver(Observer observer) {
        observers.add(observer);
    }

    final public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * GetObservers: Henter observers
     * 
     * @author Helle Achari, s180317
     * 
     * @return Returnerer et Set af Observers
     */
    public Set<Observer> getObservers() {
        return observers;
    }
}
