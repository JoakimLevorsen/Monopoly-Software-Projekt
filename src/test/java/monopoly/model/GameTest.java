package monopoly.model;

import static org.junit.Assert.assertNotEquals;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GameTest {
    @Before
    public void setupTest() {
        Base.open("com.mysql.cj.jdbc.Driver", "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s185023",
                "s185023", "t0MzfHeQBfHIlo8ociaB2");
    }

    @Test
    public void testAllKeysPresent() {
        Game g = Game.newGame("TEST GAME SAFE TO DELETE");

        for (Game.Properties key: Game.Properties.values()) {
            assertNotEquals(g.get(key.getProperty()), null);
        }

        g.deleteThisGame();
    }

    @After
    public void endTest() {
        Base.close();
    }
}