package me.yoursole.ctf.events

import me.yoursole.ctf.datafiles.Utils.getCTFId
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

object DropItemEvent : Listener {
    @EventHandler
    fun onItemDrop(e: PlayerDropItemEvent) {
        if (e.itemDrop.itemStack.getCTFId() == "flag") {
            e.isCancelled = true
            e.player.sendMessage("§cYou can not get rid of this")
        }
    }
}