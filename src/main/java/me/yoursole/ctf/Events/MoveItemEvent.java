package me.yoursole.ctf.Events;

import me.yoursole.ctf.DataFiles.Items.Flag;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class MoveItemEvent implements Listener {
    private static ArrayList<Player> openers = new ArrayList<>();
    @EventHandler
    public void onMoveItem(InventoryClickEvent e){
        if(e.getCurrentItem()!=null&&openers.contains(e.getWhoClicked())&&e.getCurrentItem().isSimilar(Flag.flag)){
            e.setCancelled(true);
            e.getWhoClicked().sendMessage(ChatColor.RED+"You can not get rid of this");
        }
    }

    @EventHandler
    public void onOpenGUI(InventoryOpenEvent e){
        if(e.getInventory()!=e.getPlayer().getInventory())
            openers.add((Player) e.getPlayer());
    }

    @EventHandler
    public void onCloseGUI(InventoryCloseEvent e){
        openers.remove((Player) e.getPlayer());
    }

}
