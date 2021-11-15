package me.yoursole.ctf.events

import me.yoursole.ctf.datafiles.GameData
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

object RespawnEvent : Listener {
    @EventHandler
    fun onPlayerRespawn(e: PlayerRespawnEvent) {
        if (GameData.it == null) return
        e.respawnLocation = GameData.gameSpawnPoint!!
    }
}