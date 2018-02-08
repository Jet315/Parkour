package me.jet315.timedpmparkour.gui;

import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.manager.Arena;
import me.jet315.timedpmparkour.storage.PlayerMapData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Jet on 05/01/2018.
 */
public class PlayersGUI extends GUI{


    public PlayersGUI(){
        super();
        initializeGUI();
    }


    private void initializeGUI() {
        //Nothing to do atm, possibly take stuff out from the methods bellow and place into fields?
    }


    /**
     *
     * @param p The Player
     *          This will method will open up the first, main, navigation menu for a player

     */
    public void openGUI(Player p) {
        //New Inventory instance
        Inventory mainMenu = super.getMainMenu();

        //Stores ALL the players maps
        ArrayList<PlayerMapData> maps = Core.getInstance().getPlayerData().getPlayerMaps().get(p);

        int easyMaps = 0;
        int mediumMaps = 0;
        if (maps != null) {
            for (PlayerMapData map : maps) {
                if (Core.getInstance().getArenaManager().getArenas().containsKey(map.getMapName())) {
                    //Map is active
                    Arena a = Core.getInstance().getArenaManager().getArenas().get(map.getMapName());
                    if (a.getDifficulty().equalsIgnoreCase("easy")) {
                        easyMaps += 1;
                    } else if (a.getDifficulty().equalsIgnoreCase("medium")) {
                        mediumMaps += 1;
                    }//TODO hard when Mega is released
                }
            }
        }


        /**
         *Medium Courses Item
         **/
        ArrayList<String> lore = new ArrayList<>();
        ItemStack medium = new ItemStack(Material.WOOL, 1, (byte) 4);
        ItemMeta mediumM = medium.getItemMeta();
        mediumM.setDisplayName(ChatColor.BOLD + "" + ChatColor.YELLOW + "MEDIUM WORLD");
        //Will possibly change to a config option (If released to Spigot)
        lore.add(ChatColor.GRAY + "• 12+ Medium Parkour Courses");
        lore.add(ChatColor.GRAY + "Click to View Courses!");
        lore.add(" ");
        if (easyMaps < 10) {
            lore.add(ChatColor.RED + "LOCKED");
            lore.add(ChatColor.GRAY + "✘ Requires 10 Easy Maps");
        } else {
            lore.add(ChatColor.GREEN + "UNLOCKED");
            lore.add(ChatColor.GRAY + "✔ Requires 10 Easy Maps");
            mediumM.addEnchant(Enchantment.SILK_TOUCH, 1, false);
            mediumM.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        }
        mediumM.setLore(lore);
        medium.setItemMeta(mediumM);

        mainMenu.setItem(12, medium);
        lore.clear();

        /**
         *Hard courses Item
         **/
        ItemStack hard = new ItemStack(Material.WOOL, 1, (byte) 14);
        ItemMeta hardM = hard.getItemMeta();
        hardM.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "HARD WORLD");
        //Will possibly change to a config option (If released to Spigot)
        lore.add(ChatColor.GRAY + "• 12+ Hard Parkour Courses");
        lore.add(ChatColor.GRAY + "Click to View Courses!");
        lore.add(" ");
        if (mediumMaps < 10) {
            lore.add(ChatColor.RED + "LOCKED");
            lore.add(ChatColor.GRAY + "✘ Requires 10 Medium Maps");

        } else {
            lore.add(ChatColor.GREEN + "UNLOCKED");
            lore.add(ChatColor.GRAY + "✔ Requires 10 Medium Maps");
            hardM.addEnchant(Enchantment.SILK_TOUCH, 1, false);
            hardM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        hardM.setLore(lore);
        hard.setItemMeta(hardM);
        mainMenu.setItem(14, hard);
        lore.clear();

        /**
        *Mega courses
         **/
        ItemStack mega = new ItemStack(Material.WOOL, 1, (byte) 10);
        ItemMeta megaM = mega.getItemMeta();
        megaM.setDisplayName(ChatColor.BOLD + "" + ChatColor.DARK_PURPLE + "MEGA WORLD");
        //Will possibly change to a config option (If released to Spigot)
        lore.add(ChatColor.GRAY + "COMING TO TIMEMC " + ChatColor.UNDERLINE+ "SOON™");
        lore.add(" ");

        megaM.setLore(lore);
        mega.setItemMeta(megaM);
        mainMenu.setItem(16, mega);
        lore.clear();

        p.openInventory(mainMenu);

    }


