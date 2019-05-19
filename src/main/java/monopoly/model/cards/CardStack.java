package monopoly.model.cards;

import monopoly.model.Game;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.ValidationException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CardStack: Implementering af klasse til håndtering af en kortbunke
 *
 * @author Joakim Levorsen, s185023
 */
public class CardStack extends Model {
    private List<Card> cards = null;

    /**
     * Properties: Enumeration til at beskytte mod stavefejl
     */
    public enum Properties {
        CHANCE_CARD("chanceCard"), NEXT_CARD_INDEX("nextCardIndex");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    /**
     * Create: Opretter en kortbunke
     * 
     * @param cardData Dataobjekt fra JSON
     * @param game Det spil, som kortbunken tilhører
     * @param isChanceCardStack Boolean om kortbunken indeholder Chancekort eller ej
     * @param nextCardIndex Index på det kort, der ligger øverst i bunken
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer en kortbunke
     */
    public static CardStack create(JSONObject cardData, Game game, boolean isChanceCardStack, int nextCardIndex) {
        CardStack cardStack = new CardStack();
        cardStack.set(CardStack.Properties.CHANCE_CARD.getProperty(), isChanceCardStack);
        cardStack.set(CardStack.Properties.NEXT_CARD_INDEX.getProperty(), 0);
        game.add(cardStack);
        if (isChanceCardStack) {
            JSONCardFactory.createChanceCards(cardData, cardStack);
        } else {
            JSONCardFactory.createCommunityChestCards(cardData, cardStack);
        }
        return cardStack;
    }

    /**
     * GetCardForType: Finder alle kort af en bestemt type i kortbunken
     * 
     * @param type Typen af kort på klasse-form (eksempelvis GetLoseMoneyCard.class)
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer en liste af kortene af den angivne type
     */
    public <C extends Card> List<C> getCardForType(Class<C> type) {
        List<C> matches = new ArrayList<C>();
        for (Card card : getCards()) {
            if (type.isInstance(card)) {
                matches.add((C) card);
            }
        }
        return matches;
    }

    /**
     * DrawCard: Trækker et kort fra en bunke
     * 
     * @param game Spillet, hvori der skal trækkes et kort
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer det trukne kort
     */
    public Card drawCard(Game game) {
        Card drawnCard;
        do {
            this.setNextIndex(this.getNextIndex() + 1);
            if (this.getNextIndex() == this.getCards().size()) {
                // We get the cards, shuffle them and save their new position
                List<Card> cards = this.getCards();
                Collections.shuffle(cards);
                for (int i = 0; i < cards.size(); i++) {
                    Card card = cards.get(i);
                    card.setStackPosition(i);
                }
                this.setNextIndex(0);
            }
            drawnCard = this.getCards().get(this.getNextIndex());
        } while (drawnCard instanceof GetOutOfJailCard && ((GetOutOfJailCard) drawnCard).getOwner(game) != null);
        return drawnCard;
    }

    /**
     * GetNextIndex: Henter index for det øverste kort i bunken
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer index for det "øverste" kort i bunken
     */
    private int getNextIndex() {
        return this.getInteger(CardStack.Properties.NEXT_CARD_INDEX.getProperty());
    }

    /**
     * SetNextIndex: Sætter index for det øverste kort i bunken
     * 
     * @param to Index, for det kort, der skal ligge øverst
     * 
     * @author Joakim Bøegh Levorsen, s185023
     */
    private void setNextIndex(int to) {
        this.set(CardStack.Properties.NEXT_CARD_INDEX.getProperty(), to);
    }

    /**
     * GetCards: Henter alle kort i en kortbunke fra databasen
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer en liste af kort
     */
    public List<Card> getCards() {
        if (this.cards == null) {
            this.cards = DatabaseCardFactory.getCardsFor(this);
        }
        return this.cards;
    }

    /**
     * IsChanceCardStack: Bestemmer om kortbunken består af Chancekort
     * 
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer om kortbunken består af Chancekort eller ej
     */
    public boolean isChanceCardStack() {
        return this.getBoolean(CardStack.Properties.CHANCE_CARD.getProperty());
    }

    /**
     * SaveIt: Overskriver saveIt() for game, men kalder den på alle dens "børne"-elementer
     *
     * @author Joakim Bøegh Levorsen, s185023
     * 
     * @return Returnerer hvorvidt der blev gemt succesfuldt
     */
    @Override
    public boolean saveIt() {
        try {
            super.saveIt();
        } catch (ValidationException e) {
            e.printStackTrace();
            return false;
        }

        for (Card c : this.getCards()) {
            try {
                c.saveIt();
            } catch (ValidationException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
