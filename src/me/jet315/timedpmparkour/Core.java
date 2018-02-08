package me.jet315.timedpmparkour;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.zaxxer.hikari.HikariDataSource;
import me.jet315.timedpmparkour.commands.CommandHandler;
import me.jet315.timedpmparkour.events.*;
import me.jet315.timedpmparkour.gui.PlayersGUI;
import me.jet315.timedpmparkour.manager.ArenaManager;
import me.jet315.timedpmparkour.manager.ParkourPlayer;
import me.jet315.timedpmparkour.manager.PlayerManager;
import me.jet315.timedpmparkour.storage.MySQLManager;
import me.jet315.timedpmparkour.storage.PlayerData;
import me.jet315.timedpmparkour.storage.PlayerMapData;
import me.jet315.timedpmparkour.utils.Clock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import be.maximvdw.placeholderapi.PlaceholderAPI; // The main API
import be.maximvdw.placeholderapi.PlaceholderReplacer; // The replacer interface
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent; // The replacer event

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * Created by Jet on 17/11/2017.
 */
public class Core extends JavaPlugin {

    private static Core instance;
    private PlayerManager playerManager;
    private PlayerData playerData;
    private ArenaManager arenaManager;
    private MySQLManager mysql;
    private HikariDataSource hikari;
    private PlayersGUI playerGUI;
    private Clock clock;
    private boolean useHolographicDisplays;

    private ItemStack menuItem = new ItemStack(Material.COMPASS);

    public void onEnable() {
        instance = this;
        hikari = new HikariDataSource();
        mysql = new MySQLManager();
        this.playerManager = new PlayerManager();
        this.arenaManager = new ArenaManager();
        this.playerData = new PlayerData();
        this.clock = new Clock();
        this.playerGUI = new PlayersGUI();

        createConfig();
        connectToDataBase();
        createTable();
        getServer().getPluginManager().registerEvents(new SignEvents(),this);
        getServer().getPluginManager().registerEvents(new PlayerMove(),this);
        getServer().getPluginManager().registerEvents(new LeaveEvent(),this);
        getServer().getPluginManager().registerEvents(new JoinEvent(),this);
        getServer().getPluginManager().registerEvents(new InventoryClick(),this);
        getServer().getPluginManager().registerEvents(new ClickEvent(),this);
        getServer().getPluginManager().registerEvents(new WorldChangeEvent(),this);
        getServer().getPluginManager().registerEvents(new OnDeath(),this);
        getServer().getPluginManager().registerEvents(new PlayerDrop(),this);
        getCommand("pk").setExecutor(new CommandHandler());
        getCommand("stats").setExecutor(new CommandHandler());

        createConfigs();
        loadItemMeta();
        useHolographicDisplays = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays") && Bukkit.getPluginManager().isPluginEnabled("ProtocolLib");

        if(Bukkit.getPluginManager().isPluginEnabled("ActionBarAPI")){
            clock.startClock();
        }

        if(Bukkit.getPluginManager().isPluginEnabled("FeatherBoard")){
            registerPlaceHolders();
        }

        //Just to check
        // Find the holograms created by your plugin
        if(useHolographicDisplays) {
            for (Hologram hologram : HologramsAPI.getHolograms(this)) {
                getLogger().severe("HOLOGRAM DELETED IN WORLD (Did the server crash?)" + hologram.getLocation());
                hologram.delete();
            }
        }

        //Loads the current players online
        for(Player p : Bukkit.getOnlinePlayers()){
            Core.getInstance().getMySQLManager().loadPlayerData(p);
            p.getInventory().setItem(0,Core.getInstance().getMenuItem());
        }


    }

    public void onDisable() {

        this.saveConfig();
        for(Player p : Bukkit.getOnlinePlayers()){
            ArrayList<PlayerMapData> allMaps = Core.getInstance().getPlayerData().getPlayerMaps().get(p);
            ArrayList<PlayerMapData> mapsToSave = new ArrayList<>();

            for(PlayerMapData map : allMaps){
                if(map.isHasBeenUpdated()){
                    mapsToSave.add(map);
                }
            }
            Core.getInstance().getMySQLManager().forceSavePlayerMaps(p.getUniqueId(),mapsToSave);
        }
        clock = null;
        if (hikari != null)
            hikari.close();
        hikari = null;
        mysql = null;
        this.playerManager = null;
        this.arenaManager = null;
        createConfig();
        instance = null;
    }

