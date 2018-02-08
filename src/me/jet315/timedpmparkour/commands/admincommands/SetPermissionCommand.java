package me.jet315.timedpmparkour.commands.admincommands;

import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.commands.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jet on 16/12/2017.
 */
public class SetPermissionCommand extends CommandExecutor {

    public SetPermissionCommand() {

        setCommand("setpermission");
        setPermission("pk.command.createarena");
        setLength(3);
        setBoth();
        setUsage("/pk setpermission <permission> <name>");
        //lms create name

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        String permission = args[1];
        String arena = args[2];
        Core.getInstance().getArenaManager().setPermission(permission,arena);
        p.sendMessage(ChatColor.RED + "To load the arena, force load the arena using  " + ChatColor.RED + "/pk forceload " + args[2] + " (Or restart the server)");

    }
}
