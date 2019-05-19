package monopoly.model.spaces;

import monopoly.controller.GameController;
import monopoly.model.Game;
import monopoly.model.Player;
import monopoly.model.cards.Card;
import monopoly.model.cards.CardStack;

/**
 * CardSpace: Implementering af CardSpace
 *
 * @author Joakim Bøegh Levorsen, s185023
 * @author Anders Brandt, s185016
 */
public class CardSpace extends Space {
    /**
     * Properties: Enumeration til at sikre mod stavefejl
     */
    public enum Properties {
        BOARD_POSITION("boardPosition"), CARD_STACK_ID("card_stack_id");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    /**
     * GetCardStackId: Henter en kortbunkes ID
     *
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer ID'et som en long
     */
    public long getCardStackId() {
        return this.getLong(Properties.CARD_STACK_ID.getProperty()).longValue();
    }

    /**
     * GetStack: Henter en kortbunker
     * 
     * @param game Det spil, som kortbunken tilhører
     *
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer en kortbunke
     */
    public CardStack getStack(Game game) {
        return game.getStackForID(this.getCardStackId());
    }

    /**
     * Create: Opretter et CardSpace
     * 
     * @param position Feltets placering på spillebrættet
     * @param stack Hvilken kortbunke feltet har
     * @param name Feltets navn
     * @param color Feltets farve
     *
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer et CardSpace
     */
    public static CardSpace create(int position, CardStack stack, String name, String color) {
        CardSpace space = new CardSpace();
        space = (CardSpace) (Space.setValues(space, name, color));
        space.set(CardSpace.Properties.BOARD_POSITION.getProperty(), position);
        stack.add(space);
        return space;
    }

    /**
     * Equals: Bestemmer om et CardSpace ligner et andet
     * 
     * @param obj Det objekt, som det pågældende CardSpace skal sammenlignes med
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer om de to ojekter er ens eller ej 
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CardSpace))
            return false;
        CardSpace other = (CardSpace) obj;
        return this.getBoardPosition() == other.getBoardPosition();
    }

    /**
     * PerformAction: Trækker et Chancekort og eksekverer det
     *
     * @param controller en GameController
     * @param player Spilleren, som lander på feltet
     * 
     * @author Anders Brandt, s185016
     */
    @Override
    public void performAction(GameController controller, Player player) {
        for (CardStack stack : controller.getGame().getCardStacks()) {
            if (stack.getLongId().longValue() == this.getCardStackId()) {
                Card drawnCard = stack.drawCard(controller.getGame());
                controller.view.getGUI().displayChanceCard(drawnCard.getText());
                drawnCard.execute(controller.movementController, player);
            }
        }
    }
}
