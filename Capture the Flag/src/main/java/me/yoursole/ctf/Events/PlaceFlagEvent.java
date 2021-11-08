package me.yoursole.ctf.Events;

import me.yoursole.ctf.DataFiles.GameData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceFlagEvent implements Listener {
    @EventHandler
    public void onPlaceFlag(BlockPlaceEvent e){
        if(GameData.it!=null&&e.getBlockPlaced().getType().equals(Material.BLUE_BANNER)||e.getBlockPlaced().getType().equals(Material.BLUE_WALL_BANNER)){
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED+"You can not place this item");
        }
    }
}
