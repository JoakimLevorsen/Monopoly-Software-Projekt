package monopoly.model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import monopoly.model.cards.*;
import monopoly.model.spaces.*;
import resources.json.*;

/*
ModelTest:
En test til at verificere model klasserne virker korrekt.

@author Joakim Levorsen, S185023
*/

public class ModelTest {
    @Before
    public void setupTest() {
        DatabaseBase.openBase();
    }

    @Test
    public void createGameFromJSONAndSave() {
        try {
            ResourceManager manager = new ResourceManager();
            JSONFile language = JSONFile.DA;
            JSONObject languageData = manager.readFile(language);
            Game newGame = Game.newGame("Slet mig pls", language, languageData, 4);
            newGame.saveIt();
            List<Space> board = newGame.getBoard();

            // Now the game has been created and saved, so we retrive it again, and check.

            Game loadedGame = Game.findFirst("id = ?", newGame.getId());
            List<Space> loadedBoard = loadedGame.getBoard();
            for (int i = 0; i < loadedBoard.size(); i++) {
                Space loadedSpace = loadedBoard.get(i);
                Space oldSpace = board.get(i);
                boolean comparison = loadedSpace.equals(oldSpace);
                assertTrue("Spaces not indentical: " + loadedSpace.toString() + "; " + oldSpace.toString(), comparison);
            }

            // Check the cards
            for (CardStack loadedStack : loadedGame.getCardStacks()) {
                boolean stackIdentical = false;
                for (CardStack createdStack : newGame.getCardStacks()) {
                    if (createdStack.getLongId().equals(loadedStack.getLongId())) {
                        for (int i = 0; i < loadedStack.getCards().size(); i++) {
                            Card a = loadedStack.getCards().get(i);
                            Card b =  createdStack.getCards().get(i);
                            assertTrue("Cards different: " + a + b + ",Stack: " + loadedStack + createdStack, a.equals(b));
                        }
                        stackIdentical = true;
                    }
                }
                assertTrue("Card stacks were not identical" + loadedGame.getCardStacks() + newGame.getCardStacks(), stackIdentical);
            }
            newGame.deleteThisGame();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @After
    public void endTest() {
        DatabaseBase.closeBase();
    }
}
