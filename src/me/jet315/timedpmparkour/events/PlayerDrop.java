package me.jet315.timedpmparkour.events;

import me.jet315.timedpmparkour.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Created by Jet on 09/01/2018.
 */
public class PlayerDrop implements Listener{

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if(e.getItemDrop().getItemStack().isSimilar(Core.getInstance().getMenuItem())){
            e.setCancelled(true);
        }
    }
}
