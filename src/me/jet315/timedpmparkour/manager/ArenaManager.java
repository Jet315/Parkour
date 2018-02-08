package me.jet315.timedpmparkour.manager;

import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.utils.Utils;
import org.bukkit.Location;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jet on 17/11/2017.
 */
public class ArenaManager {
    //Strings that store game ID
    private Map<String, Arena> activeArenas = new HashMap<>();

    //Stores each map difficulty, sorted (in order)
    private HashMap<String,ArrayList<Arena>> maps = new HashMap<>();

    public ArenaManager(){
        loadArenas();

    }

    private void loadArenas() {

        if (Core.getInstance().getConfig().getConfigurationSection("Games") == null) return;
        //If there are games, then loop through
        for (String arenaName : Core.getInstance().getConfig().getConfigurationSection("Games").getKeys(false)) {
            Location spawnLoc = Utils.locationFromString(Core.getInstance().getConfig().getString("Games." + arenaName + ".spawnLocation"));
            Location startPadLocation = Utils.locationFromString(Core.getInstance().getConfig().getString("Games." + arenaName + ".startPadLocation"));
            Location endPadLocation = Utils.locationFromString(Core.getInstance().getConfig().getString("Games." + arenaName + ".endPadLocation"));
            String difficulty = Core.getInstance().getConfig().getString("Games." + arenaName + ".difficulty");
            String permission = Core.getInstance().getConfig().getString("Games." + arenaName + ".permission");

            //Think this line is pointless as if there is an error, it will occur when getting the LocationFromString
            if (spawnLoc == null || startPadLocation == null || endPadLocation == null) return;

            Arena arena = new Arena(arenaName, spawnLoc, startPadLocation, endPadLocation);

            //Should do it like this
            String holoLocation = Core.getInstance().getConfig().getString("Games." + arenaName + ".holoLocation");
            if(holoLocation != null) {
                if (!holoLocation.equalsIgnoreCase("null")) {
                    Location locOfHolo = Utils.locationFromString(holoLocation);
                    arena.setHoloLocation(locOfHolo);
                    arena.setHasHolo(true);

                }
            }
            String statsLocation =  Core.getInstance().getConfig().getString("Games." + arenaName + ".statsSignLocation");


            if (!difficulty.equalsIgnoreCase("null")) {
                arena.setDifficulty(difficulty);
            }
            if (!permission.equalsIgnoreCase("null")) {
                arena.setPermission(permission);
            }

            if (statsLocation.equalsIgnoreCase("null")) {
                arena.setWorldRecordTime(9999999);
            } else {
                arena.setStatsSignLocation(Utils.locationFromString(Core.getInstance().getConfig().getString("Games." + arenaName + ".statsSignLocation")));

            }
            activeArenas.put(arenaName, arena);
        }
        sortMaps();
    }


    public boolean createArena(String gameID){
        //Check if game exists with ID - Not actually used atm as I check before calling this method anyway
        if(activeArenas.containsKey(gameID)){
            return false;
        }
        //Putting defaults into config
        Core.getInstance().getConfig().set("Games." + gameID + ".spawnLocation","null");
        Core.getInstance().getConfig().set("Games." + gameID + ".startPadLocation","null");
        Core.getInstance().getConfig().set("Games." + gameID + ".endPadLocation","null");
        //Save default sign locations
        Core.getInstance().getConfig().set("Games." + gameID + ".statsSignLocation","null");
        Core.getInstance().getConfig().set("Games." + gameID + ".holoLocation","null");
        Core.getInstance().getConfig().set("Games." + gameID + ".difficulty","null");
        Core.getInstance().getConfig().set("Games." + gameID + ".permission","null");


        //Save to config
        Core.getInstance().saveConfig();
        return true;

    }

    public void setSpawnLocation(Location locationOfPlayer, String gameID){
        Core.getInstance().getConfig().set("Games."+ gameID + ".spawnLocation",locationOfPlayer.getWorld().getName()+","+locationOfPlayer.getX() +","+locationOfPlayer.getY()+1+","+locationOfPlayer.getZ() +","+locationOfPlayer.getYaw() +","+locationOfPlayer.getPitch());
        if(activeArenas.containsKey(gameID)){
            activeArenas.get(gameID).setSpawnLocation(locationOfPlayer);
        }
        Core.getInstance().saveConfig();
    }

    public void setStartPadLocation(Location locationOfPlayer, String gameID){
        Core.getInstance().getConfig().set("Games."+ gameID + ".startPadLocation",locationOfPlayer.getWorld().getName()+","+locationOfPlayer.getX() +","+locationOfPlayer.getY()+1+","+locationOfPlayer.getZ() +","+locationOfPlayer.getYaw() +","+locationOfPlayer.getPitch());
        if(activeArenas.containsKey(gameID)){
            activeArenas.get(gameID).setSpawnLocation(locationOfPlayer);
        }
        Core.getInstance().saveConfig();
    }

    public void setEndPadLocation(Location locationOfPlayer, String gameID){
        Core.getInstance().getConfig().set("Games."+ gameID + ".endPadLocation",locationOfPlayer.getWorld().getName()+","+locationOfPlayer.getX() +","+locationOfPlayer.getY()+1+","+locationOfPlayer.getZ() +","+locationOfPlayer.getYaw() +","+locationOfPlayer.getPitch());
        if(activeArenas.containsKey(gameID)){
            activeArenas.get(gameID).setSpawnLocation(locationOfPlayer);
        }
        Core.getInstance().saveConfig();
    }

