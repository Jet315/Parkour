package me.jet315.timedpmparkour.storage;

import me.jet315.timedpmparkour.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Jet on 17/11/2017.
 */
public class MySQLManager {


    private static final String SELECTPLAYERSDATA = "SELECT map, times_completed, best_time FROM parkourstats WHERE unique_id=?";
    private static final String SAVEPLAYERSDATA = "INSERT INTO parkourstats VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE best_time=?,times_completed=?";

    private static final String DELETEMAPDATA = "DELETE from parkourstats WHERE map=?";


    //Works and will return all map courses as objects (PlayerMapData)
    public void loadPlayerData(final Player p) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                try{
                    Connection connection = Core.getInstance().getHikari().getConnection();
                    PreparedStatement selectlayerData = connection.prepareStatement(SELECTPLAYERSDATA);
                    selectlayerData.setString(1, p.getUniqueId().toString());

                    ResultSet result = selectlayerData.executeQuery();
                    ArrayList<PlayerMapData> playerData = new ArrayList<>();

                    while (result.next()) {
                        String mapName = result.getString("map");
                        int timesCompleted = result.getInt("times_completed");
                        long bestTime = result.getInt("best_time");

                        PlayerMapData map = new PlayerMapData(mapName, timesCompleted, bestTime);
                        playerData.add(map);
                    }
                    connection.close();
                    Core.getInstance().getPlayerData().getPlayerMaps().put(p,playerData);
                }

                catch(SQLException e){
                    e.printStackTrace();
                }
            }


        });
    }






/*    //Works and will return all map courses as objects (PlayerMapData) *****NOT THREAD SAFE
    public ArrayList<PlayerMapData> loadPlayerData(final UUID playerUUID){
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<ArrayList<PlayerMapData>> future = executor.submit(new Callable<ArrayList<PlayerMapData>>() {

            @Override
            public ArrayList<PlayerMapData> call() throws Exception {
                try{
                    Connection connection = Core.getInstance().getHikari().getConnection();
                    PreparedStatement selectlayerData = connection.prepareStatement(SELECTPLAYERSDATA);
                    selectlayerData.setString(1, playerUUID.toString());

                    ResultSet result = selectlayerData.executeQuery();
                    ArrayList<PlayerMapData> playerData = new ArrayList<>();

                    while (result.next()) {
                        String mapName = result.getString("map");
                        int timesCompleted = result.getInt("times_completed");
                        long bestTime = result.getInt("best_time");

                        PlayerMapData map = new PlayerMapData(mapName,timesCompleted,bestTime);
                        playerData.add(map);
                    }
                    connection.close();
                    return playerData;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }

        });
        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    //set the best time they have completed the course in
    public void savePlayerMap(final UUID playerUUID, ArrayList<PlayerMapData> maps){
        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection = Core.getInstance().getHikari().getConnection();
                    PreparedStatement savemap = connection.prepareStatement(SAVEPLAYERSDATA);
                    savemap.setString(1,playerUUID.toString());

                    for(PlayerMapData map : maps){
                        if(!map.isHasBeenUpdated()) return; // No need to update a map that has not been updated
                        savemap.setString(2,map.getMapName());
                        savemap.setInt(3,map.getTimesComleted());
                        savemap.setLong(4,map.getBestTime());
                        savemap.setLong(5,map.getBestTime());
                        savemap.setInt(6,map.getTimesComleted());
                        savemap.execute();

                    }
                    connection.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //In the case of a server stop, tasks cannot be run Asynchronously therefor:
    //set the best time they have completed the course in
    public void forceSavePlayerMaps(final UUID playerUUID, ArrayList<PlayerMapData> maps){
                try {
                    Connection connection = Core.getInstance().getHikari().getConnection();
                    PreparedStatement savemap = connection.prepareStatement(SAVEPLAYERSDATA);
                    savemap.setString(1,playerUUID.toString());

                    for(PlayerMapData map : maps){
                        if(!map.isHasBeenUpdated()) return; // No need to update a map that has not been updated
                        savemap.setString(2,map.getMapName());
                        savemap.setInt(3,map.getTimesComleted());
                        savemap.setLong(4,map.getBestTime());
                        savemap.setLong(5,map.getBestTime());
                        savemap.setInt(6,map.getTimesComleted());
                        savemap.execute();

                    }
                    connection.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

    }

    //Deletes data for a particular map
    public void deleteMap(String mapName){
        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection = Core.getInstance().getHikari().getConnection();
                    PreparedStatement deleteMap = connection.prepareStatement(DELETEMAPDATA);
                    deleteMap.setString(1,mapName);
                    deleteMap.execute();
                    connection.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