    private void connectToDataBase() {
        String ip = getConfig().getString("database.mySQLHost");
        String port = getConfig().getString("database.mySQLPort");
        String database = getConfig().getString("database.mySQLDatabase");
        String username = getConfig().getString("database.mySQLUser");
        String password = getConfig().getString("database.mySQLPassword");

        hikari.setJdbcUrl("jdbc:mysql://"+ip+":"+port+"/"+database);

        hikari.addDataSourceProperty("user", username);
        hikari.addDataSourceProperty("password", password);

    }

    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Config.yml not found, creating!");
                saveDefaultConfig();
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();


        }
    }
    private void createTable() {
        try (Connection connection = hikari.getConnection();
             Statement statement = connection.createStatement()) {

            //Maybe change the TEXT datatype
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS parkourstats(unique_id CHAR(36), map VARCHAR(20), times_completed INTEGER, best_time FLOAT)");
            //statement.execute("ALTER TABLE parkourstats ADD PRIMARY KEY(unique_id, map)");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            //Bukkit.getPluginManager().disablePlugin(this);
            System.out.println("Error connecting to MySQL DB - Plugin disabled");
        }

    }
    public void loadItemMeta(){
        ItemMeta menuMeta = menuItem.getItemMeta();
        menuMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.AQUA + "Main Menu");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Use the Menu Selector");
        lore.add(ChatColor.GRAY + "To quickly Teleport to");
        lore.add(ChatColor.GRAY + "Any Parkour course and view stats!");
        menuMeta.setLore(lore);
        menuItem.setItemMeta(menuMeta);
    }

    private void registerPlaceHolders(){
        PlaceholderAPI.registerPlaceholder(this, "currentmap",
                new PlaceholderReplacer() {

                    @Override
                    public String onPlaceholderReplace(
                            PlaceholderReplaceEvent e) {

                        if(playerManager.getPlayers().containsKey(e.getPlayer().getName())){
                            ParkourPlayer parkourPlayer = playerManager.getPlayers().get(e.getPlayer().getName());
                            return  parkourPlayer == null ? "✘" : parkourPlayer.getArenaID();
                        }else{
                            return "✘";
                        }
                    }
                });
                    PlaceholderAPI.registerPlaceholder(this, "timescompleted",
                            new PlaceholderReplacer() {

                        @Override
                        public String onPlaceholderReplace(
                                PlaceholderReplaceEvent e) {
                        if(playerManager.getPlayers().containsKey(e.getPlayer().getName())){
                            ParkourPlayer parkourPlayer = playerManager.getPlayers().get(e.getPlayer().getName());
                            if(parkourPlayer.getArenaID() != null){
                                for(PlayerMapData playerMapData : getPlayerData().getPlayerMaps().get(e.getPlayer())){
                                    if(playerMapData.getMapName().equalsIgnoreCase(parkourPlayer.getArenaID())){
                                        return String.valueOf(playerMapData.getTimesComleted());
                                    }
                                }
                            }
                            return  "✘";
                        }else{
                            return "✘";
                        }
                    }
                });

                    PlaceholderAPI.registerPlaceholder(this, "besttime",
                            new PlaceholderReplacer() {

                        @Override
                        public String onPlaceholderReplace(
                                PlaceholderReplaceEvent e) {
                        if(playerManager.getPlayers().containsKey(e.getPlayer().getName())){
                            ParkourPlayer parkourPlayer = playerManager.getPlayers().get(e.getPlayer().getName());
                            if(parkourPlayer.getArenaID() != null){
                                for(PlayerMapData playerMapData : getPlayerData().getPlayerMaps().get(e.getPlayer())){
                                    if(playerMapData.getMapName().equalsIgnoreCase(parkourPlayer.getArenaID())){
                                        return String.valueOf((double)playerMapData.getBestTime()/1000);
                                    }
                                }
                            }
                            return  "✘";
                        }else{
                            return "✘";
                        }
                    }
                });

        PlaceholderAPI.registerPlaceholder(this, "worldrecord",
                new PlaceholderReplacer() {

                    @Override
                    public String onPlaceholderReplace(
                            PlaceholderReplaceEvent e) {
                        if(playerManager.getPlayers().containsKey(e.getPlayer().getName())){
                            ParkourPlayer parkourPlayer = playerManager.getPlayers().get(e.getPlayer().getName());
                            if(parkourPlayer.getArenaID() != null){
                                return String.valueOf((double) Core.getInstance().getArenaManager().getArenas().get(parkourPlayer.getArenaID()).getWorldRecordTime()/1000);
                            }
                            return  "✘";
                        }else{
                            return "✘";
                        }
                    }
                });
    }


    public static Core getInstance() {
        return instance;
    }

    public HikariDataSource getHikari(){
        return hikari;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public MySQLManager getMySQLManager() {
        return mysql;
    }
    //Todo
    //Interact item to go to hub

    //Store arenas - Best time in arena file

    // Store player data as UUIDs? Inside have a LastTimePlayed, then the map name followed by a time
    private void createConfigs() {
        saveDefaultConfig();
    }

    public ItemStack getMenuItem() {
        return menuItem;
    }

    public boolean isUseHolographicDisplays() {
        return useHolographicDisplays;
    }

    public PlayersGUI getPlayerGUI() {
        return playerGUI;
    }
}

