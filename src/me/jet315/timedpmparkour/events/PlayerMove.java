package me.jet315.timedpmparkour.events;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.manager.Arena;
import me.jet315.timedpmparkour.manager.ParkourPlayer;
import me.jet315.timedpmparkour.storage.PlayerMapData;
import me.jet315.timedpmparkour.utils.Messages;
import me.jet315.parkouradditional.API;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

/**
 * Created by Jet on 11/12/2017.
 */
public class PlayerMove implements Listener {
    private ArrayList<String> players = new ArrayList<>();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        //Whether they are in a timedpmparkour course
        if (Core.getInstance().getPlayerManager().getPlayers().containsKey(p.getName())) {
            ParkourPlayer parkourPlayer = Core.getInstance().getPlayerManager().getPlayers().get(p.getName());
            Arena arena = Core.getInstance().getArenaManager().getArenas().get(parkourPlayer.getArenaID());

            //Check to see if they have fallen from the course
            if (p.getLocation().getY() <= 85) {
                p.setFallDistance(-999.0F);
                Location loc = API.getPlayerCheckpointLocation(p);
                p.sendMessage(Messages.prefix + ChatColor.GREEN + " Respawned!");
                if(loc != null){
                    p.teleport(loc);
                    return;
                }
                p.teleport(arena.getSpawnLocation());
                parkourPlayer.setActive(false);
                return;
            }
            if (players.contains(e.getPlayer().getName())) return;

            //Check to see if they are at the end of the course
            if (p.getLocation().getBlock().getType() == Material.GOLD_PLATE) {
                if (p.getLocation().getWorld().getName().equalsIgnoreCase(arena.getSpawnLocation().getWorld().getName())) {
                    if (p.getLocation().distance(arena.getEndPressurePad()) <= 6) {
                        API.removePlayersCheckpoint(p);

                        p.setFallDistance(-999.0F);
                        p.teleport(arena.getSpawnLocation());
                        //FINISHED PARKOUR, GET TIME.....
                        //TODO: #############################
                        //Check if game object exists...
                        long playersTime = parkourPlayer.endParkourTime();
                        long worldRecordTime = arena.getWorldRecordTime();

                        if (playersTime == -1 || !parkourPlayer.isActive()) {
                            e.getPlayer().sendMessage(Messages.prefix + ChatColor.GRAY + " You have not started the timedpmparkour!");
                            return;
                        }
                        parkourPlayer.setActive(false);

                        //Core.getInstance().getScoreBoard().updateScoreBoard(p,String.valueOf(0));
/*                        String playerMessage = ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("MessagePrefix")) + ChatColor.GREEN + ChatColor.BOLD + " Your Time Was "
                                + ChatColor.GOLD + (double) playersTime / 1000 + " Seconds ";     */

                        String playerMessage = Messages.prefix + ChatColor.AQUA + " Congratulations! " + ChatColor.GREEN + "Your Time Was "
                                + ChatColor.GOLD + (double) playersTime / 1000 + " Seconds ";
                        if (playersTime - worldRecordTime < 0) {
                            e.getPlayer().sendMessage(playerMessage.concat(ChatColor.RED + "" + ChatColor.BOLD + "This is the " + ChatColor.YELLOW + "" + ChatColor.BOLD + "World Record!"));
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"eco give " + p.getName() + " 500");
                            arena.updateWorldRecordOnSign(p, playersTime);
                        }

                        //get the arenaid and compare if a playerdata file with this exists, if it does check if best times, add one to completion attempts. Else create a playerdatafile
                        for (PlayerMapData d : Core.getInstance().getPlayerData().getPlayerMaps().get(p)) {
                            if (d.getMapName().equalsIgnoreCase(arena.getId())) {
                                d.setHasBeenUpdated(true);
                                //Playerdatafile does exist
                                if (playersTime - d.getBestTime() < 0) {
                                    d.setBestTime(playersTime);
                                    d.setTimesCompleted(d.getTimesComleted() + 1);
                                    updateHologram(p,arena,d);
                                    p.sendMessage((playersTime - worldRecordTime < 0) ? "" : Messages.prefix + ChatColor.GREEN + " This is your fastest time so far! " + ChatColor.GOLD + (double) playersTime / 1000 + " Seconds" + ChatColor.AQUA + " (" + d.getTimesComleted() + " Map Completions)");
                                    rewardPlayer(p,arena.getDifficulty(),d.getTimesComleted());
                                    return;
                                }
                                d.setTimesCompleted(d.getTimesComleted() + 1);
                                updateHologram(p,arena,d);
                                playerMessage = playerMessage + ChatColor.GRAY + "You were " + ChatColor.GREEN + ((double)(playersTime - d.getBestTime()) / 1000) + ChatColor.GRAY + " Seconds off your best!" + ChatColor.AQUA + " (" + d.getTimesComleted() + " Map Completions)";
                                p.sendMessage(playerMessage);
                                rewardPlayer(p,arena.getDifficulty(),d.getTimesComleted());
                                return;
                            }
                        }
                        //Player file must not have existed, as I returned if there is one
                        PlayerMapData newData = new PlayerMapData(arena.getId(), 1, playersTime);
                        newData.setHasBeenUpdated(true);
                        updateHologram(p,arena,newData);
                        p.sendMessage((playersTime - worldRecordTime < 0) ? "" : playerMessage);
                        Core.getInstance().getPlayerData().getPlayerMaps().get(p).add(newData);
                        rewardPlayer(p,arena.getDifficulty(),newData.getTimesComleted());

                    } else {
                        p.setFallDistance(-999.0F);
                        p.teleport(arena.getSpawnLocation());
                        p.sendMessage(Messages.prefix +  ChatColor.RED + " Error - It appears you have not started the map..");
                        Core.getInstance().getPlayerManager().parkourLeaveEvent(p);
                    }
                    return;
                }
            }

