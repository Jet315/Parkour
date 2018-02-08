package me.jet315.timedpmparkour.events;

import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by Jet on 16/12/2017.
 */
public class InventoryClick implements Listener{

    @EventHandler
    public void onClick(InventoryClickEvent e){
        //First null check to see if item exists
        if(e.getCurrentItem() == null) return;

        //Second (May click like air block this prevents that)
        if (e.getCurrentItem().getItemMeta() == null) return;

        //Third
        if(e.getCurrentItem().getItemMeta().getDisplayName() == null) return;


        if(e.getInventory().getName().contains("Main Menu")){
            //Will always cancel the event, as I want a custom thing to happen
            e.setCancelled(true);


            if(e.getCurrentItem().getType() == Material.WOOL){
                if(!(e.getWhoClicked() instanceof Player)) return;
                Player p = (Player) e.getWhoClicked();
                //Stores the map previous to the one currently in the for loop
                for(String difficulty : Core.getInstance().getArenaManager().getMaps().keySet()){
                    if(e.getCurrentItem().getItemMeta().getDisplayName().contains(difficulty.toUpperCase())) {
                        p.closeInventory();
                        p.updateInventory();
                        if (e.getCurrentItem().getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                            Core.getInstance().getPlayerGUI().openGUI(p, difficulty);
                        }else{
                            p.sendMessage(Messages.prefix + ChatColor.RED + " These courses are locked! " + ChatColor.AQUA + "Complete 10 " + (difficulty.equalsIgnoreCase("medium") ? ChatColor.GREEN : (difficulty.equalsIgnoreCase("mega") ? ChatColor.RED : ChatColor.GOLD)) + mapBefore(difficulty) + ChatColor.AQUA +" courses to unlock these!");

                        }
                        break;

                    }
                }
/*                if(e.getCurrentItem().getItemMeta().getDisplayName().contains("EASY")){
                    //Open second inventory
                    e.getWhoClicked().closeInventory();
                    ((Player) e.getWhoClicked()).updateInventory();
                    Core.getInstance().getPlayerManager().openSecondPlayerGUI((Player) e.getWhoClicked(),"easy");
                }else if(e.getCurrentItem().getItemMeta().getDisplayName().contains("MEDIUM")){
                    //Open second inventory
                    e.getWhoClicked().closeInventory();
                    ((Player) e.getWhoClicked()).updateInventory();
                    if(e.getCurrentItem().getEnchantments().containsKey(Enchantment.SILK_TOUCH)){
                        Core.getInstance().getPlayerManager().openSecondPlayerGUI((Player) e.getWhoClicked(),"medium");
                    }else{
                        e.getWhoClicked().sendMessage(Messages.prefix + ChatColor.RED + " These courses are locked! " + ChatColor.AQUA + "Complete 10 " + ChatColor.GREEN +"Easy" + ChatColor.AQUA +" courses to unlock these!");
                    }
                }else if(e.getCurrentItem().getItemMeta().getDisplayName().contains("HARD")){
                    e.getWhoClicked().closeInventory();
                    ((Player) e.getWhoClicked()).updateInventory();
                    if(e.getCurrentItem().getEnchantments().containsKey(Enchantment.SILK_TOUCH)){
                        Core.getInstance().getPlayerManager().openSecondPlayerGUI((Player) e.getWhoClicked(),"hard");
                    }else{
                        e.getWhoClicked().sendMessage(Messages.prefix + ChatColor.RED + " These courses are locked! " + ChatColor.AQUA + "Complete 10 " + ChatColor.GOLD +"Medium" + ChatColor.AQUA +" courses to unlock these!");
                    }

                }//Else if (another one.. etc)*/
            }else if(e.getCurrentItem().getType() == Material.NETHER_STAR){
                e.getWhoClicked().closeInventory();
                ((Player)e.getWhoClicked()).performCommand("pc open main");
            }


            return;
        }
        if(e.getInventory().getName().contains("Courses")){
            //Will always cancel the event, as I want a custom thing to happen
            e.setCancelled(true);

            //Third (May click a block not on the GUI, then return)
            if(e.getCurrentItem().getItemMeta().getDisplayName() == null){
                return;
            }
            if(e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE){
                short durability = e.getCurrentItem().getDurability();
                if(durability == 10){
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage(Messages.prefix + ChatColor.RED + " This Map is a " + ChatColor.GOLD + "Donator Map!" + ChatColor.AQUA + "To gain access to this map & to support the server, purchase a rank over at " + ChatColor.GREEN + "EXAMPLE");
                    return;
                }
                String arena = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
                if(Core.getInstance().getArenaManager().getArenas().containsKey(arena)){
                    e.getWhoClicked().teleport(Core.getInstance().getArenaManager().getArenas().get(arena).getSpawnLocation());
                    e.getWhoClicked().sendMessage(Messages.prefix + ChatColor.GREEN + " You have been teleported to " + ChatColor.RED + arena);
                }else{
                    e.getWhoClicked().sendMessage(Messages.prefix + ChatColor.RED + " Error occurred when teleporting! No Arena found!");
                }
                return;


            }
            if(e.getCurrentItem().getType() == Material.BOOK){
                e.getWhoClicked().closeInventory();
                ((Player) e.getWhoClicked()).updateInventory();
                Core.getInstance().getPlayerGUI().openGUI((Player) e.getWhoClicked());
                return;
            }



        }





    }
    //Sort of hard coded and annoying as this will only work for my needs, but this will return the map before
    private String mapBefore(String map){
        switch (map){
            case "mega":
                return "Hard";
            case "hard":
                return "Medium";
            case "medium":
                return "Easy";
            default: return "Easy";
        }

    }
}
