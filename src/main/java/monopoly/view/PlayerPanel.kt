package monopoly.view

import monopoly.model.Game
import monopoly.model.Player
import monopoly.model.spaces.PropertySpace
import monopoly.model.spaces.Space
import monopoly.model.spaces.StationSpace
import org.json.JSONObject
import resources.json.JSONKey

import javax.swing.*
import java.awt.*

/**
 * PlayerPanel: Opretter paneler med information omkring spilleren og dets ejendomme.
 *
 * @author Anders Brandt, s185016
 */

class PlayerPanel
/**
 * PlayerPanel constructor
 *
 * @param game Spillet, som panelet skal tilhøre
 * @param player Spilleren, som panelet skal beskrive
 *
 * @author Anders Brandt, s185016
 */
(private val game: Game, private val player: Player) : JFrame() {
    private val jsonData: JSONObject
    val frame = JFrame()
    private val panel = JPanel()
    private val mainPanel = JPanel()

    init {
        this.jsonData = game.languageData
        frame.title = player.name
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setLocation(700, game.getPlayers().indexOf(player) * 200)
        frame.preferredSize = Dimension(730, 150)
        frame.add(mainPanel)
        frame.pack()
        frame.isVisible = true
    }

    /**
     * Update: Opdaterer paneler
     *
     * @author Anders Brandt, s185016
     */
    fun update() {
        panel.removeAll()
        mainPanel.removeAll()

        panel.minimumSize = Dimension(100, 100)
        panel.maximumSize = Dimension(100, 100)
        panel.border = BorderFactory.createLineBorder(Color.black)
        panel.isOpaque = true
        panel.background = player.color
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        mainPanel.layout = BoxLayout(mainPanel, BoxLayout.X_AXIS)

        var pLabel = JLabel(jsonData.getString(JSONKey.NAME.key) + player.name)
        panel.add(pLabel)

        pLabel = JLabel(jsonData.getString(JSONKey.BALANCE.key) + player.accountBalance)
        panel.add(pLabel)
        if (player.isInJail) {
            pLabel = JLabel(jsonData.getString(JSONKey.PLAYER_IS_IN_JAIL.key))
            panel.add(pLabel)
        }

        mainPanel.add(panel)
        this.contentPane.layout = null
        var propPanel = JPanel()

        for (property in player.getOwnedProperties(game)) {
            propPanel = JPanel()
            propPanel.minimumSize = Dimension(150, 100)
            propPanel.preferredSize = Dimension(150, 100)
            propPanel.maximumSize = Dimension(150, 100)
            propPanel.layout = BoxLayout(propPanel, BoxLayout.Y_AXIS)
            propPanel.border = BorderFactory.createLineBorder(Color.black)
            propPanel.isOpaque = true

            if (property is PropertySpace) {
                pLabel = JLabel(property.name)
                propPanel.add(pLabel)
                pLabel.foreground = Color.white
                propPanel.background = property.color
                pLabel = JLabel(jsonData.getString(JSONKey.HOUSES_BUILT.key) + property.housesBuilt)
                pLabel.foreground = Color.white
                propPanel.add(pLabel)
                pLabel = JLabel(jsonData.getString(JSONKey.RENT.key) + property.getRent(game))
                pLabel.foreground = Color.white
                propPanel.add(pLabel)
            } else if (property is StationSpace) {
                pLabel = JLabel("" + property.name)
                pLabel.foreground = Color.white
                propPanel.add(pLabel)
                propPanel.background = property.color
                pLabel = JLabel(jsonData.getString(JSONKey.RENT.key) + property.getRent(game))
                pLabel.foreground = Color.white
                propPanel.add(pLabel)
            }

            mainPanel.add(propPanel)
        }
        frame.revalidate()
        frame.repaint()
    }

    companion object {
        private val serialVersionUID = -3176977364745853778L
    }
}
