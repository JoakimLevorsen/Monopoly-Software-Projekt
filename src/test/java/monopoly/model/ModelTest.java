package monopoly.model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

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
        Game newGame = Game.newGame("Slet mig pls");
        newGame.saveIt();
        ResourceManager manager = new ResourceManager();
        try {
            JSONObject boardData = manager.readFile(JSONFile.DA);
            // Create the stacks
            CardStack chanceStack = CardStack.create(true, 0);
            CardStack communityStack = CardStack.create(false, 0);
            newGame.add(chanceStack);
            newGame.add(communityStack);

            // Create the cards
            ArrayList<Card> chanceCards = JSONCardFactory.createChanceCards(boardData, chanceStack);
            ArrayList<Card> communityCards = JSONCardFactory.createCommunityChestCards(boardData, communityStack);

            // Create the board
            Space[] board = JSONSpaceFactory.createSpaces(boardData, newGame, chanceStack, communityStack);

            // Now the game has been created and saved, so we retrive it again, and check.

            Game loadedGame = Game.findFirst("id = ?", newGame.getId());
            List<Space> loadedBoard = loadedGame.getBoard();
            for (int i = 0; i < loadedBoard.size(); i++) {
                Space loadedSpace = loadedBoard.get(i);
                Space oldSpace = board[i];
                boolean comparison = loadedSpace.equals(oldSpace);
                assertTrue("Spaces not indentical: " + loadedSpace.toString() + "; " + oldSpace.toString(), comparison);
            }

            // Card comparison cant be done yet, since the nessecary methods dont exist.
            // List<CardStack> cardStacks = loadedGame.getCardStacks();
            // for (CardStack stack : cardStacks) {
            // if (stack.)
            // }

            List<Space> readBoard = newGame.getBoard();
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