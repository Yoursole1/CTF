package me.yoursole.ctf.events

import me.yoursole.ctf.datafiles.FlagDropper
import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.items.Flag
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent

object PlayerDeath : Listener {
    @EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent) {
        if (GameData.it == null) return
        if (e.player.uniqueId == GameData.it!!.uniqueId) {
            e.player.inventory.remove(Flag.flag)
            e.player.isGlowing = false
            e.drops.clear()
            val killer = e.player.killer ?: Bukkit.getOnlinePlayers()
                .find {
                    it.gameMode == GameMode.SURVIVAL && it.uniqueId != GameData.it?.uniqueId && e.deathMessage!!.contains(
                        it.displayName
                    )
                }
            if (killer != null && killer.uniqueId != e.player.uniqueId) {
                killer.inventory.addItem(Flag.flag)
                killer.sendMessage("§aYou received the flag through murder")
                killer.absorptionAmount += 5
                killer.addPotionEffect(GameData.regen)
                killer.isGlowing = true
                for (player in Bukkit.getOnlinePlayers()) {
                    if (killer.displayName != player.displayName) {
                        player.sendMessage("§a${killer.displayName} has received the flag")
                    }
                }
                GameData.it = killer
            } else {
                if (GameData.inNether) {
                    val chosen = Bukkit.getOnlinePlayers().filter { it.gameMode == GameMode.SURVIVAL && it != GameData.it }
                        .minByOrNull { it.location.distanceSquared(e.entity.location) }
                    if (chosen != null) {
                        chosen.inventory.addItem(Flag.flag)
                        chosen.absorptionAmount += 5
                        chosen.addPotionEffect(GameData.regen)
                        chosen.sendMessage("§aYou were the closest to the player, you have received the flag.")
                        chosen.isGlowing = true
                        if (GameData.inNether && !GameData.netherHunters.contains(chosen)) {
                            GameData.inNether = false
                        }
                        for (playera in Bukkit.getOnlinePlayers()) {
                            if (chosen.displayName != playera.displayName) {
                                playera.sendMessage("§a${chosen.displayName} has received the flag")
                            }
                        }
                        GameData.it = chosen
                    } else {
                        a = e.player
                    }
                } else {
                    GameData.it = null
                    GameData.itLoc = null
                    e.player.inventory.remove(Flag.flag)
                    e.player.isGlowing = false
                    e.drops.clear()
                    FlagDropper.dropFlag(GameData.world!!, e.player.location.blockX, e.player.location.blockZ, 30)
                }
            }
        } else {
            GameData.netherHunters.remove(e.player)
            if (e.player != GameData.it && e.player.killer?.uniqueId == GameData.it!!.uniqueId) {
                GameData.scores.compute(GameData.it!!.uniqueId) { _, i ->
                    (i ?: 0) + 10
                }
            }
        }
    }

    @EventHandler
    fun onRespawn(e: PlayerRespawnEvent) {
        if (e.player == a) {
            (e.player).inventory.addItem(Flag.flag)
            e.player.isGlowing = true
            e.player.absorptionAmount = 5.0
            e.player.sendMessage("§aYou got lucky...you are the only person online so you get to keep the flag")
            a = null
        }
    }

    private var a: Player? = null
}