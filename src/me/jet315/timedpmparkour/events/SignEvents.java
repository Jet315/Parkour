package me.jet315.timedpmparkour.events;

import me.jet315.timedpmparkour.Core;
import me.jet315.timedpmparkour.utils.Messages;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Jet on 15/12/2017.
 */
public class SignEvents implements Listener {

    //On create
    @EventHandler
    public void placeSign(SignChangeEvent e) {
        Player p = e.getPlayer();
        if (e.getLine(0).equalsIgnoreCase("pk")) {
            String arenaName = e.getLine(1);
            if (!Core.getInstance().getArenaManager().getArenas().containsKey(arenaName)) {
                p.sendMessage(Messages.prefix + ChatColor.RED + "ERROR - That game does not exist!");
                p.sendMessage(Messages.prefix + ChatColor.RED + "If the name correct, ensure you have set the spawn (Check console for any errors)");
                e.setLine(0, ChatColor.BOLD + "" + ChatColor.RED + "ERROR");
                e.setLine(1, ChatColor.BOLD + "" + ChatColor.RED + "ERROR");
                e.setLine(2, ChatColor.BOLD + "" + ChatColor.RED + "ERROR");
                e.setLine(3, ChatColor.BOLD + "" + ChatColor.RED + "ERROR");
                return;
            }
            //e.setLine(0, ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("SignPrefix")));
            e.setLine(0, ChatColor.translateAlternateColorCodes('&', "&b+==&aStats&b==+"));
            e.setLine(1, ChatColor.RED + "Map: " + ChatColor.YELLOW + arenaName);
            BlockState blockState = e.getBlock().getState();
            Sign s = (Sign) blockState;
            e.setLine(2, ChatColor.AQUA + e.getPlayer().getName());
            e.setLine(3, ChatColor.AQUA + "1000 Seconds");
            new BukkitRunnable() {
                int i = 1;

                public void run() {
                    if (i == 0) {
                        Core.getInstance().getArenaManager().saveSign(s, arenaName);
                        cancel();
                    }
                    i--;
                }

            }.runTaskTimer(Core.getInstance(), 0L, 10L);
        }
    }

    //On Break - reset the WR
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.SIGN || e.getBlock().getType() == Material.SIGN_POST) {
            if (!e.getPlayer().isOp()) return;
            BlockState blockState = e.getBlock().getState();
            Sign s = (Sign) blockState;
            if (s.getLine(0).contains("stats")) {
                String line = s.getLine(1);
                String linesplit[] = line.split(" ");
                if (linesplit.length > 1) {
                    String arena = linesplit[1];
                    e.getPlayer().sendMessage(ChatColor.RED + "SIGN FOR " + arena + " HAS BEEN REMOVED");
                    Core.getInstance().getArenaManager().deleteSign(arena);
                }
            }
        }
    }
}
