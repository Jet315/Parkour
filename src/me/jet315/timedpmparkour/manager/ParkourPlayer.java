package me.jet315.timedpmparkour.manager;

import org.bukkit.Location;

/**
 * Created by Jet on 17/11/2017.
 */
public class ParkourPlayer {

    /**
     * Stores the time that they activated the pressure plate (Start time)
     */
    private long startTimeOfPressurePlate = 0;
    /**
     * Stores the arenaid that the ParkourPlayer is in
     */
    private String arenaID;
    /*
    * Stores whether player is activly running
    */
    private boolean isActive = false;

    //Stores there checkpoint location
    private Location checkpointLocation = null;
    //Stores the amount of checkpoints they are allowed
    private int allowedCheckpoints = 0;
    //Stores the arenas that they have completed

    public ParkourPlayer(String arenaID){
        this.arenaID = arenaID;
    }

    public long getStartTimeOfPressurePlate() {
        return startTimeOfPressurePlate;
    }

    //Called when a user steps on a pressure plate
    public void startParkour(long currentMili) {
        this.startTimeOfPressurePlate = currentMili;
    }

    public long endParkourTime(){
        if(startTimeOfPressurePlate == 0){
            return -1;
        }
        return System.currentTimeMillis() - startTimeOfPressurePlate;
    }

    public String getArenaID() {
        return arenaID;
    }

    public void setArenaID(String arenaID) {
        this.arenaID = arenaID;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Location getCheckpointLocation() {
        return checkpointLocation;
    }

    public void setCheckpointLocation(Location checkpointLocation) {
        this.checkpointLocation = checkpointLocation;
    }

    public int getAllowedCheckpoints() {
        return allowedCheckpoints;
    }

    public void setAllowedCheckpoints(int allowedCheckpoints) {
        this.allowedCheckpoints = allowedCheckpoints;
    }
}
