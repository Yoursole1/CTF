package me.yoursole.ctf.Events;

import me.yoursole.ctf.DataFiles.GameData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class GetCompass implements Listener {
    @EventHandler
    public void onGetCompass(CraftItemEvent e){
        if(GameData.it!=null){
            if(Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.COMPASS)){
                ((Player)e.getWhoClicked()).setCompassTarget(GameData.it.getLocation());
                e.getWhoClicked().sendMessage(ChatColor.GREEN+"Your compass is now pointing to the player who has the flag!");
            }
        }

    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent e){
        if(GameData.it!=null){
            if(e.getItem().getItemStack().getType().equals(Material.COMPASS)){
                e.getPlayer().setCompassTarget(GameData.it.getLocation());
                e.getPlayer().sendMessage(ChatColor.GREEN+"Your compass is now pointing to the player who has the flag!");
            }
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if(GameData.it!=null){
            for(Player player : Bukkit.getOnlinePlayers()){
                player.setCompassTarget(GameData.it.getLocation());
            }
        }

    }
}