            //Check to see if they starting a new course
            if (p.getLocation().getBlock().getType() == Material.WOOD_PLATE) {
                if(p.getLocation().getWorld().getName().equalsIgnoreCase(arena.getSpawnLocation().getWorld().getName())){
                if (p.getLocation().distance(arena.getStartPressurePad()) <= 4) {
                    API.removePlayersCheckpoint(p);
                    parkourPlayer.startParkour(System.currentTimeMillis());
                    p.sendMessage(Messages.prefix +  ChatColor.GREEN + " Started Parkour for map: " + ChatColor.RED + arena.getId());
                    players.add(p.getName());
                    refreshPlayer(p);
                    parkourPlayer.setActive(true);
                    return;
                } else {
                    //Started a different timedpmparkour
                    findParkourStartLocation(p);
                    return;
                }
                }


            }


            //Else not in a game

        } else {
            //Check to see if started a timedpmparkour course
            if (p.getLocation().getBlock().getType() == Material.WOOD_PLATE) {
                //Started a different timedpmparkour
                findParkourStartLocation(p);
                return;
            }


        }


    }


    public void findParkourStartLocation(Player p) {
        //Started a different timedpmparkour
        Location loc = p.getLocation();
        for (Arena a : Core.getInstance().getArenaManager().getArenas().values()) {
            if(!a.getSpawnLocation().getWorld().getName().equalsIgnoreCase(p.getWorld().getName())) continue;
            if (a.getStartPressurePad().distance(loc) <= 4) {
                API.removePlayersCheckpoint(p);
                Core.getInstance().getPlayerManager().parkourJoinEvent(p, a.getId());
                //Will be a new player object
                Core.getInstance().getPlayerManager().getPlayers().get(p.getName()).startParkour(System.currentTimeMillis());
                Core.getInstance().getPlayerManager().getPlayers().get(p.getName()).setActive(true);
                p.sendMessage(Messages.prefix +  ChatColor.GREEN + " Started Parkour for map: " + ChatColor.RED + a.getId());
                players.add(p.getName());
                refreshPlayer(p);

                break;
            }
        }
        return;
    }

    public void refreshPlayer(Player p) {
        new BukkitRunnable() {
            int i = 1;

            public void run() {
                if (i == 0) {
                    players.remove(p.getName());
                    cancel();
                }
                i--;
            }

        }.runTaskTimer(Core.getInstance(), 0L, 20L);
    }

    public void updateHologram(Player p, Arena a, PlayerMapData map){
        if(!Core.getInstance().getPlayerData().getPlayersHolos().containsKey(p)) return;
        for(Hologram h : Core.getInstance().getPlayerData().getPlayersHolos().get(p)){
            if(h.getLocation().distance(a.getHoloLocation()) <= 4){
                h.delete();
                //Create a new hologram
                Hologram hologram = HologramsAPI.createHologram(Core.getInstance(),a.getHoloLocation());
                VisibilityManager visiblityManager = hologram.getVisibilityManager();
                visiblityManager.showTo(p);
                visiblityManager.setVisibleByDefault(false);
                if(a.getDifficulty().equalsIgnoreCase("easy")){
                    hologram.appendItemLine(new ItemStack(Material.STAINED_GLASS,1,(byte) 5));
                }else if(a.getDifficulty().equalsIgnoreCase("medium")){
                    hologram.appendItemLine(new ItemStack(Material.STAINED_GLASS,1,(byte) 1));
                }else if(a.getDifficulty().equalsIgnoreCase("hard")){
                    hologram.appendItemLine(new ItemStack(Material.STAINED_GLASS,1,(byte) 14));
                }else{
                    hologram.appendItemLine(new ItemStack(Material.GRASS));
                }
                hologram.appendTextLine(Messages.prefix);
                hologram.appendTextLine(ChatColor.RED + "Map: " + ChatColor.YELLOW + a.getId());
                if(map != null){
                    hologram.appendTextLine(ChatColor.GRAY + "Times Completed: " + ChatColor.AQUA + map.getTimesComleted());
                    hologram.appendTextLine(ChatColor.GRAY + "Personal Best Time: " + ChatColor.AQUA + (double)map.getBestTime()/1000);
                }else{
                    hologram.appendTextLine(ChatColor.GRAY + "Times Completed: " + ChatColor.RED + "✘");
                    hologram.appendTextLine(ChatColor.GRAY + "Personal Best Time: " + ChatColor.RED + "✘");
                }

                Core.getInstance().getPlayerData().getPlayersHolos().get(p).add(hologram);
                Core.getInstance().getPlayerData().getPlayersHolos().get(p).remove(h);
                return;
            }
        }

    }
    //Called when a player has completed a map
/*
    x5 for completing for first time
    Easy: 10
    Medium: 25
    Hard: 50
*/

    public void rewardPlayer(Player p,String difficulty,int timesCompletedCourse){
        switch (difficulty){
            case "easy":
                if(timesCompletedCourse == 1){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"eco give " + p.getName() + " 50");
                }else{
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"eco give " + p.getName() + " 10");
                }
                return;
            case "medium":
                if(timesCompletedCourse == 1){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"eco give " + p.getName() + " 125");
                }else{
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"eco give " + p.getName() + " 25");
                }
                return;
            case "hard":
                if(timesCompletedCourse == 1){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"eco give " + p.getName() + " 250");
                }else{
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"eco give " + p.getName() + " 50");
                }
                return;
            case "mega":
                if(timesCompletedCourse == 1){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"eco give " + p.getName() + " 350");
                }else{
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"eco give " + p.getName() + " 75");
                }
                return;
            default:
                return;
        }
    }
}