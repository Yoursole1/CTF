package me.yoursole.ctf.events

import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.items.Flag
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

object Leave : Listener {
    @EventHandler
    fun onPlayerLeave(e: PlayerQuitEvent) {

        if (e.player.uniqueId == GameData.it?.uniqueId) {
            e.player.inventory.remove(Flag.flag)
            val chosen = Bukkit.getOnlinePlayers().filter { it.gameMode == GameMode.SURVIVAL && it != GameData.it }
                .minByOrNull { it.location.distanceSquared(e.player.location) }
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
            }
        }
        /*
        GameData.netherHunters.remove(e.player)
        GameData.netherBackupLocs.remove(e.player)
         */

    }
}