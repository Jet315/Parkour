package me.jet315.timedpmparkour.utils;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.manager.ParkourPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;

/**
 * Created by Jet on 17/12/2017.
 */
public class Clock {

    public void startClock() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
            public void run() {
                for (String p : Core.getInstance().getPlayerManager().getPlayers().keySet()) {
                    ParkourPlayer player = Core.getInstance().getPlayerManager().getPlayers().get(p);

                    if (player.isActive()) {
                        //This just returns the current milliseconds since they started the timedpmparkour
                        double theirTime = (double) player.endParkourTime()/1000;
                        ActionBarAPI.sendActionBar(Bukkit.getPlayer(p),ChatColor.RED + "• " + ChatColor.GRAY + "Current Time: " + ChatColor.AQUA  + new DecimalFormat("#.#").format(theirTime) + ChatColor.RED + " •");

                    }
                }
            }
        }, 0L, 2L);
    }

}
