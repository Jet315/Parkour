package me.jet315.timedpmparkour.commands.admincommands;

import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.commands.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jet on 17/12/2017.
 */
public class SetHoloCommand extends CommandExecutor {

    public SetHoloCommand() {

        setCommand("setholo");
        setPermission("pk.command.createarena");
        setLength(2);
        setPlayer();
        setUsage("/pk setholo <name>");
        //lms create name

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        Core.getInstance().getArenaManager().setHoloPossition(p.getLocation(),args[1]);
        sender.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.RED + args[1] + ChatColor.GREEN + " Has had it's HOLO set");
        p.sendMessage(ChatColor.GREEN + "Now set the joining permission (if there is one) using " + ChatColor.RED + "/pk setpermission <permission> " + args[1]);
        p.sendMessage(ChatColor.RED + "If you do not want to set a permission, force load the arena using " + ChatColor.RED + "/pk forceload " + args[1]);
    }
}

