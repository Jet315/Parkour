package me.jet315.timedpmparkour.commands.admincommands;

import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.commands.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jet on 16/12/2017.
 */
public class SetSpawnCommand extends CommandExecutor {

    public SetSpawnCommand() {

        setCommand("setspawn");
        setPermission("pk.command.createarena");
        setLength(2);
        setPlayer();
        setUsage("/pk setspawn <name>");
        //lms create name

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        if (Core.getInstance().getArenaManager().getArenas().containsKey(args[1])) {
            Core.getInstance().getArenaManager().setSpawnLocation(((Player)sender).getLocation(),args[1]);
            sender.sendMessage(ChatColor.GREEN + "Arena " + args[1] + " has had it's spawn relocated");
            return;
        }
        Core.getInstance().getArenaManager().setSpawnLocation(((Player)sender).getLocation(),args[1]);
        sender.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.RED + args[0] + ChatColor.GREEN + " Has had it's spawn set");
        sender.sendMessage(ChatColor.GREEN + "Now set the starting location of the course by flying to the wooden pressure pad, stand on it, and typing " + ChatColor.RED + " /pk setstart " + args[1]);

    }
}
