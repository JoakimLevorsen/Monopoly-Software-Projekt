package monopoly.view;

import monopoly.model.Game;
import monopoly.model.Player;
import monopoly.model.spaces.PropertySpace;
import monopoly.model.spaces.Space;
import monopoly.model.spaces.StationSpace;
import resources.json.JSONKey;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

/*
PlayerPanel:
Opretter paneler med information omkring spilleren og dets ejendomme.

@author Anders Brandt, s185016
*/

public class PlayerPanel extends JFrame {
    private JSONObject jsonData; 
    private Game game;
    private Player player;
    final JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JPanel mainPanel = new JPanel();

    public PlayerPanel(Game game, Player player) {
        this.player = player;
        this.game = game;
        this.jsonData = game.getLanguageData();
        frame.setTitle(player.getName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(700, game.getPlayers().indexOf(player) * 300);
        frame.setPreferredSize(new Dimension(500, 200));
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
        Update();
    }

    public void Update() {
        panel.removeAll();

        panel.setMinimumSize(new Dimension(100, 100));
        panel.setMaximumSize(new Dimension(100, 100));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        panel.setOpaque(true);
        panel.setBackground(player.getColor());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        JLabel pLabel = new JLabel(jsonData.getString(JSONKey.NAME.getKey()) + player.getName());
        panel.add(pLabel);

        pLabel = new JLabel(jsonData.getString(JSONKey.BALANCE.getKey()) + player.getAccountBalance());
        panel.add(pLabel);
         if (player.isInJail()) { pLabel = new JLabel(jsonData.getString(JSONKey.PLAYER_IS_IN_JAIL.getKey()));
         panel.add(pLabel); }

        mainPanel.add(panel);
        this.getContentPane().setLayout(null);
        JPanel propPanel = new JPanel();

        for (Space property : game.getOwnedSpaces(player)) {
            propPanel = new JPanel();
            propPanel.setMinimumSize(new Dimension(150, 100));
            propPanel.setPreferredSize(new Dimension(150, 100));
            propPanel.setMaximumSize(new Dimension(150, 100));
            propPanel.setLayout(new BoxLayout(propPanel, BoxLayout.Y_AXIS));
            propPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            propPanel.setOpaque(true);

            if (property instanceof PropertySpace) {
                PropertySpace propertySpace = (PropertySpace) property;
                pLabel = new JLabel(propertySpace.getName());
                propPanel.add(pLabel);
                propPanel.setBackground(propertySpace.getColor());
                pLabel = new JLabel(jsonData.getString(JSONKey.HOUSES_BUILT.getKey())+ propertySpace.getHousesBuilt());
                propPanel.add(pLabel);
                pLabel = new JLabel(jsonData.getString(JSONKey.RENT.getKey()) + propertySpace.getRent(game));
                propPanel.add(pLabel);
            }

            else if (property instanceof StationSpace) {
                StationSpace stationSpace = (StationSpace) property;
                pLabel = new JLabel("" + stationSpace.getName());
                propPanel.add(pLabel);
                propPanel.setBackground(Color.getHSBColor(38, 38, 38));
                pLabel = new JLabel(jsonData.getString(JSONKey.RENT.getKey()) + stationSpace.getRent(game));
                propPanel.add(pLabel);
            }

            mainPanel.add(propPanel);
        }
        frame.revalidate();
        frame.repaint();
    }
}
