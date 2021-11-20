package me.yoursole.ctf.events

import me.yoursole.ctf.datafiles.GameData
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

object Leave : Listener {
    @EventHandler
    fun onPlayerLeave(e: PlayerQuitEvent) {
        GameData.scores.remove(e.player.uniqueId)
        /*
        GameData.netherHunters.remove(e.player)
        GameData.netherBackupLocs.remove(e.player)
         */

    }
}