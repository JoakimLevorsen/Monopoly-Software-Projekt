package monopoly.view;

import java.awt.*;
import java.util.List;

import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import designpatterns.*;
import gui_fields.*;
import gui_main.GUI;
import monopoly.model.*;
import monopoly.model.spaces.*;
import resources.json.JSONFile;
import resources.json.JSONKey;
import resources.json.ResourceManager;

public class View implements Observer {

    private Game game;
    private GUI gui;
    private JSONObject jsonData; 

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
        this.jsonData = game.getLanguageData(); 
        GUI_Field[] guiFields = gui.getFields();

        int i = 0;
        for (Space space : game.getBoard()) {

            space.addObserver(this);
            spaceToField.put(space, guiFields[i++]);

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
                "Do you want to load a saved game or start a new game? / Vil du indlæse et gemt spil eller starte et nyt spil?", "Load game / Indlæs spil", "Start new game / Start nyt spil");

        if (loadGame == true) {
            List<Game> savedGames = Game.findAll();
            if (savedGames.isEmpty()) {
                String saveName = chooseGameGUI
                        .getUserString("There are no saved games. Type a name to start a new game / Der er ingen gemte spil. Angiv et navn for at starte et nyt spil.");
                int playerAmount = chooseGameGUI.getUserInteger("How many players? / Hvor mange spillere?", 2, 4);
                try {
                    JSONFile language = chooseLanguage();
                    JSONObject languageData = rm.readFile(language);
                    chooseGameGUI.close();
                    return Game.newGame(saveName, language, languageData, playerAmount);
                } catch (JSONException e) {
                    System.out.println("Could not find resource");
                    System.err.println(e);
                    chooseGameGUI.close();
                    return null;
                }
            } else {
                String[] saveNames = new String[savedGames.size()];
                for (int i = 0; i < savedGames.size(); i++) {
                    saveNames[i] = savedGames.get(i).getGameName();
                    saveNameToGame.put(saveNames[i], savedGames.get(i));
                }

                String selection = JOptionPane.showInputDialog(null, "Choose a game / Vælg et spil.", "Load game / Indlæs spil",
                        JOptionPane.QUESTION_MESSAGE, null, saveNames, saveNames[0]).toString();

                Game loadedGame = saveNameToGame.get(selection);
                chooseGameGUI.close();
                return loadedGame;
            }
        } else {
            String saveName = chooseGameGUI.getUserString("Type a name for your new game / Angiv et navn til dit nye spil.");
            int playerAmount = chooseGameGUI.getUserInteger("How many players? / Hvor mange spillere?", 2, 4);
            try {
                JSONFile language = chooseLanguage();
                JSONObject languageData = rm.readFile(language);
                chooseGameGUI.close();
                return Game.newGame(saveName, language, languageData, playerAmount);
            } catch (JSONException e) {
                System.out.println("Could not find resource");
                System.err.println(e);
                chooseGameGUI.close();
                return null;
            }
        }
    }

    public static JSONFile chooseLanguage() throws JSONException {
        HashMap<String, JSONFile> languageChoices = new HashMap<String, JSONFile>();
        List<String> stringChoices = new ArrayList<String>();
        for (JSONFile file : JSONFile.values()) {
            String text;
            switch (file.getPackName()) {
            case "da":
                text = "Dansk";
                break;
            case "uk":
                text = "English (UK)";
                break;
            case "us":
                text = "English (US)";
                break;
            default:
                text = "Unknown Language: " + file.getPackName();
            }
            languageChoices.put(text, file);
            stringChoices.add(text);
        }
        String[] stringArray = stringChoices.toArray(new String[stringChoices.size()]);
        String choice = JOptionPane.showInputDialog(null, "Choose a language / Vælg et sprog", "Vælg sprog / Choose language",
                JOptionPane.QUESTION_MESSAGE, null, stringArray, stringArray[0]).toString();
        return languageChoices.get(choice);
    }

    public PropertySpace whichPropertyDoWantToBuildOn(List<PropertySpace> possibleChoices) {
        HashMap<String, PropertySpace> targets = new HashMap<String, PropertySpace>();
        List<String> choices = new ArrayList<String>();
        for (PropertySpace p : possibleChoices) {
            targets.put(p.getName(), p);
            choices.add(p.getName());
        }
        String[] stringArray = choices.toArray(new String[choices.size()]);
        String choice = JOptionPane.showInputDialog(null, jsonData.getString(JSONKey.WHICH_PROPERTY_TO_BUILD_ON.getKey()), jsonData.getString(JSONKey.CHOOSE_PROPERTY.getKey()),
                JOptionPane.QUESTION_MESSAGE, null, stringArray, stringArray[0]).toString();
        return targets.get(choice);
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
            } else if (subject instanceof StationSpace) {
                updateProperty((StationSpace) subject);
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
     * 
     * @author Cecilie Krog Drejer, s185032
     */
    private void updateProperty(StationSpace property) {
        GUI_Field thisField = this.spaceToField.get(property);
        GUI_Ownable thisOwnableField = (GUI_Ownable) thisField;
        thisOwnableField.setRentLabel(jsonData.getString(JSONKey.RENT.getKey()) + " " + String.valueOf(property.getRent(game)));
        if (thisOwnableField != null) {
            if (property.getOwner(game) != null) {
                if (property.isMortgaged()) {
                    thisOwnableField.setOwnableLabel(jsonData.getString(JSONKey.MORTGAGE_BY.getKey())); 
                    thisOwnableField.setOwnerName(property.getOwner(game).getName());
                    thisOwnableField.setBorder(Color.BLACK);
                } else {
                    thisOwnableField.setOwnableLabel(jsonData.getString(JSONKey.OWNED_BY.getKey()));
                    thisOwnableField.setOwnerName(property.getOwner(game).getName());
                    thisOwnableField.setBorder(property.getOwner(game).getColor());
                }
            } else {
                thisOwnableField.setOwnableLabel("");
                thisOwnableField.setOwnerName("");
                thisOwnableField.setBorder(null);
            }
        }
        if (property instanceof PropertySpace) {
            GUI_Street thisStreet = (GUI_Street) thisField;
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
