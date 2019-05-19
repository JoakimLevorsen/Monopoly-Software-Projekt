package monopoly.model.cards;

import monopoly.controller.MovementController;
import monopoly.model.Game;
import monopoly.model.Player;
import org.javalite.activejdbc.annotations.BelongsTo;

/**
 * GetOutOfJailCard: Implementering af Get Out of Jail Free Card
 *
 * @author Cecilie Krog Drejer, s185032
 * @author Joakim Bøegh Levorsen, s185023
 * @author Anders Brandt, s185016
 */
@BelongsTo(parent = Player.class, foreignKeyName = "player_id")
public class GetOutOfJailCard extends Card {
    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    public enum Properties {
        TEXT("text"), OWNER("player_id"), STACK_POSITION("stackPosition");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    /**
     * Create: Opretter et GetOutOfJailCard
     * 
     * @param text Tekst, der står på kortet
     * 
     * @author Cecilie Krog Drejer, s185032
     * 
     * @return Returnerer et GetOutOfJailCard
     */
    public static GetOutOfJailCard create(String text) {
        GetOutOfJailCard getOutOfJailCard = new GetOutOfJailCard();
        getOutOfJailCard.set(GetOutOfJailCard.Properties.TEXT.getProperty(), text);
        return getOutOfJailCard;
    }

    /**
     * GetOwner: Henter ejeren af det pågældende GetOutofJailFreeCard
     *
     * @param game Spillet, som kortet tilhører
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer ejeren (en Player)
     */
    public Player getOwner(Game game) {
        Object playerID = this.get(Properties.OWNER.getProperty());
        if (playerID != null) {
            if (playerID instanceof Integer) {
                Integer id = (Integer) playerID;
                return game.getPlayerForID(id.longValue());
            } else {
                System.out.println("Woah unexpected type of player id");
            }
        }
        return null;
    }

    /**
     * GetText: Henter teksten der står på kortet
     *
     * @author Anders Brandt, s185016
     * 
     * @return Returnerer teksten som en String
     */
    public String getText() {
        return this.getString(Properties.TEXT.getProperty());
    }

    /**
     * SetOwner: Sætter ejeren af det pågældende GetOutOfJailCard
     * 
     * @param player Spilleren, der skal være ejer af kortet
     * 
     * @author Joakim Bøegh Levorsen, s185023
     */
    public void setOwner(Player player) {
        if (player == null) {
            this.set(Properties.OWNER.getProperty(), null);
        }
        this.set(Properties.OWNER.getProperty(), player.getLongId());
    }

    /**
     * SetStackPosition: Sætter kortets placering i kortbunken
     * 
     * @param i Index, der angiver kortets ønskede placering
     * 
     * @author Cecilie Krog Drejer, s185032
     */
    @Override
    public void setStackPosition(int i) {
        this.set(GetOutOfJailCard.Properties.STACK_POSITION.getProperty(), i);
    }

    /**
     * GetStackPosition: Henter kortets placering i kortbunken
     * 
     * @author Cecilie Krog Drejer, s185032
     * 
     * @return Returnerer index for kortets placering i bunken
     */
    @Override
    public int getStackPosition() {
        return this.getInteger(GetOutOfJailCard.Properties.STACK_POSITION.getProperty());
    }

    /**
     * Equals: Bestemmer om et GetLoseMoneyCard ligner et andet
     * 
     * @param obj Det objekt, som det pågældende GetLoseMoneyCard skal sammenlignes med
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer om de to ojekter er ens eller ej 
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GetOutOfJailCard))
            return false;
        return ((GetOutOfJailCard) obj).getLongId().equals(this.getLongId());
    }

    /**
     * Execute: Sætter spilleren som ejer af kortet, så det kan bruges senere
     * 
     * @param moveController en MovementController
     * @param player Spilleren, som trækker kortet
     * 
     * @author Cecilie Krog Drejer, s185032
     */
    public void execute(MovementController moveController, Player player) {
        setOwner(player);
    }
}
