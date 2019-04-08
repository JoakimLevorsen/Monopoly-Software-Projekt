package monopoly.view;

import java.util.HashMap;

import designpatterns.*;
import monopoly.model.Player;
import monopoly.model.spaces.*;
import gui_fields.*;

public class View implements Observer {
    HashMap<Space, GUI_Field> spaceToField = new HashMap<Space, GUI_Field>();
    HashMap<Player, GUI_Player> playerToGUIPlayer = new HashMap<Player, GUI_Player>();

    public void update(Subject subject) {
        //TODO    
    }

    public void update(Player player) {
        //TODO
    }
}