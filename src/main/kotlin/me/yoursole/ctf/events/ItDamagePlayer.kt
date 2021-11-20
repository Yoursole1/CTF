package me.yoursole.ctf.events

import me.yoursole.ctf.datafiles.GameData
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

object ItDamagePlayer : Listener {
    @EventHandler
    fun onPlayerDamage(e: EntityDamageByEntityEvent) {
        if (GameData.gameRunning) {
            if (e.damager is Player && e.damager.uniqueId === GameData.it!!.uniqueId) {
                val player = e.damager as Player
                if (player.absorptionAmount < 5) {
                    player.absorptionAmount += 0.5
                }
            }
        }
    }
}