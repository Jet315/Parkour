package me.jet315.timedpmparkour.storage;

/**
 * Created by Jet on 26/11/2017.
 */
public class PlayerMapData {

    //Stores map data for the MySQL
    private String mapName;
    private int timesComleted;
    private long bestTime;
    private boolean hasBeenUpdated = false;

    public PlayerMapData(String mapName, int timesComleted, long bestTime){
        this.mapName = mapName;
        this.timesComleted = timesComleted;
        this.bestTime = bestTime;
    }

    public String getMapName() {
        return mapName;
    }

    public int getTimesComleted() {
        return timesComleted;
    }

    public void setTimesCompleted(int timesComleted) {
        this.timesComleted = timesComleted;
    }

    public long getBestTime() {
        return bestTime;
    }

    public void setBestTime(long bestTime) {
        this.bestTime = bestTime;
    }

    public boolean isHasBeenUpdated() {
        return hasBeenUpdated;
    }

    public void setHasBeenUpdated(boolean hasBeenUpdated) {
        this.hasBeenUpdated = hasBeenUpdated;
    }
}
