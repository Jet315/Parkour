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
public class SetEndCommand extends CommandExecutor {

    public SetEndCommand() {

        setCommand("setend");
        setPermission("pk.command.createarena");
        setLength(2);
        setPlayer();
        setUsage("/pk setend <name>");
        //lms create name

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        Block block = p.getLocation().getBlock();
        if (block.getType() == Material.GOLD_PLATE) {
            Core.getInstance().getArenaManager().setEndPadLocation(block.getLocation(), args[1]);
            sender.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.RED + args[0] + ChatColor.GREEN + " Has had it's ending location set");
            sender.sendMessage(ChatColor.GREEN + "Now set the difficulty of the course by typing, " + ChatColor.RED + "/pk setdifficulty |easy|medium|hard " + args[1]);
        } else {
            p.sendMessage(ChatColor.RED + "It appears you are not standing on a Golden pressure plate..");
            return;
        }
    }
}