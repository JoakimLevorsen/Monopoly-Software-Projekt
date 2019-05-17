package monopoly;

import gui_main.GUI;
import monopoly.controller.GameController;
import monopoly.DatabaseBase;
import monopoly.model.Game;
import monopoly.view.View;

public final class Monopoly {
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
