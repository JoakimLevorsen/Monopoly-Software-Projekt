package monopoly.view;

import monopoly.model.Game;
import monopoly.model.Player;
import monopoly.model.spaces.PropertySpace;
import monopoly.model.spaces.Space;
import monopoly.model.spaces.StationSpace;
import org.json.JSONObject;
import resources.json.JSONKey;

import javax.swing.*;
import java.awt.*;

/**
 * PlayerPanel: Opretter paneler med information omkring spilleren og dets ejendomme.
 *
 * @author Anders Brandt, s185016
 */

public class PlayerPanel extends JFrame {
    private static final long serialVersionUID = -3176977364745853778L;
    private JSONObject jsonData;
    private Game game;
    private Player player;
    public final JFrame frame = new JFrame();
    private JPanel panel = new JPanel();
    private JPanel mainPanel = new JPanel();

    /**
     * PlayerPanel constructor
     * 
     * @param game Spillet, som panelet skal tilhøre
     * @param player Spilleren, som panelet skal beskrive
     * 
     * @author Anders Brandt, s185016
     */
    public PlayerPanel(Game game, Player player) {
        this.player = player;
        this.game = game;
        this.jsonData = game.getLanguageData();
        frame.setTitle(player.getName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(700, game.getPlayers().indexOf(player) * 200);
        frame.setPreferredSize(new Dimension(730, 150));
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Update: Opdaterer paneler
     * 
     * @author Anders Brandt, s185016
     */
    public void update() {
        panel.removeAll();
        mainPanel.removeAll();

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
        if (player.isInJail()) {
            pLabel = new JLabel(jsonData.getString(JSONKey.PLAYER_IS_IN_JAIL.getKey()));
            panel.add(pLabel);
        }

        mainPanel.add(panel);
        this.getContentPane().setLayout(null);
        JPanel propPanel = new JPanel();

        for (Space property : player.getOwnedProperties(game)) {
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
                pLabel.setForeground(Color.white);
                propPanel.setBackground(propertySpace.getColor());
                pLabel = new JLabel(jsonData.getString(JSONKey.HOUSES_BUILT.getKey()) + propertySpace.getHousesBuilt());
                pLabel.setForeground(Color.white);
                propPanel.add(pLabel);
                pLabel = new JLabel(jsonData.getString(JSONKey.RENT.getKey()) + propertySpace.getRent(game));
                pLabel.setForeground(Color.white);
                propPanel.add(pLabel);
            }

            else if (property instanceof StationSpace) {
                StationSpace stationSpace = (StationSpace) property;
                pLabel = new JLabel("" + stationSpace.getName());
                pLabel.setForeground(Color.white);
                propPanel.add(pLabel);
                propPanel.setBackground(property.getColor());
                pLabel = new JLabel(jsonData.getString(JSONKey.RENT.getKey()) + stationSpace.getRent(game));
                pLabel.setForeground(Color.white);
                propPanel.add(pLabel);
            }

            mainPanel.add(propPanel);
        }
        frame.revalidate();
        frame.repaint();
    }
}
