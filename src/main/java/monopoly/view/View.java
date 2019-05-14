package monopoly.view;

import java.awt.*;
import java.util.List;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import designpatterns.*;
import gui_fields.*;
import gui_main.GUI;
import monopoly.model.*;
import monopoly.model.spaces.*;
import resources.json.JSONFile;
import resources.json.ResourceManager;

public class View implements Observer {

    private Game game;
    private GUI gui;

    private HashMap<Player, Integer> playerToPosition = new HashMap<>();
    private HashMap<Space, GUI_Field> spaceToField = new HashMap<>();
    private HashMap<Player, GUI_Player> playerToGUIPlayer = new HashMap<>();
    private HashMap<Player, PlayerPanel> panels = new HashMap<>();
    private boolean disposed = false;

    /*
     * View: opretter felter og spillere
     * 
     * @author Ekkart Kindler
     * 
     * @author Anders Brandt s185016
     */
    public View(Game game, GUI gui) {
        this.game = game;
        this.gui = gui;
        GUI_Field[] guiFields = gui.getFields();

        int i = 0;
        for (Space space : game.getBoard()) {
            // TODO, here we assume that the games fields fit to the GUI's fields;
            // the GUI fields should actually be created according to the game's
            // fields
            space.addObserver(this);
            spaceToField.put(space, guiFields[i++]);

            // TODO we should also register with the properties as observer; but
            // the current version does not update anything for the spaces, so we do not
            // register the view as an observer for now
        }

        // create the players in the GUI
        for (Player player : game.getPlayers()) {
            GUI_Car car = new GUI_Car(player.getColor(), Color.black, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL);
            GUI_Player guiPlayer = new GUI_Player(player.getName(), player.getAccountBalance(), car);
            playerToGUIPlayer.put(player, guiPlayer);
            gui.addPlayer(guiPlayer);

            // register this view with the player as an observer, in order to update the
            // player's state in the GUI
            player.addObserver(this);
            PlayerPanel playerPanel = new PlayerPanel(game, player);
            panels.put(player, playerPanel);
            updatePlayer(player);
        }
    }

    public static Game chooseGame() {
        HashMap<String, Game> saveNameToGame = new HashMap<>();
        ResourceManager rm = new ResourceManager();
        GUI chooseGameGUI = new GUI(new GUI_Field[0]);

        boolean loadGame = chooseGameGUI.getUserLeftButtonPressed(
                "Vil du indlæse et gemt spil eller starte et nyt spil?", "Indlæs spil", "Start nyt spil");

        if (loadGame == true) {
            List<Game> savedGames = Game.findAll();
            if (savedGames.isEmpty()) {
                String saveName = chooseGameGUI
                        .getUserString("Der er ingen gemte spil. Angiv et navn for at starte et nyt spil.");
                int playerAmount = chooseGameGUI.getUserInteger("Hvor mange spillere?", 1, 4);
                try {
                    JSONFile language = chooseLanguage();
                    JSONObject languageData = rm.readFile(language);
                    return Game.newGame(saveName, language, languageData, playerAmount);
                } catch (resources.json.JSONException e) {
                    System.out.println("Could not find resource");
                    System.err.println(e);
                    return null;
                }
            } else {
                String[] saveNames = new String[savedGames.size()];
                for (int i = 0; i < savedGames.size(); i++) {
                    saveNames[i] = savedGames.get(i).getGameName();
                    saveNameToGame.put(saveNames[i], savedGames.get(i));
                }

                String selection = JOptionPane.showInputDialog(null, "Vælg et spil.", "Indlæs spil",
                        JOptionPane.QUESTION_MESSAGE, null, saveNames, saveNames[0]).toString();

                Game loadedGame = saveNameToGame.get(selection);
                return loadedGame;
            }
        } else {
            String saveName = chooseGameGUI.getUserString("Angiv et navn til dit nye spil.");
            int playerAmount = chooseGameGUI.getUserInteger("Hvor mange spillere?", 1, 4);
            try {
                JSONFile language = chooseLanguage();
                JSONObject languageData = rm.readFile(language);
                return Game.newGame(saveName, language, languageData, playerAmount);
            } catch (resources.json.JSONException e) {
                System.out.println("Could not find resource");
                System.err.println(e);
                return null;
            }
        }
    }

