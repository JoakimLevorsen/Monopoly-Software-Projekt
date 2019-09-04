package monopoly.controller

import monopoly.model.Player

/**
 * CashController er ansvarlig for at flytte penge rundt mellem bank og spillere
 *
 * @author Helle Achari, s180317
 */
class CashController
/**
 * CashController constructor
 *
 * @param owner GameController som "ejer" CashController
 *
 * @author Helle Achari, s180317
 */
(var controller: GameController) {

    /**
     * Payment: Betaling fra en spiller til en anden
     *
     * @param playerFrom     Spiller, som skal betale
     * @param amount         Beløb, der skal betales
     * @param playerReceiver Spiller, som skal modtage betalingen
     *
     * @author Helle Achari, s180317
     */
    fun payment(playerFrom: Player, amount: Int, playerReceiver: Player) {
        playerFrom.changeAccountBalance(-amount)
        playerReceiver.changeAccountBalance(amount)
    }

    /**
     * PaymentToBank: Betaling fra spiller til banken
     *
     * @param player Spiller, der skal betale
     * @param amount Beløb, der skal betales
     *
     * @author Helle Achari, s180317
     */
    fun paymentToBank(player: Player, amount: Int) {
        player.changeAccountBalance(-amount)
    }

    /**
     * PaymentToBank: Betaling fra banken til en spiller
     *
     * @param player Spiller, der skal modtage betalingen
     * @param amount Beløb, der skal betales
     *
     * @author Helle Achari, s180317
     */
    fun paymentFromBank(player: Player, amount: Int) {
        player.changeAccountBalance(amount)
    }
}
