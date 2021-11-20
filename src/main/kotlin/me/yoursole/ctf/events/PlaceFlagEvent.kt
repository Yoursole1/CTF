package me.yoursole.ctf.events

import me.yoursole.ctf.datafiles.GameData
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

object PlaceFlagEvent : Listener {
    @EventHandler
    fun onPlaceFlag(e: BlockPlaceEvent) {
        if (GameData.gameRunning && e.blockPlaced.type == Material.BLUE_BANNER || e.blockPlaced.type == Material.BLUE_WALL_BANNER) {
            e.isCancelled = true
            e.player.sendMessage("§cYou can not place this item")
        }
    }
}