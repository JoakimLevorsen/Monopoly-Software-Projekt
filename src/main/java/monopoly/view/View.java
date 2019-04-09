package monopoly.view;

import java.util.List;

import javax.swing.JOptionPane;

import java.util.HashMap;

import designpatterns.*;
import monopoly.model.Game;
import monopoly.model.Player;
import monopoly.model.spaces.*;
import gui_fields.*;
import gui_main.GUI;

/*
View:
Klasse til håndtering af spillets UI.

@author Cecilie Krog Drejer, s185032
*/

public class View implements Observer {
    private HashMap<Player, Integer> playerToPosition = new HashMap<Player, Integer>();
    private HashMap<Space, GUI_Field> spaceToField = new HashMap<Space, GUI_Field>();
    private HashMap<Player, GUI_Player> playerToGUIPlayer = new HashMap<Player, GUI_Player>();
    private HashMap<String, Game> saveNameToGame = new HashMap<String, Game>();
    //private Map<Player, PlayerPanel> panels = new HashMap<>();

    public Game chooseGame() {
        GUI chooseGameGUI = new GUI(new GUI_Field[0]);

        boolean loadGame = chooseGameGUI.getUserLeftButtonPressed("Vil du indlæse et gemt spil eller starte et nyt spil?", "Indlæs spil", "Start nyt spil");

        if (loadGame == true) {
            List<Game> savedGames = Game.findAll();
            if (savedGames.isEmpty()) {
                String saveName = chooseGameGUI.getUserString("Der er ingen gemte spil. Angiv et navn for at starte et nyt spil.");
                return Game.newGame(saveName);
            } else {
                String[] saveNames = new String[savedGames.size()];
                for (int i = 0; i < savedGames.size(); i++) {
                    saveNames[i] = savedGames.get(i).getGameName();
                    saveNameToGame.put(saveNames[i], savedGames.get(i));
                }

                String selection = JOptionPane.showInputDialog(null, "Vælg et spil.", "Indlæs spil", JOptionPane.QUESTION_MESSAGE, null, saveNames, saveNames[0]).toString();

                Game loadedGame = saveNameToGame.get(selection);
                return loadedGame;
            }
        } else {
            String saveName = chooseGameGUI.getUserString("Angiv et navn til dit nye spil.");
            return Game.newGame(saveName);
        }
    }

    public void update(Subject subject) {
        //TODO    
    }

    public void update(Player player) {
        //TODO
    }
}
//Dette skal være en del af updateplayer, for at opdatere panelerne.
//panels.get(player).Update();
