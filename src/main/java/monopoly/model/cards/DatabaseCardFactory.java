package monopoly.model.cards;

import java.util.*;

/*
DatabaseCardFactory:
Klasse til at hente kort fra databasen.

@author Joakim Levorsen, s185023
@author Cecilie Krog Drejer, s185032
*/

public class DatabaseCardFactory {

    public static List<Card> getCardsFor(CardStack stack) {
        // TODO: edit to fit different cardstacks
        Class<? extends Card>[] classArray = new Class[] { GoToSpaceCard.class, GetLoseMoneyCard.class,
                GetOutOfJailCard.class, GoToJailCard.class };

        List<Card> allCards = new ArrayList<Card>();
        for (Class<? extends Card> classy : classArray) {
            allCards.addAll(stack.getAll(classy).load());
        }
        Collections.sort(allCards);
        return allCards;
    }
}