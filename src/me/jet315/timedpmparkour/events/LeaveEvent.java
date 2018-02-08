package me.jet315.timedpmparkour.events;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.storage.PlayerMapData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

/**
 * Created by Jet on 13/12/2017.
 */
public class LeaveEvent implements Listener{

    @EventHandler
    public void onLeave(PlayerQuitEvent e){

        ArrayList<PlayerMapData> allMaps = Core.getInstance().getPlayerData().getPlayerMaps().get(e.getPlayer());
        ArrayList<PlayerMapData> mapsToSave = new ArrayList<>();
        if(allMaps != null) {
            for (PlayerMapData map : allMaps) {
                if (map.isHasBeenUpdated()) {
                    mapsToSave.add(map);
                }
            }
            Core.getInstance().getMySQLManager().savePlayerMap(e.getPlayer().getUniqueId(),mapsToSave);
        }

        Core.getInstance().getPlayerData().getPlayerMaps().remove(e.getPlayer());
        Core.getInstance().getPlayerManager().getPlayers().remove(e.getPlayer().getName());

        //Delete the users holograms
        if(Core.getInstance().getPlayerData().getPlayersHolos().containsKey(e.getPlayer())) {
            ArrayList<Hologram> playersHolos = Core.getInstance().getPlayerData().getPlayersHolos().get(e.getPlayer());
            for (Hologram holo : playersHolos) {
                holo.delete();
            }
            Core.getInstance().getPlayerData().getPlayersHolos().remove(e.getPlayer());
        }



    }
}
