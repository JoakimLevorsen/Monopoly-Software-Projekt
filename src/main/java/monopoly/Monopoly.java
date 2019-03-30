package monopoly;

import org.javalite.activejdbc.Base;

import monopoly.model.Game;
import monopoly.model.Player;

public final class Monopoly {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Base.open("com.mysql.cj.jdbc.Driver", "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s185023", "s185023", "t0MzfHeQBfHIlo8ociaB2");
        // DriverManager.getConnection("jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s185023?" + "user=s185023&password=t0MzfHeQBfHIlo8ociaB2");
        Game newGame = Game.newGame("Perter");
        newGame.saveIt();
        newGame.addPlayer(Player.newPlayer("per", 0, 1000));
        newGame.addPlayer(Player.newPlayer("ker", 1, 1200));
        newGame.addPlayer(Player.newPlayer("der", 2, 1500));
        System.out.println(newGame);
        for (Player p: newGame.getPlayers()) {
            System.out.println(p.toString());
        }
        Base.close();
    }
}