    public static JSONFile chooseLanguage() throws resources.json.JSONException {
        HashMap<String, JSONFile> languageChoices = new HashMap<String, JSONFile>();
        List<String> stringChoices = new ArrayList<String>();
        for (JSONFile file : JSONFile.values()) {
            String text;
            switch (file.getFileName()) {
            case "da":
                text = "Dansk";
                break;
            case "en":
                text = "English";
                break;
            case "am":
                text = "American";
                break;
            default:
                text = "Unknown Language: " + file.getFileName();
            }
            languageChoices.put(text, file);
            stringChoices.add(text);
        }
        String[] stringArray = (String[]) stringChoices.toArray();
        String choice = JOptionPane.showInputDialog(null, "Choose a language", "Choose language",
                JOptionPane.QUESTION_MESSAGE, null, stringArray, stringArray[0]).toString();
        return languageChoices.get(choice);
    }

    /*
     * View: opdaterer brættet
     * 
     * @author Ekkart Kindler
     * 
     * @author Anders Brandt s185016
     */
    public void update(Subject subject) {
        if (!disposed) {
            if (subject instanceof Player) {
                updatePlayer((Player) subject);
            } else if (subject instanceof PropertySpace) {
                updateProperty((PropertySpace) subject);
            } else if (subject instanceof StationSpace) {
                updateStation((StationSpace) subject);
            }

        }
    }

    /*
     * View: Opdaterer spilleren
     * 
     * @author Ekkart Kindler
     * 
     * @author Anders Brandt s185016
     */
    private void updatePlayer(Player player) {
        GUI_Player guiPlayer = this.playerToGUIPlayer.get(player);
        if (guiPlayer != null) {
            guiPlayer.setBalance(player.getAccountBalance());
            GUI_Field[] guiFields = gui.getFields();
            Integer oldPosition = playerToPosition.get(player);
            if (oldPosition != null && oldPosition < guiFields.length) {
                guiFields[oldPosition].setCar(guiPlayer, false);
            }
            int pos = player.getBoardPosition();
            if (pos < guiFields.length) {
                playerToPosition.put(player, pos);
                guiFields[pos].setCar(guiPlayer, true);
            }

            String name = player.getName();
            if (player.hasOverdrawnAccount()) {
            }
            if (!name.equals(guiPlayer.getName())) {
                guiPlayer.setName(name);
            }
        }
        panels.get(player).Update();
    }

    /*
     * View: Opdaterer ejendommene
     * 
     * @author Ekkart Kindler
     * 
     * @author Anders Brandt s185016
     * @author Cecilie Krog Drejer, s185032
     */
    private void updateProperty(StationSpace property) {
        GUI_Field thisField = this.spaceToField.get(property);
        GUI_Ownable thisOwnableField = (GUI_Ownable) thisField;
        GUI_Street thisStreet = (GUI_Street) thisField;
        if (thisOwnableField != null) {
            if (property.getOwner(game) != null) {
                if (property.isMortgaged()) {
                    thisOwnableField.setOwnableLabel("Mortgaged by: ");
                    thisOwnableField.setOwnerName(property.getOwner(game).getName());
                    thisOwnableField.setBorder(Color.BLACK);
                } else {
                    thisOwnableField.setOwnableLabel("Owned by: ");
                    thisOwnableField.setOwnerName(property.getOwner(game).getName());
                    thisOwnableField.setBorder(property.getOwner(game).getColor());
                }
            } else {
                thisOwnableField.setOwnableLabel("");
                thisOwnableField.setOwnerName("");
                thisOwnableField.setBorder(null);
            }
        }
        if (thisStreet != null && (property instanceof PropertySpace)) {
            if (((PropertySpace) property).getHousesBuilt() == 5) {
                thisStreet.setHotel(true);
                thisStreet.setHouses(0);
            } else {
                thisStreet.setHotel(false);
                thisStreet.setHouses(((PropertySpace) property).getHousesBuilt());
            }
            this.updateProperty(property);
        }
    }

    /*
     * getGUI: Retuner dette views gui
     * 
     * @author Joakim Levorsen, S185023
     */
    public GUI getGUI() {
        return gui;
    }

    public void dispose() {
        if (!disposed) {
            disposed = true;
            for (Player player : game.getPlayers()) {
                player.removeObserver(this);
            }
            for (Space space : game.getBoard()) {
                space.removeObserver(this);
            }
        }
    }

}
