package me.yoursole.ctf.Events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BuiltLimit implements Listener {

    @EventHandler
    public void onBuild(BlockPlaceEvent e){
        if(e.getPlayer().isOp()){
            return;
        }

        if(e.getBlockPlaced().getLocation().getBlockY()>120){
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED+"You can not place blocks above y=120");
        }
    }
}
