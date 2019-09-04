package monopoly.model.cards

import monopoly.controller.MovementController
import monopoly.model.Player
import org.javalite.activejdbc.Model

/**
 * Card: Implementering af abstract kort-klasse.
 *
 * @author Joakim Levorsen, s185023
 * @author Cecilie Krog Drejer, s185032
 */
abstract class Card : Model(), Comparable<Card> {

    abstract var stackPosition: Int

    abstract val text: String
    abstract fun execute(moveController: MovementController, player: Player)

    /**
     * CompareTo: Sammenligner et kort med et andet
     *
     * @param that Det kort, der skal sammenlignes med
     *
     * @author Cecilie Krog Drejer, s185032
     *
     * @return Returnerer forskellen på de to korts placering i en kortbunke
     */
    override fun compareTo(that: Card): Int {
        return this.stackPosition - that.stackPosition
    }
}
