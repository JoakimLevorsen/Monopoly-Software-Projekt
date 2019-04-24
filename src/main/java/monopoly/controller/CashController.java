package monopoly.controller;

import monopoly.model.Player;
import monopoly.model.spaces.PropertySpace;

public class CashController {
    public GameController controller;

    public CashController(GameController owner) {
        this.controller = owner;
    }

    public void offerProperty(PropertySpace property, Player player) {
        // TODO: Implementering

    }

    public void payment(Player playerFrom, int amount, Player playerReceiver) {
        playerFrom.changeAccountBalance(-amount);
        playerReceiver.changeAccountBalance(amount);
        if (playerFrom.getAccountBalance() < 0) {
            controller.propertyController.playerBroke(playerFrom);
        }
        if (playerReceiver.getAccountBalance() < 0) {
            controller.propertyController.playerBroke(playerReceiver);
        }
    }

    public void paymentToBank(Player player, int amount) {
        player.changeAccountBalance(-amount);
        if (player.getAccountBalance() < 0) {
            controller.propertyController.playerBroke(player);
        }
    }

    public void paymentFromBank(Player player, int amount) {
        player.changeAccountBalance(amount);
        if (player.getAccountBalance() < 0) {
            controller.propertyController.playerBroke(player);
        }
    }
}
