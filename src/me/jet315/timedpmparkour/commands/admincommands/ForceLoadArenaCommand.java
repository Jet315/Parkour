package me.jet315.timedpmparkour.commands.admincommands;

import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.commands.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jet on 16/12/2017.
 */
public class ForceLoadArenaCommand extends CommandExecutor {

    public ForceLoadArenaCommand() {

        setCommand("forceload");
        setPermission("pk.command.createarena");
        setLength(2);
        setBoth();
        setUsage("/pk forceload <arena>");
        //lms create name

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        sender.sendMessage(ChatColor.RED + "Arena Started: " + Core.getInstance().getArenaManager().forceLoadArena(args[1]));
        p.sendMessage(ChatColor.GREEN + "The Arena, provided there is a Spawn Location, Start Location and End Location, show now be loaded!");
        p.sendMessage(ChatColor.GREEN + "If the Arena has failed to load, check the console for any logs!");
    }
}
