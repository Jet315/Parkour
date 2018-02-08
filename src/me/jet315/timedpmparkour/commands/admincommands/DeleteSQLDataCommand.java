package me.jet315.timedpmparkour.commands.admincommands;

import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.commands.CommandExecutor;
import me.jet315.timedpmparkour.utils.Messages;
import org.bukkit.command.CommandSender;

/**
 * Created by Jet on 01/01/2018.
 */
public class DeleteSQLDataCommand extends CommandExecutor {

    public DeleteSQLDataCommand() {

        setCommand("deletemysql");
        setPermission("pk.command.createarena");
        setLength(2);
        setBoth();
        setUsage("/pk deletemysql <name>");
        //lms create name

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(Messages.prefix + " Map data has been reset for map " + args[1] + " Note any players currently on the course are likely to be glitched - please restart the server");
        //Remove players map objects so it deletes there cashed data
        //TODO

        //Remove from database
        Core.getInstance().getMySQLManager().deleteMap(args[1]);
    }
}
