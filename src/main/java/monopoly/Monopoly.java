package monopoly;

import gui_main.GUI;
import monopoly.controller.GameController;
import monopoly.model.Game;
import monopoly.view.View;

/**
 * Monopoly: Klasse fra hvilken softwaren køres
 * 
 * @author Joakim Bøegh Levorsen, s185023
 */
public final class Monopoly {
    /**
     * Main: Kører softwaren
     * 
     * @param args
     * 
     * @author Joakim Bøegh Levorsen, s185023
     */
    public static void main(String[] args) {
        DatabaseBase.openBase();
        Game ourGame = View.chooseGame();
        GUI gameGUI = new GUI(ourGame.exportGUIFields());

        View gameView = new View(ourGame, gameGUI);
        GameController controller = new GameController(ourGame, gameView);
        controller.play();

        DatabaseBase.closeBase();
    }
}
