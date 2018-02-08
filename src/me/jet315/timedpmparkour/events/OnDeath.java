package me.jet315.timedpmparkour.events;

import me.jet315.timedpmparkour.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Created by Jet on 17/12/2017.
 */
public class OnDeath implements Listener{

    @EventHandler
    public void onDeath(PlayerRespawnEvent e){
        if(e.getPlayer().getInventory().contains(Core.getInstance().getMenuItem())) return;
        e.getPlayer().getInventory().setItem(0, Core.getInstance().getMenuItem());
    }
}
