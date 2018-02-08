package me.jet315.timedpmparkour.commands.admincommands;

import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.commands.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jet on 16/12/2017.
 */
public class SetStartCommand extends CommandExecutor {

    public SetStartCommand() {

        setCommand("setstart");
        setPermission("pk.command.createarena");
        setLength(2);
        setPlayer();
        setUsage("/pk setstart <name>");
        //lms create name

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        Block block = p.getLocation().getBlock();
        if(block.getType() == Material.WOOD_PLATE){
            Core.getInstance().getArenaManager().setStartPadLocation(block.getLocation(),args[1]);
            sender.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.RED + args[1] + ChatColor.GREEN + " Has had it's starting location set");
            sender.sendMessage(ChatColor.GREEN + "Now set the ending location of the course by flying to the golden pressure pad, stand on it, and typing " + ChatColor.RED + " /pk setend " + args[1]);
        }else{
            p.sendMessage(ChatColor.RED + "It appears you are not standing on a wooden pressure plate..");
            return;
        }
    }
}
