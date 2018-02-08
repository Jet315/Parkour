package me.jet315.timedpmparkour.commands.admincommands;

import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.commands.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jet on 16/12/2017.
 */
public class DeleteArenaCommand extends CommandExecutor {

    public DeleteArenaCommand() {

        setCommand("delete");
        setPermission("pk.command.createarena");
        setLength(2);
        setBoth();
        setUsage("/pk delete <name>");
        //lms create name

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        Core.getInstance().getArenaManager().deleteArena(args[1]);
        sender.sendMessage(ChatColor.GREEN + args[1]+ " Arena Deleted");
        Core.getInstance().getArenaManager().deleteArena(args[1]);

    }
}