    public void setDifficulty(String gameID, String difficulty){
        Core.getInstance().getConfig().set("Games." + gameID + ".difficulty",difficulty);
        if(activeArenas.containsKey(gameID)){
            activeArenas.get(gameID).setDifficulty(difficulty);
        }
        Core.getInstance().saveConfig();

    }

    public void setPermission(String permission, String gameID){
        Core.getInstance().getConfig().set("Games." + gameID + ".permission",permission);
        activeArenas.get(gameID).setPermission(permission);

        Core.getInstance().saveConfig();
    }

    public void saveSign(Sign s, String gameID){
        Core.getInstance().getConfig().set("Games." + gameID + ".statsSignLocation",s.getLocation().getWorld().getName() + "," +s.getLocation().getX() +","+s.getLocation().getY()+","+s.getLocation().getZ() +","+s.getLocation().getYaw() +","+s.getLocation().getPitch());
        activeArenas.get(gameID).setStatsSignLocation(s.getLocation());

        Core.getInstance().saveConfig();
    }
    public void deleteSign(String gameID){
        Core.getInstance().getConfig().set("Games." + gameID + ".statsSignLocation","null");
        activeArenas.get(gameID).setHasStatsSign(false);

        Core.getInstance().saveConfig();
    }

    public void setHoloPossition(Location locationOfPlayer, String gameID){
        Core.getInstance().getConfig().set("Games."+ gameID + ".holoLocation",locationOfPlayer.getWorld().getName()+","+locationOfPlayer.getX() +","+locationOfPlayer.getY()+1+","+locationOfPlayer.getZ() +","+locationOfPlayer.getYaw() +","+locationOfPlayer.getPitch());
        if(activeArenas.containsKey(gameID)){
            activeArenas.get(gameID).setHoloLocation(locationOfPlayer);
            activeArenas.get(gameID).setHasHolo(true);
        }
        Core.getInstance().saveConfig();
    }

    public boolean forceLoadArena(String gameID){
        if(activeArenas.containsKey(gameID)) return false;
        Location spawnLoc = Utils.locationFromString(Core.getInstance().getConfig().getString("Games." + gameID + ".spawnLocation"));
        Location startPadLocation = Utils.locationFromString(Core.getInstance().getConfig().getString("Games." + gameID + ".startPadLocation"));
        Location endPadLocation = Utils.locationFromString(Core.getInstance().getConfig().getString("Games." + gameID + ".endPadLocation"));
        String difficulty = Core.getInstance().getConfig().getString("Games." + gameID + ".difficulty");

        if(spawnLoc == null || startPadLocation == null || endPadLocation == null || difficulty.equalsIgnoreCase("null")) return false;

        Arena arena = new Arena(gameID,spawnLoc,startPadLocation,endPadLocation);
        arena.setDifficulty(difficulty);
        activeArenas.put(gameID,arena);
        sortMaps();
        return true;

    }

    public void deleteArena(String gameID){
        if(activeArenas.containsKey(gameID)){
            activeArenas.remove(gameID);
        }
        Core.getInstance().getConfig().set("Games." + gameID,null);
        sortMaps();
        Core.getInstance().saveConfig();
    }



    private void sortMaps() {
        maps.clear();

        //Iterate through each map, if the maps array list contains the maps difficulty, add it to this array - else create a new array for this difficulty
        for (Arena a : activeArenas.values()) {
            if (maps.containsKey(a.getDifficulty())) {
                maps.get(a.getDifficulty()).add(a);
            } else {
                ArrayList<Arena> activeMap = new ArrayList<>();
                activeMap.add(a);
                maps.put(a.getDifficulty(), activeMap);
            }
        }

        //Sorts the maps with my Comparator  - Sorts in order of Roman Numerals - Bit of a quick fix by replacing map 9 with 4
        for (String difficultyOfMap : maps.keySet()) {
            Collections.sort(maps.get(difficultyOfMap), new ArenaComparator());
            if (maps.get(difficultyOfMap).size() >= 9) {
                maps.get(difficultyOfMap).add(9, maps.get(difficultyOfMap).get(4));
                maps.get(difficultyOfMap).remove(4);
            }

        }
        //The above method is a lot simpler, more efficient and cleaner than this: (also allows for an unlimited amount of map difficulties)
/*        Collections.sort(easyMaps, new ArenaComparator());
        if(easyMaps.size() >= 9) {
            easyMaps.add(9, easyMaps.get(4));
            easyMaps.remove(4);
        }
        Collections.sort(mediumMaps, new ArenaComparator());
        if(mediumMaps.size() >= 9) {
            mediumMaps.add(9, mediumMaps.get(4));
            mediumMaps.remove(4);
        }
        Collections.sort(hardMaps, new ArenaComparator());
        if(hardMaps.size() >= 9) {
            hardMaps.add(9, hardMaps.get(4));
            hardMaps.remove(4);
        }

    }*/
    }
    public Map<String, Arena> getArenas(){
        return activeArenas;
    }

    public HashMap<String, ArrayList<Arena>> getMaps() {
        return maps;
    }
}
