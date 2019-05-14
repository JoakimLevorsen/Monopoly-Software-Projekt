package monopoly.controller;

import gui_main.GUI;
import monopoly.model.Player;
import monopoly.model.spaces.StationSpace;

/*
* CashController er ansvarlig for at flytte penge rundt mellem bank og spillere
* @author Helle Achari, s180317
*/
public class CashController {
    public GameController controller;

    public CashController(GameController owner) {
        this.controller = owner;
    }

    public void offerProperty(StationSpace property, Player player) {
        GUI gui = controller.view.getGUI();
        boolean didChoose = gui.getUserLeftButtonPressed(" Do you want to buy this property? "
                + property.getBoardPosition() + "at the cost of: " + property.getPrice(), "Yes", "No");
        if (didChoose) {
            gui.showMessage("Yeah, you bought the" + property.getName() + "property!");
            controller.cashController.paymentToBank(player, property.getPrice());
            property.setOwner(player);
        }
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
