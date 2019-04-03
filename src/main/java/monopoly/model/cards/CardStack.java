package monopoly.model.cards;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.javalite.activejdbc.Model;

import designpatterns.Observer;
import designpatterns.Subject;

/*
CardStack:
Implementering af klasse til håndtering af et sæt kort.

@author Cecilie Krog Drejer, s185032
*/

public class CardStack extends Model implements Subject {

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
        cardStack.set(CardStack.Properties.NEXT_CARD_INDEX.getProperty(), 0);
        return cardStack;
    }

    public Card drawChanceCard() {
        // TODO: Implementer card klasser og returner kort
        return null;
    }

    public Card drawCommunityChestCard() {
        // TODO: Implementer card klasser og returner kort
        return null;
    }

    public Card[] getCards() {
        return new Card[0];
        // TODO
    }

    public boolean isChanceCardStack() {
        return true;
        // TODO
    }

    @Override
    public void addObserver(Observer observer) {
        //TODO
    }

    @Override
    public void removeObserver(Observer observer) {
        //TODO
    }

    @Override
    public List<Observer> getObservers() {
        //TODO
        return null;
    }
}