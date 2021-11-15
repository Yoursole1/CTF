package me.yoursole.ctf.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

object BuildLimit : Listener {
    @EventHandler
    fun onBuild(e: BlockPlaceEvent) {
        if (e.player.isOp) {
            return
        }
        if (e.blockPlaced.location.blockY > 120) {
            e.isCancelled = true
            e.player.sendMessage("§cYou can not place blocks above y=120")
        }
    }
}