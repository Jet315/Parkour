package me.jet315.timedpmparkour.storage;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jet on 13/12/2017.
 */
public class PlayerData {

    //Stores the data objects in cashe for each player
    private HashMap<Player,ArrayList<PlayerMapData>> playerMaps = new HashMap<>();
    //Stores the Holograms for each player
    private HashMap<Player,ArrayList<Hologram>> playersHolos = new HashMap<>();


    public HashMap<Player, ArrayList<PlayerMapData>> getPlayerMaps() {
        return playerMaps;
    }

    public HashMap<Player, ArrayList<Hologram>> getPlayersHolos() {
        return playersHolos;
    }
}
