package me.jet315.timedpmparkour.commands.admincommands;

import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.commands.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jet on 16/12/2017.
 */
public class SetDifficultyCommand extends CommandExecutor {

    public SetDifficultyCommand() {

        setCommand("setdifficulty");
        setPermission("pk.command.createarena");
        setLength(3);
        setBoth();
        setUsage("/pk setdifficulty <difficulty> <name>");
        //lms create name

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        String lowercase = args[1].toLowerCase();
        Core.getInstance().getArenaManager().setDifficulty(args[2],lowercase);
        p.sendMessage(ChatColor.GREEN + "A difficulty rating of "+ lowercase + " has been set for " + args[2]);
        p.sendMessage(ChatColor.GREEN + "Now set Holo (if there is one) using " + ChatColor.RED + "/pk setholo " + args[2]);
        p.sendMessage(ChatColor.GREEN + "Now set the joining permission (if there is one) using " + ChatColor.RED + "/pk setpermission <permission> " + args[2]);
        p.sendMessage(ChatColor.RED + "If you do not want to set a permission, force load the arena using " + ChatColor.RED + "/pk forceload " + args[2]);
    }
}
