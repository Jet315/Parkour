package me.jet315.timedpmparkour.events;

import me.jet315.timedpmparkour.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Jet on 16/12/2017.
 */
public class ClickEvent implements Listener{

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Action action = e.getAction();

        //If someone steps on a pressure pad, this event is called - this stops that right here!
        if(action.equals(Action.PHYSICAL)){
            return;
        }

        if(e.getPlayer().getInventory().getItemInMainHand().isSimilar(Core.getInstance().getMenuItem())){
            Core.getInstance().getPlayerGUI().openGUI(p);
            e.setCancelled(true);
        }
    }
}
