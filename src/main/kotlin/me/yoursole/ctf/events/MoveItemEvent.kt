package me.yoursole.ctf.events

import me.yoursole.ctf.datafiles.items.Flag
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent

object MoveItemEvent : Listener {
    @EventHandler
    fun onMoveItem(e: InventoryClickEvent) {
        if (e.currentItem != null && openers.contains(e.whoClicked) && e.currentItem!!
                .isSimilar(Flag.flag)
        ) {
            e.isCancelled = true
            e.whoClicked.sendMessage("§cYou can not get rid of this")
        }
    }

    @EventHandler
    fun onOpenGUI(e: InventoryOpenEvent) {
        if (e.inventory !== e.player.inventory) openers.add(e.player as Player)
    }

    @EventHandler
    fun onCloseGUI(e: InventoryCloseEvent) {
        openers.remove(e.player as Player)
    }

    private val openers = arrayListOf<Player>()
}