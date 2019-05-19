package monopoly.controller;

import monopoly.model.Player;

/**
 * CashController er ansvarlig for at flytte penge rundt mellem bank og spillere
 *
 * @author Helle Achari, s180317
 */

public class CashController {
    public GameController controller;

    /**
     * @param owner
     */
    public CashController(GameController owner) {
        this.controller = owner;
    }

    /**
     * @param playerFrom
     * @param amount
     * @param playerReceiver
     */
    public void payment(Player playerFrom, int amount, Player playerReceiver) {
        playerFrom.changeAccountBalance(-amount);
        playerReceiver.changeAccountBalance(amount);
    }

    /**
     * @param player
     * @param amount
     */
    public void paymentToBank(Player player, int amount) {
        player.changeAccountBalance(-amount);
    }

    /**
     * @param player 
     * @param amount
     */
    public void paymentFromBank(Player player, int amount) {
        player.changeAccountBalance(amount);
    }
}
