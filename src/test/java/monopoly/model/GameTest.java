package monopoly.model;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class GameTest {
    @Test
    public void testAllKeysPresent() {
        Game g = Game.newGame("TEST GAME SAFE TO DELETE");

        for (Game.Properties key: Game.Properties.values()) {
            assertNotEquals(g.get(key.getProperty()), null);
        }

        g.deleteThisGame();
    }
}