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

    public void paymentToPlayer(Player playerFrom, int amount, Player playerReceiver) {
        // TODO: Implementering
    }

    public void paymentFromBank(Player player, int amount) {
        // TODO: Implementering
    }

    public void paymentToBank(Player player, int amount) {
        // TODO: Implementering
    }
}
