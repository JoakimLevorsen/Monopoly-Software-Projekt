package monopoly.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.json.JSONObject;

import monopoly.model.Game;

/*
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
        if (isChanceCardStack) {
            JSONCardFactory.createChanceCards(cardData, cardStack);
        } else {
            JSONCardFactory.createCommunityChestCards(cardData, cardStack);
        }
        return cardStack;
    }

    /*
     * getCardForType: Finder alle kort af en type i sig selv.
     *
     * @author Joakim Levorsen, s185023
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

    public Card drawCard() {
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
        return this.getCards().get(this.getNextIndex());
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
}