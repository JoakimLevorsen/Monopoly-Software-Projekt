package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.Game;
import monopoly.model.Player;

/**
 * StationSpace: Et objekt til at repræsentere stationer.
 *
 * @author Joakim Bøegh Levorsen, s185023
 * @author Cecilie Krog Drejer, s185032
 * @author Anders Brandt, s185016
 * @author Helle Achari, s180317
 */
public class StationSpace extends Space {
    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    public enum Properties {
        BOARD_POSITION("boardPosition"), NAME("name"), MORTGAGED("mortgaged"), PRICE("price"), BASE_RENT("baseRent"),
        OWNER("player_id");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    /**
     * PerformAction: Betaler leje hvis feltet er ejet af en anden spiller, gør
     * ingenting hvis feltet er ejet af den spiller, der lander på feltet, eller
     * tilbyder spilleren at købe ejendommen, hvis den ikke er ejet af nogen
     *
     * @param controller en GameController
     * @param player     Spilleren, som lander på feltet
     * 
     * @author Joakim Bøegh Levorsen, s185023
     */
    @Override
    public void performAction(GameController controller, Player player) {
        Player owner = this.getOwner(controller.getGame());
        if (owner != null && !isMortgaged()) {
            if (!owner.equals(player)) {
                controller.cashController.payment(player, this.getRent(controller.getGame()), owner);
                // TODO: JSON it up
                controller.view.getGUI().showMessage(
                        player.getName() + " betaler " + getRent(controller.getGame()) + " til " + owner.getName());
            }
        } else
            controller.propertyController.offerProperty(this, player);
    }

    /**
     * Create: Opretter et StationSpace
     * 
     * @param position Feltets placering på spillebrættet
     * @param name     Feltets navn
     * @param price    Ejendommens pris
     * @param baseRent Ejendommens basisleje
     * @param color    Feltets farve
     *
     * @author Joakim Bøegh Levorsen, s185023
     * @author Cecilie Krog Drejer, s185032
     * 
     * @return Returnerer et StationSpace
     */
    public static StationSpace create(int position, String name, int price, int baseRent, String color) {
        StationSpace space = new StationSpace();
        space = (StationSpace) (Space.setValues(space, name, color));
        space.set(StationSpace.Properties.BOARD_POSITION.getProperty(), position);
        space.set(StationSpace.Properties.NAME.getProperty(), name);
        space.set(StationSpace.Properties.MORTGAGED.getProperty(), false);
        space.set(StationSpace.Properties.PRICE.getProperty(), price);
        space.set(StationSpace.Properties.BASE_RENT.getProperty(), baseRent);
        return space;
    }

    /**
     * Equals: Bestemmer om et StationSpace ligner et andet
     * 
     * @param obj Det objekt, som det pågældende StationSpace skal sammenlignes med
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer om de to ojekter er ens eller ej
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StationSpace))
            return false;
        StationSpace other = (StationSpace) obj;
        return this.getBoardPosition() == other.getBoardPosition();
    }

    /**
     * SetOwner: Sætter en spiller som ejer af ejendommen
     * 
     * @param player Spilleren, som skal sættes som ejer
     * 
     * @author Cecilie Krog Drejer, s185032
     */

    public void setOwner(Player player) {
        player.add(this);
    }

    /**
     * RemoveOwner: Fjerner den nuværende ejer af ejendommen
     * 
     * @author Cecilie Krog Drejer, s185032
     */

    public void removeOwner() {
        this.set(Properties.OWNER.getProperty(), null);
    }

    /**
     * GetOwner: Henter ejendommens ejer
     * 
     * @param game Det spil, som ejendommen tilhører
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer ejeren (en Player)
     */
    public Player getOwner(Game game) {
        Player owner = this.parent(Player.class);
        if (owner != null) {
            long id = owner.getLongId();
            return game.getPlayerForID(id);
        }
        return null;
    }

    /**
     * GetRent: Henter lejen for stationen
     * 
     * @param game Det spil, som stationen tilhører
     * 
     * @author Anders Brandt, s185016
     * 
     * @return Returnerer stationens leje
     */
    public int getRent(Game game) {
        int baseRent = this.getInteger(Properties.BASE_RENT.getProperty());
        int amountOwned = 0;
        Player owner = this.getOwner(game);
        if (owner == null) {
            return 0;
        }
        for (Space space : game.getBoard()) {
            if (space instanceof StationSpace && !(space instanceof PropertySpace)) {
                Player otherOwner = ((StationSpace) space).getOwner(game);
                if (otherOwner != null && otherOwner.equals(owner))
                    amountOwned++;
            }
        }
        return baseRent * amountOwned;
    }

    /**
     * GetPrice: Henter ejendommens pris
     * 
     * @author Helle Achari, s180317
     * 
     * @return Returnerer prisen for ejendommen
     */
    public int getPrice() {
        return this.getInteger(Properties.PRICE.getProperty());
    }

    /**
     * IsMortgaged: Bestemmer om ejendommen er pantsat
     * 
     * @author Cecilie Krog Drejer, s185032
     * 
     * @return Returnerer om ejendommen er pantsat eller ej
     */
    public boolean isMortgaged() {
        return this.getBoolean(Properties.MORTGAGED.getProperty());
    }

    /**
     * SetMortgaged: Sætter ejendommens pantsat-status
     * 
     * @param mortgaged Boolean om ejendommen er pantsat eller ej
     * 
     * @author Cecilie Krog Drejer, s185032
     */
    public void setMortgaged(boolean mortgaged) {
        this.set(Properties.MORTGAGED.getProperty(), mortgaged);
    }
}
