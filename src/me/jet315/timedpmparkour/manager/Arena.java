package me.jet315.timedpmparkour.manager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 * Created by Jet on 17/11/2017.
 */
public class Arena {
    //Stores properties of each Arena
    //Name of Arena
    private String id;
    //Difficulty of Arena
    private String difficulty;
    //Stores spawn location of Arena
    private Location spawnLocation;
    //Stores the starting pressure pad location
    private Location startPressurePad;
    //Stores the end pressure pad location
    private Location endPressurePad;
    //Stores the players name of the person who has done the map the queeekest
    private String playerName;
    //Like above, stores time
    private long worldRecordTime = 0;
    //Stores true/false to whether a stats sign is valid
    private boolean hasStatsSign;
    //Stores the stats sign location
    private Location statsSignLocation;
    //Stores the permission required to join the arena
    private String permission;
    //Stores whether arena has a hologram
    private boolean hasHolo = false;
    //Stores the holo location
    private Location holoLocation;

    public Arena(String id, Location spawnLoc, Location startPressurePad, Location endPressurePad){
        this.id = id;
        this.spawnLocation = spawnLoc;
        this.startPressurePad = startPressurePad;
        this.endPressurePad = endPressurePad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public long getWorldRecordTime() {
        return worldRecordTime;
    }

    public void setWorldRecordTime(long worldRecordTime) {
        this.worldRecordTime = worldRecordTime;
    }

    public boolean isHasStatsSign() {
        return hasStatsSign;
    }

    public void setHasStatsSign(boolean hasStatsSign) {
        this.hasStatsSign = hasStatsSign;
    }

    public Location getStatsSignLocation() {
        return statsSignLocation;
    }

    public void setStatsSignLocation(Location statsSignLocation) {
        this.statsSignLocation = statsSignLocation;
        this.hasStatsSign = true;

        //Loading the World Record
        Block b = statsSignLocation.getBlock();
        BlockState blockState = b.getState();
        Sign s = (Sign) blockState;
        String timeWithText = s.getLine(3);
        String split[] = timeWithText.split(" ");
        long time =  (long) (Double.valueOf(split[0].substring(2)) * 1000);
        setWorldRecordTime(time);
    }

    public Location getStartPressurePad() {
        return startPressurePad;
    }

    public void setStartPressurePad(Location startPressurePad) {
        this.startPressurePad = startPressurePad;
    }

    public Location getEndPressurePad() {
        return endPressurePad;
    }

    public void setEndPressurePad(Location endPressurePad) {
        this.endPressurePad = endPressurePad;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public boolean hasPermission() {
        if (permission == null) {
            return false;
        } else if (permission.equalsIgnoreCase("null")) {
            return false;
        } else {
            return true;
        }
    }
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void updateWorldRecordOnSign(Player p, long time){
        worldRecordTime = time;
        if(hasStatsSign){
            BlockState blockState = statsSignLocation.getBlock().getState();
            Sign s = (Sign) blockState;
            s.setLine(2, ChatColor.AQUA + p.getName());
            s.setLine(3,ChatColor.AQUA +""+ (double) time/1000 + " Seconds");
            s.update();
        }
    }

    public boolean isHasHolo() {
        return hasHolo;
    }

    public void setHasHolo(boolean hasHolo) {
        this.hasHolo = hasHolo;
    }

    public Location getHoloLocation() {
        return holoLocation;
    }

    public void setHoloLocation(Location holoLocation) {
        this.holoLocation = holoLocation;
    }
}
