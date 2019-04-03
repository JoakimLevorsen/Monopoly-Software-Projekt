package monopoly.model.cards;

import java.util.*;

import monopoly.model.Game;

public class DatabaseCardFactory {
    
    public static List<Card> getCardsFor(Game game) {
        //TODO: edit to fit different cardstacks
        Class<? extends Card>[] classArray = new Class[] {GoToSpaceCard.class, GetLoseMoneyCard.class, GetOutOfJailCard.class, GoToJailCard.class};

    List<Card> allCards = new ArrayList<Card>();
    for (Class<? extends Card> classy : classArray) {
        allCards.addAll(game.getAll(classy).load());
    }
    Collections.sort(allCards);
    return allCards;
    }
}