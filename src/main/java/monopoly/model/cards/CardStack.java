package monopoly.model.cards;

import monopoly.model.Game;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.ValidationException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
CardStack:
Implementering af klasse til håndtering af et sæt kort.

@author Cecilie Krog Drejer, s185032
@author Joakim Levorsen, s185023
*/

public class CardStack extends Model {
    private List<Card> cards = null;

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
     * @param type man vil finde kort for
     * @return Alle kort af typen C
     * @author jake
     * @author Joakim Levorsen s185023
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

    private int getNextIndex() {
        return this.getInteger(CardStack.Properties.NEXT_CARD_INDEX.getProperty());
    }

    private void setNextIndex(int to) {
        this.set(CardStack.Properties.NEXT_CARD_INDEX.getProperty(), to);
    }

    public List<Card> getCards() {
        if (this.cards == null) {
            this.cards = DatabaseCardFactory.getCardsFor(this);
        }
        return this.cards;
    }

    public boolean isChanceCardStack() {
        return this.getBoolean(CardStack.Properties.CHANCE_CARD.getProperty());
    }

    /**
     * saveIt: Overskriver saveIt for game, men kalder den på alle dens "børne"
     * elementer.
     *
     * @author Joakim Levorsen, S185023
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
