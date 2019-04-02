package monopoly.model.cards;

import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

import designpatterns.Subject;

/*
CardStack:
Implementering af klasse til håndtering af et sæt kort.

@author Cecilie Krog Drejer, s185032
*/

public class CardStack extends Subject {

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

    public static CardStack create(boolean chanceCard, int nextCardIndex) {
        CardStack cardStack = new CardStack();
        cardStack.set(CardStack.Properties.CHANCE_CARD.getProperty(), chanceCard);
        cardStack.set(CardStack.Properties.NEXT_CARD_INDEX.getProperty(), nextCardIndex);
        return cardStack;
    }

    public void shuffle(Card[] cards) {
        Collections.shuffle(Arrays.asList(cards));
    }

    public Card draw() {
        // TODO: Impementer card classer og retuner kort
        return null;
    }
}