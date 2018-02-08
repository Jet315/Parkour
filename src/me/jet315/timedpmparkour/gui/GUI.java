package me.jet315.timedpmparkour.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by Jet on 05/01/2018.
 *
 * Used this class to store key components that may be used in other GUIs
 */
public abstract class GUI {

    //Main Menu
    private Inventory mainMenu = Bukkit.createInventory(null, 54, ChatColor.AQUA + "Main Menu");

    //Back Item
    //Stores the return to main menu icon
    private ItemStack backButton = new ItemStack(Material.BOOK);

    public GUI(){
        initializeGUI();
    }

    private void initializeGUI(){

        ArrayList<String> lore = new ArrayList<>();
        /**
         * Following code loads the items around the navigation GUI
         */
        ItemStack greenPanel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 13);
        ItemMeta greenMeta = greenPanel.getItemMeta();
        greenMeta.setDisplayName(ChatColor.DARK_GRAY + "");
        greenPanel.setItemMeta(greenMeta);

        ItemStack yellowPanel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 4);
        ItemMeta yellowMeta = yellowPanel.getItemMeta();
        yellowMeta.setDisplayName(ChatColor.DARK_GRAY + "");
        yellowPanel.setItemMeta(yellowMeta);

        ItemStack redPanel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta redMeta = redPanel.getItemMeta();
        redMeta.setDisplayName(ChatColor.DARK_GRAY + "");
        redPanel.setItemMeta(redMeta);

        ItemStack bluePanel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);
        ItemMeta blueMeta = bluePanel.getItemMeta();
        blueMeta.setDisplayName(ChatColor.DARK_GRAY + "");
        bluePanel.setItemMeta(blueMeta);

        ItemStack purplePanel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 10);
        ItemMeta purpleMeta = purplePanel.getItemMeta();
        purpleMeta.setDisplayName(ChatColor.DARK_GRAY + "");
        purplePanel.setItemMeta(purpleMeta);

        ItemStack cosmeticItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta cosmeticM = cosmeticItem.getItemMeta();
        cosmeticM.setDisplayName(ChatColor.BOLD + "" + ChatColor.AQUA + "Cosmetics");
        lore.add(ChatColor.GRAY + "• Activate Cool Cosmetics!");
        lore.add(ChatColor.GRAY + "Open Cosmetics at /Spawn!");
        cosmeticM.setLore(lore);
        cosmeticItem.setItemMeta(cosmeticM);
        lore.clear();

        mainMenu.setItem(1, greenPanel);
        mainMenu.setItem(3, yellowPanel);
        mainMenu.setItem(5, redPanel);
        mainMenu.setItem(7, purplePanel);

        mainMenu.setItem(19, greenPanel);
        mainMenu.setItem(21, yellowPanel);
        mainMenu.setItem(23, redPanel);
        mainMenu.setItem(25, purplePanel);

        mainMenu.setItem(40, cosmeticItem);

        for (int i = 27; i <= 35; i++) {
            mainMenu.setItem(i, bluePanel);
        }

        //TODO Hard coded, need to recode so it will create the inventory based from difficulties - Load from Config
        ItemStack easy = new ItemStack(Material.WOOL, 1, (byte) 5);
        ItemMeta easyM = easy.getItemMeta();
        easyM.setDisplayName(ChatColor.BOLD + "" + ChatColor.GREEN + "EASY WORLD");
        //Will possibly change to a config option (If released to Spigot)
        lore.add(ChatColor.GRAY + "• 12+ Easy Parkour Courses");
        lore.add(ChatColor.GRAY + "Click to View Courses!");
        lore.add(" ");
        lore.add(ChatColor.GREEN + "UNLOCKED");
        easyM.setLore(lore);
        easyM.addEnchant(Enchantment.SILK_TOUCH, 1, false);
        easyM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        easy.setItemMeta(easyM);
        mainMenu.setItem(10, easy);
        lore.clear();


        ItemMeta bookMeta = backButton.getItemMeta();
        bookMeta.setDisplayName(ChatColor.RED + "Main Menu");
        lore.add(ChatColor.GRAY + "Return to Main Menu!");
        bookMeta.setLore(lore);
        backButton.setItemMeta(bookMeta);
        lore.clear();
    }


    public ItemStack getBackButton() {
        return backButton;
    }

    public Inventory getMainMenu() {
        return mainMenu;
    }

}


