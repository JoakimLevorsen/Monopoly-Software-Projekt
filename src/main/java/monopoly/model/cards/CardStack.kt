package monopoly.model.cards

import monopoly.model.Game
import org.javalite.activejdbc.Model
import org.javalite.activejdbc.validation.ValidationException
import org.json.JSONObject

import java.util.ArrayList
import java.util.Collections

/**
 * CardStack: Implementering af klasse til håndtering af en kortbunke
 *
 * @author Joakim Levorsen, s185023
 */
class CardStack : Model() {
    private var cards: List<Card>? = null

    /**
     * GetNextIndex: Henter index for det øverste kort i bunken
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer index for det "øverste" kort i bunken
     */
    /**
     * SetNextIndex: Sætter index for det øverste kort i bunken
     *
     * @param to Index, for det kort, der skal ligge øverst
     *
     * @author Joakim Bøegh Levorsen, s185023
     */
    private var nextIndex: Int
        get() = this.getInteger(CardStack.Properties.NEXT_CARD_INDEX.property)!!
        set(to) {
            this.set<Model>(CardStack.Properties.NEXT_CARD_INDEX.property, to)
        }

    /**
     * IsChanceCardStack: Bestemmer om kortbunken består af Chancekort
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer om kortbunken består af Chancekort eller ej
     */
    val isChanceCardStack: Boolean
        get() = this.getBoolean(CardStack.Properties.CHANCE_CARD.property)!!

    /**
     * Properties: Enumeration til at beskytte mod stavefejl
     */
    enum class Properties private constructor(val property: String) {
        CHANCE_CARD("chanceCard"), NEXT_CARD_INDEX("nextCardIndex")
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
    fun <C : Card> getCardForType(type: Class<C>): List<C> {
        val matches = ArrayList<C>()
        for (card in getCards()!!) {
            if (type.isInstance(card)) {
                matches.add(card as C)
            }
        }
        return matches
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
    fun drawCard(game: Game): Card {
        var drawnCard: Card
        do {
            this.nextIndex = this.nextIndex + 1
            if (this.nextIndex == this.getCards()!!.size) {
                // We get the cards, shuffle them and save their new position
                val cards = this.getCards()
                Collections.shuffle(cards!!)
                for (i in cards.indices) {
                    val card = cards[i]
                    card.stackPosition = i
                }
                this.nextIndex = 0
            }
            drawnCard = this.getCards()!![this.nextIndex]
        } while (drawnCard is GetOutOfJailCard && drawnCard.getOwner(game) != null)
        return drawnCard
    }

    /**
     * GetCards: Henter alle kort i en kortbunke fra databasen
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer en liste af kort
     */
    fun getCards(): List<Card>? {
        if (this.cards == null) {
            this.cards = DatabaseCardFactory.getCardsFor(this)
        }
        return this.cards
    }

    /**
     * SaveIt: Overskriver saveIt() for game, men kalder den på alle dens "børne"-elementer
     *
     * @author Joakim Bøegh Levorsen, s185023
     *
     * @return Returnerer hvorvidt der blev gemt succesfuldt
     */
    override fun saveIt(): Boolean {
        try {
            super.saveIt()
        } catch (e: ValidationException) {
            e.printStackTrace()
            return false
        }

        for (c in this.getCards()!!) {
            try {
                c.saveIt()
            } catch (e: ValidationException) {
                e.printStackTrace()
                return false
            }

        }
        return true
    }

    companion object {

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
        fun create(cardData: JSONObject, game: Game, isChanceCardStack: Boolean, nextCardIndex: Int): CardStack {
            val cardStack = CardStack()
            cardStack.set<Model>(CardStack.Properties.CHANCE_CARD.property, isChanceCardStack)
            cardStack.set<Model>(CardStack.Properties.NEXT_CARD_INDEX.property, 0)
            game.add(cardStack)
            if (isChanceCardStack) {
                JSONCardFactory.createChanceCards(cardData, cardStack)
            } else {
                JSONCardFactory.createCommunityChestCards(cardData, cardStack)
            }
            return cardStack
        }
    }
}
