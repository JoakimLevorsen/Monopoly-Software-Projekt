package monopoly;

import java.util.List;

import org.javalite.activejdbc.Base;
import org.json.JSONObject;

import monopoly.model.Game;
import monopoly.model.Player;
import monopoly.model.spaces.JSONSpaceFactory;
import monopoly.model.spaces.Space;
import resources.json.JSONException;
import resources.json.JSONFile;
import resources.json.ResourceManager;

public final class Monopoly {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Base.open("com.mysql.cj.jdbc.Driver", "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s185023",
                "s185023", "t0MzfHeQBfHIlo8ociaB2");
        // DriverManager.getConnection("jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s185023?"
        // + "user=s185023&password=t0MzfHeQBfHIlo8ociaB2");
        // Game newGame = Game.newGame("Perter");
        // newGame.saveIt();
        // newGame.addPlayer(Player.newPlayer("per", 0, 1000));
        // newGame.addPlayer(Player.newPlayer("ker", 1, 1200));
        // newGame.addPlayer(Player.newPlayer("der", 2, 1500));
        // System.out.println(newGame);
        // for (Player p: newGame.getPlayers()) {
        // System.out.println(p.toString());
        // }
        Game newGame = Game.newGame("Slet mig pls");
        ResourceManager manager = new ResourceManager();
        try {
            JSONObject boardData = manager.readFile(JSONFile.DA);
            Space[] board = JSONSpaceFactory.createSpaces(boardData, newGame);

            System.out.println("Board is" + board.toString());

            System.out.println("Get the board");
            List<Space> readBoard = newGame.getBoard();
            System.out.println("Board from database is " + readBoard.toString());
            newGame.deleteThisGame();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Base.close();
    }
}
