package monopoly.model;

import designpatterns.Observer;
import designpatterns.Subject;
import monopoly.model.cards.CardStack;
import monopoly.model.cards.GetOutOfJailCard;
import monopoly.model.spaces.PropertySpace;
import monopoly.model.spaces.Space;
import monopoly.model.spaces.StationSpace;
import org.javalite.activejdbc.Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Player: Implementering af Player model objektet, med ORM for databasen.
 *
 * @author Joakim Bøegh Levorsen, s185023
 * @author Cecilie Krog Drejer, s185032
 * @author Anders Brandt, s185016
 * @author Helle Achari, s180317
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Player extends Model implements Subject {
    private ArrayList<StationSpace> ownedProperties;

    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    public enum Properties {
        NAME("name"), PLAYER_INDEX("playerIndex"), BOARD_POSITION("boardPosition"), ACCOUNT_BALANCE("accountBalance"),
        JAIL_SPACE("jail_space_id"), BROKE("broke"), COLOR("color");

        private final String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    /**
     * NewPlayer: Opretter en ny spiller
     * 
     * @param name Spillerens navn
     * @param index Spillerens index
     * @param balance Spillerens startbalance
     * @param color Spillerens farve
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * @author Cecilie Krog Drejer, s185032
     * 
     * @return Returnerer en ny spiller
     */
    public static Player newPlayer(String name, int index, int balance, String color) {
        return new Player().set(Player.Properties.NAME.getProperty(), name)
                .set(Player.Properties.PLAYER_INDEX.getProperty(), index)
                .set(Player.Properties.BOARD_POSITION.getProperty(), 0)
                .set(Player.Properties.ACCOUNT_BALANCE.getProperty(), balance)
                .set(Player.Properties.COLOR.getProperty(), color);
    }

    /**
     * GetName: Henter spillerens navn
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer spillerens navn som en String
     */
    public String getName() {
        return (String) this.get(Player.Properties.NAME.getProperty());
    }

    /**
     * GetPlayerIndex: Henter spillerens index
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer spillerens index som en integer
     */
    public int getPlayerIndex() {
        return (int) this.get(Player.Properties.PLAYER_INDEX.getProperty());
    }

    /**
     * GetBoardPosition: Henter spillerens placering på spillebrættet
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer spillerens placering som en integer
     */
    public int getBoardPosition() {
        return (int) this.get(Player.Properties.BOARD_POSITION.getProperty());
    }

    /**
     * SetBoardPosition: Sætter spillerens placering på spillebrættet
     * 
     * @param position index for det felt, spilleren skal stå på
     * 
     * @author Joakim Bøegh Levorsen, s185023
     */
    public void setBoardPosition(int position) {
        this.set(Player.Properties.BOARD_POSITION.getProperty(), position);
        this.updated();
    }

    /**
     * GetAccountBalance: Henter spillerens kontobalance
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer spillerens balance som en integer
     */
    public int getAccountBalance() {
        return (int) this.get(Player.Properties.ACCOUNT_BALANCE.getProperty());
    }

    /**
     * getPrisonStatus: Henter om spilleren er fængslet eller ej
     *
     * @author Anders Brandt, s185016
     * 
     * @return Returnerer om spilleren er fængslet eller ej som boolean
     */
    public boolean isInJail() {
        return this.get(Properties.JAIL_SPACE.getProperty()) != null;
    }

    /**
     * SetAccountBalance: Sætter spillerens kontobalance til en bestemt værdi
     * 
     * @param newBalance Den nye balances værdi
     * 
     * @author Joakim Bøegh Levorsen, s185023
     */
    public void setAccountBalance(int newBalance) {
        this.set(Player.Properties.ACCOUNT_BALANCE.getProperty(), newBalance);
        this.updated();
    }

    /**
     * ChangeAccountBalance: Ændrer en spillers kontobalance med et beløb
     * 
     * @param by Beløbet, balancen skal ændres med - kan også være negativ
     * 
     * @author Joakim Bøegh Levorsen, s185023
     */
    public void changeAccountBalance(int by) {
        int newBalance = this.getAccountBalance() + by;
        this.set(Player.Properties.ACCOUNT_BALANCE.getProperty(), newBalance);
        this.updated();
    }

    /**
     * GetOwnedProperties: Henter og returnerer liste af ejendomme ejet af spilleren
     * 
     * @param game Det pågældende spil, som spilleren tilhører
     * 
     * @author Cecilie Krog Drejer, s185032
     * 
     * @return Returnerer en liste af de ejendomme, som spilleren ejer
     */

    public ArrayList<StationSpace> getOwnedProperties(Game game) {
        if (ownedProperties == null) {
            List<Space> allProperties = game.getBoard();
            ownedProperties = new ArrayList<StationSpace>();
            for (Space space : allProperties) {
                if (space instanceof StationSpace) {
                    StationSpace thisSpace = (StationSpace) space;
                    if (thisSpace.getOwner(game) != null && thisSpace.getOwner(game).equals(this)) {
                        ownedProperties.add(thisSpace);
                    }
                }
            }
        }
        return ownedProperties;
    }

    /**
     * AddToOwnedProperties: Tilføjer ejendom til liste af ejendomme ejet af
     * spilleren
     * 
     * @param property Ejendom, der skal tilføjes
     * @param game Det pågældende spil, som spilleren tilhører
     * 
     * @author Cecilie Krog Drejer, s185032
     * @author Joakim Bøegh Levorsen, s185023
     */

    public void addToOwnedProperties(StationSpace property, Game game) {
        this.getOwnedProperties(game).add(property);
        this.updated();
    }

    /**
     * RemoveFromOwnedProperties: Fjerner ejendom fra liste af ejendomme ejet af
     * spilleren
     *
     * @param property Ejendom, der skal fjernes
     * @param game Det pågældende spil, som spilleren tilhører
     * 
     * @author Cecilie Krog Drejer, s185032
     * @author Joakim Bøegh Levorsen, s185023
     */

    public void removeFromOwnedProperties(StationSpace property, Game game) {
        this.getOwnedProperties(game).remove(property);
        this.updated();
    }

    /**
     * HasOverdrawnAccount: Tjekker om spillerens kontobalance er negativ
     * 
     * @author Cecilie Krog Drejer, s185032
     * 
     * @return Returnerer om spillerens balance er negativ eller ej
     */
    public boolean hasOverdrawnAccount() {
        return this.getAccountBalance() < 0;
    }

    /**
     * GetColour: Henter hexkoden for ejendommen og ændrer det til RGB
     *
     * @author Anders Brandt, s185016
     * 
     * @return Returnerer et java Color-objekt
     */
    public Color getColor() {
        String hex = this.getString(PropertySpace.Properties.COLOR.getProperty());
        return new Color(Integer.valueOf(hex.substring(0, 2), 16), Integer.valueOf(hex.substring(2, 4), 16),
                Integer.valueOf(hex.substring(4, 6), 16));
    }

    /**
     * SetColor: sætter farven for spilleren
     * 
     * @param color Spillerens farve skrevet som hexkode i en String (eksempelvis, blå = 0000FF)
     *
     * @author Anders Brandt, s185016
     */
    public void setColor(String color) {
        this.set(Player.Properties.COLOR.getProperty(), color);
        this.updated();
    }

    /**
     * IsBroke: Viser om spiller er gået konkurs, og dermed ude af spillet
     *
     * @author Joakim Levorsen, s185023
     * 
     * @return Returnerer om spilleren er gået konkurs eller ej
     */
    public boolean isBroke() {
        return this.getBoolean(Properties.BROKE.getProperty());
    }

    /**
     * SetBrokeStatus: Sætter konkurs status
     * 
     * @param to Boolean, som konkursstatus skal sættes til
     *
     * @author Joakim Levorsen, s185023
     */
    public void setBrokeStatus(boolean to) {
        this.set(Properties.BROKE.getProperty(), to);
        this.updated();
    }

    /**
     * GetOutOfJailCard: Finder spillerens GetOutOfJailCard, hvis de har et
     *
     * @param game Det spil, som spilleren tilhører
     * 
     * @author Joakim Levorsen, s185023
     * 
     * @return Returnerer et GetOutOfJailCard, hvis spilleren har et, ellers null
     */
    public GetOutOfJailCard getGetOutOfJailCard(Game game) {
        for (CardStack stack : game.getCardStacks()) {
            for (GetOutOfJailCard getOutCard : stack.getCardForType(GetOutOfJailCard.class)) {
                Player jailCardOwner = getOutCard.getOwner(game);
                if (jailCardOwner != null && jailCardOwner.equals(this))
                    return getOutCard;
            }
        }
        return null;
    }

    /**
     * GetOutOfJail: Løslader spilleren fra fængsel
     *
     * @author Joakim Levorsen, s185023
     */
    public void getOutOfJail() {
        this.set(Properties.JAIL_SPACE.getProperty(), null);
    }

    /**
     * Variabler og metoder til at implementere Subject
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     *
     */
    private Set<Observer> observers = new HashSet<Observer>();

    /**
     * Variabler og metoder til at implementere Subject
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     *
     */
    final public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Variabler og metoder til at implementere Subject
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     *
     */
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