    /**
     * This will load the second GUI, the GUI will contain the timedpmparkour courses that link into the difficulty
     * Called from an on click event
     *
     * @param p          the Player
     * @param difficulty The difficulty of courses to be loaded
     */

    public void openGUI(Player p, String difficulty) {

        Inventory secondInv = Bukkit.createInventory(null, 54, ChatColor.GRAY + "" + difficulty.substring(0, 1).toUpperCase() + difficulty.substring(1) + " Courses");
        p.openInventory(secondInv);
        //Stores ALL the players maps
        ArrayList<PlayerMapData> maps = Core.getInstance().getPlayerData().getPlayerMaps().get(p);
        //Stores the players maps that match a certain difficulty
        ArrayList<PlayerMapData> playerMaps = new ArrayList<>();
        //Stores all the active maps that match a certain difficulty
        ArrayList<Arena> allMaps;
        //Stores the number of total times they have completed a difficulty
        int totalCompletions = 0;

        /**Back Button */
        secondInv.setItem(45, super.getBackButton());
        //Getting all the PLAYER maps that match a certain difficulty
        if (maps != null) {
            for (PlayerMapData map : maps) {
                //To get the difficulty, I need to get the map name from the object, get the actual map object and return the difficulty
                String mapName = map.getMapName();
                if (Core.getInstance().getArenaManager().getArenas().containsKey(mapName)) {
                    //Map is active
                    if (Core.getInstance().getArenaManager().getArenas().get(mapName).getDifficulty().equalsIgnoreCase(difficulty)) {
                        //Map contains the same difficulty, so add it to the maps to load
                        playerMaps.add(map);
                        //Add the times completed to the map
                        totalCompletions += map.getTimesComleted();

                    }
                }
            }
        }

        //Put the difficulty of wool in slot 4
        //Lore needs to contain Completed number/number
        //Lore needs to contain the total amount of completed tmes
        String woolName;
        ItemStack wool;
        ArrayList<String> lore = new ArrayList<>();
        if (difficulty.contains("easy")) {
            woolName = ChatColor.GREEN + "Easy Courses";
            wool = new ItemStack(Material.WOOL, 1, (byte) 5);
            allMaps = Core.getInstance().getArenaManager().getMaps().get(difficulty);

        } else if (difficulty.contains("medium")) {
            woolName = ChatColor.YELLOW + "Medium Courses";
            wool = new ItemStack(Material.WOOL, 1, (byte) 4);
            allMaps = Core.getInstance().getArenaManager().getMaps().get(difficulty);

        } else if (difficulty.contains("hard")) {
            woolName = ChatColor.RED + "Hard Courses";
            wool = new ItemStack(Material.WOOL, 1, (byte) 14);
            allMaps = Core.getInstance().getArenaManager().getMaps().get(difficulty);
        } else {
            p.sendMessage(ChatColor.RED + "UNABLE TO LOAD INVENTORY FOR " + difficulty + " - ERROR OCCURRED WHILE LOADING INVENTORY");
            return;
        }


        ItemMeta woolMeta = wool.getItemMeta();
        woolMeta.setDisplayName(woolName);
        lore.add(" ");
        lore.add(ChatColor.GRAY + "Completed: " + ChatColor.AQUA + playerMaps.size() + "/" + allMaps.size());
        lore.add(ChatColor.GRAY + "Total Completions: " + ChatColor.AQUA + totalCompletions);
        woolMeta.setLore(lore);
        wool.setItemMeta(woolMeta);
        secondInv.setItem(4, wool);
        lore.clear();
        //Then red stained glass panels shown if not done map, green if done
        //Green ones have Name, total completeions \ best time \ click to tp - red ones have similar
        //Purple ones are donator maps
        //Getting all the map names relating to that difficulty

        //This variable stores the position at which to place it into the inventory
        int inventorySlot = 8;
        boolean hasCompletedCourse;

        for (Arena map : allMaps) {
            inventorySlot++;
            hasCompletedCourse = false;
            //Check if user has a map
            //Load stats if so (get the mapdataobject)
            //Then check permission, show purple if doesnt have permission
            //if doesnt have and no permission, show red

            for (PlayerMapData playerMap : playerMaps) {
                if (map.getId().equalsIgnoreCase(playerMap.getMapName())) {
                    //Player has completed the map, load stats
                    ItemStack completed = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
                    ItemMeta completedM = completed.getItemMeta();
                    completedM.setDisplayName(ChatColor.GRAY + map.getId());
                    lore.add(" ");
                    lore.add(ChatColor.GREEN + "Map Completions: " + ChatColor.AQUA + playerMap.getTimesComleted());
                    double theirTime = (double) playerMap.getBestTime() / 1000;
                    lore.add(ChatColor.GREEN + "Best Time: " + ChatColor.AQUA + new DecimalFormat("#.##").format(theirTime) + "s");
                    lore.add(" ");
                    lore.add(ChatColor.GRAY + "Click to Teleport!");
                    completedM.setLore(lore);
                    completed.setItemMeta(completedM);
                    secondInv.setItem(inventorySlot, completed);
                    lore.clear();
                    hasCompletedCourse = true;
                    break;
                }
            }
            if (hasCompletedCourse) continue;

            if (map.hasPermission()) {
                if (p.hasPermission(map.getPermission())) {
                    //Show Red tile showning not complete, but can do, maybe put donor map at the bottom
                    //Player has completed the map, load stats
                    ItemStack notCompleted = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
                    ItemMeta notCompletedM = notCompleted.getItemMeta();
                    notCompletedM.setDisplayName(ChatColor.GRAY + map.getId());
                    lore.add(" ");
                    lore.add(ChatColor.GREEN + "Map Completions: " + ChatColor.RED + "✘");
                    lore.add(ChatColor.GREEN + "Best Time: " + ChatColor.RED + "✘");
                    lore.add(ChatColor.GOLD + "Donator Map");
                    lore.add(" ");
                    lore.add(ChatColor.GRAY + "Click to Teleport!");
                    notCompletedM.setLore(lore);
                    notCompleted.setItemMeta(notCompletedM);
                    secondInv.setItem(inventorySlot, notCompleted);
                    lore.clear();

                } else {
                    //Show purple tile showing rank needed to play this map
                    ItemStack rankNeeded = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 10);
                    ItemMeta rankMeta = rankNeeded.getItemMeta();
                    rankMeta.setDisplayName(ChatColor.GRAY + map.getId());
                    lore.add(" ");
                    lore.add(ChatColor.LIGHT_PURPLE + "Purchase a Rank!");
                    lore.add(ChatColor.LIGHT_PURPLE + "To Play this Map!");
                    lore.add(" ");
                    lore.add(ChatColor.GRAY + "Click for more Info!");
                    rankMeta.setLore(lore);
                    rankNeeded.setItemMeta(rankMeta);
                    secondInv.setItem(inventorySlot, rankNeeded);
                    lore.clear();

                }
            } else {
                //Hasn't completed map and no permission, therefor load red stained glass ✘
                //Show Red tile showning not complete, but can do, maybe put donor map at the bottom
                //Player has completed the map, load stats
                ItemStack notCompleted = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
                ItemMeta notCompletedM = notCompleted.getItemMeta();
                notCompletedM.setDisplayName(ChatColor.GRAY + map.getId());
                lore.add(" ");
                lore.add(ChatColor.GREEN + "Map Completions: " + ChatColor.RED + "✘");
                lore.add(ChatColor.GREEN + "Best Time: " + ChatColor.RED + "✘");
                lore.add(" ");
                lore.add(ChatColor.GRAY + "Click to Teleport!");
                notCompletedM.setLore(lore);
                notCompleted.setItemMeta(notCompletedM);
                secondInv.setItem(inventorySlot, notCompleted);
                lore.clear();

            }


        }

    }
}
