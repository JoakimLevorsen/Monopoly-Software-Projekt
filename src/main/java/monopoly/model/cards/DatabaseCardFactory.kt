package monopoly.model.cards

import sun.jvm.hotspot.gc.shared.Space
import java.util.ArrayList
import java.util.Collections
import kotlin.reflect.KClass

/**
 * DatabaseCardFactory: Klasse til at hente kort fra databasen.
 *
 * @author Joakim Bøegh Levorsen, s185023
 * @author Cecilie Krog Drejer, s185032
 */
object DatabaseCardFactory {
    /**
     * GetCardsFor: Henter kortene i en given kortbunke
     *
     * @param stack Den bunke, som der skal hentes kort fra
     *
     * @author Joakim Bøegh Levorsen, s185023
     * @author Cecilie Krog Drejer, s185032
     *
     * @return Returnerer en liste af Cards
     */
    fun getCardsFor(stack: CardStack): List<Card> {
        val classArray = arrayOf(GoToSpaceCard::class, GetLoseMoneyCard::class, GetOutOfJailCard::class, GoToJailCard::class)
        val allCards = ArrayList<Card>()
        for (classy in classArray) {
            allCards.addAll(stack.getAll<Space::class>(classy).load())
        }
        Collections.sort(allCards)
        return allCards
    }
}
