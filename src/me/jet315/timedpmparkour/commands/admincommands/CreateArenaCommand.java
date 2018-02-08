package me.jet315.timedpmparkour.commands.admincommands;

import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.commands.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by Jet on 16/12/2017.
 */
public class CreateArenaCommand extends CommandExecutor {

    public CreateArenaCommand() {

        setCommand("createarena");
        setPermission("pk.command.createarena");
        setLength(2);
        setPlayer();
        setUsage("/pk createarena <name>");
        //lms create name

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        if (Core.getInstance().getArenaManager().getArenas().containsKey(args[1])) {
            sender.sendMessage(ChatColor.RED + "This command already exists!");
            return;
        }
        Core.getInstance().getArenaManager().createArena(args[1]);
        sender.sendMessage(ChatColor.GREEN + "An Arena with the ID " + ChatColor.RED + args[1] + ChatColor.GREEN + " Has now been created!");
        sender.sendMessage(ChatColor.GREEN + "Now set the spawn location using " + ChatColor.RED + " /pk setspawn " + args[1]);


    }
}