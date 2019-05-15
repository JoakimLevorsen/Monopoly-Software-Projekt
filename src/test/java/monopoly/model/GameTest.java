package monopoly.model;

import static org.junit.Assert.assertNotEquals;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GameTest {
    @Before
    public void setupTest() {
        DatabaseBase.openBase();
    }

    @Test
    public void testAllKeysPresent() {
//        Game g = Game.newGame("TEST GAME SAFE TO DELETE");
//
//        for (Game.Properties key : Game.Properties.values()) {
//            assertNotEquals(g.get(key.getProperty()), null);
//        }
//
//        g.deleteThisGame();
    }

    @After
    public void endTest() {
        DatabaseBase.closeBase();
    }
}
