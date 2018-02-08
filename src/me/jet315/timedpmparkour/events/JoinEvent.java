package me.jet315.timedpmparkour.events;

import me.jet315.timedpmparkour.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.concurrent.ExecutionException;

/**
 * Created by Jet on 13/12/2017.
 */
public class JoinEvent implements Listener{

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws ExecutionException, InterruptedException {
        Player p = e.getPlayer();

        Core.getInstance().getMySQLManager().loadPlayerData(p);

        p.getInventory().setItem(0,Core.getInstance().getMenuItem());

    }
}
