package me.jet315.timedpmparkour.manager;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Jet on 17/11/2017.
 */
public class PlayerManager {

    //Stores player name and their player object which stores information about the timetrials course/times
    private HashMap<String, ParkourPlayer> players = new HashMap<>();


    //Called when a user steps on a starting pressure plate
    public void parkourJoinEvent(Player p, String arena) {

        if (players.containsKey(p.getName())) {
            players.remove(p.getName());
        }

        ParkourPlayer parkourPlayer = new ParkourPlayer(arena);

        players.put(p.getName(), parkourPlayer);

        p.setGameMode(GameMode.SURVIVAL);


    }

    public void parkourLeaveEvent(Player p) {
        if(players.containsKey(p.getName()))
        players.remove(p.getName());
    }



    public HashMap<String, ParkourPlayer> getPlayers() {
        return players;
    }

}
