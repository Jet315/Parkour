package me.jet315.timedpmparkour.events;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.manager.Arena;
import me.jet315.timedpmparkour.storage.PlayerMapData;
import me.jet315.timedpmparkour.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Created by Jet on 17/12/2017.
 */
public class WorldChangeEvent implements Listener{

    private ItemStack easyIcon = new ItemStack(Material.STAINED_GLASS,1,(byte) 5);
    private ItemStack mediumIcon = new ItemStack(Material.STAINED_GLASS,1,(byte) 1);
    private ItemStack hardIcon = new ItemStack(Material.STAINED_GLASS,1,(byte) 14);


    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e){

        Core.getInstance().getPlayerManager().parkourLeaveEvent(e.getPlayer());
        if(!Core.getInstance().isUseHolographicDisplays()) return;

        //Stores the arenas in the world
        ArrayList<Arena> maps;
        //Stores the Itemstack top put on the holograms
        ItemStack mapHolo;
        /**
         * Plugin plugin = ... // Your plugin's instance
         * Location where = ... // A Location object
         * Hologram hologram = HologramsAPI.createHologram(plugin, where);
         * textLine = hologram.appendTextLine("A hologram line");
         */

        if(e.getPlayer().getWorld().getName().equalsIgnoreCase("easy")){
            maps = loadArenaDifficulty("easy");
            mapHolo = easyIcon;
        }else if(e.getPlayer().getWorld().getName().equalsIgnoreCase("medium")){
            maps = loadArenaDifficulty("medium");
            mapHolo = mediumIcon;
        }else if(e.getPlayer().getWorld().getName().equalsIgnoreCase("hard")){
            maps = loadArenaDifficulty("hard");
            mapHolo = hardIcon;
        }else{
            return;
        }

        if(maps == null) return;
        if(maps.size() == 0) return;

        //Delete current holograms
        if(Core.getInstance().getPlayerData().getPlayersHolos().containsKey(e.getPlayer())){
/*            ArrayList<Hologram> playersHolos = Core.getInstance().getPlayerData().getPlayersHolos().get(e.getPlayer());
            for (Hologram holo : playersHolos) {
                holo.delete();
            }*/
            Core.getInstance().getPlayerData().getPlayersHolos().remove(e.getPlayer());
        }

        ArrayList<PlayerMapData> playerMaps;
        playerMaps = Core.getInstance().getPlayerData().getPlayerMaps().get(e.getPlayer());


        //Stores players holograms
        ArrayList<Hologram> playerHolograms = new ArrayList<>();



        for(Arena a : maps){
            if(!a.isHasHolo()) continue;
            if(a.hasPermission()){
                if(!e.getPlayer().hasPermission(a.getPermission())) continue;
            }

            PlayerMapData map = null;

            for(PlayerMapData playerMap : playerMaps){
                if(playerMap.getMapName().equalsIgnoreCase(a.getId())){
                    map = playerMap;
                    break;
                }
            }

            Hologram hologram = HologramsAPI.createHologram(Core.getInstance(),a.getHoloLocation());
            VisibilityManager visiblityManager = hologram.getVisibilityManager();
            visiblityManager.showTo(e.getPlayer());
            visiblityManager.setVisibleByDefault(false);

            //Can't be null as would have returned before if mapHolo is null
            hologram.appendItemLine(mapHolo);

            hologram.appendTextLine(Messages.prefix);
            hologram.appendTextLine(ChatColor.RED + "Map: " + ChatColor.YELLOW + a.getId());
            if(map != null){
                hologram.appendTextLine(ChatColor.GRAY + "Times Completed: " + ChatColor.AQUA + map.getTimesComleted());
                hologram.appendTextLine(ChatColor.GRAY + "Personal Best Time: " + ChatColor.AQUA + (double)map.getBestTime()/1000);
            }else{
                hologram.appendTextLine(ChatColor.GRAY + "Times Completed: " + ChatColor.RED + "✘");
                hologram.appendTextLine(ChatColor.GRAY + "Personal Best Time: " + ChatColor.RED + "✘");
            }

            playerHolograms.add(hologram);
        }
        Core.getInstance().getPlayerData().getPlayersHolos().put(e.getPlayer(),playerHolograms);

    }



    private ArrayList<Arena> loadArenaDifficulty(String difficulty){
        return Core.getInstance().getArenaManager().getMaps().get(difficulty);

/*        ArrayList<Arena> allMaps = new ArrayList<>();
        for (Arena a : Core.getInstance().getArenaManager().getArenas().values()) {
            if (a.getDifficulty().equalsIgnoreCase(difficulty)) {
                allMaps.add(a);
            }
        }
        return allMaps;*/
    }
}
