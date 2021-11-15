package me.yoursole.ctf.events

import me.yoursole.ctf.datafiles.GameData
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerPickupItemEvent

object GetCompass : Listener {
    @EventHandler
    fun onGetCompass(e: CraftItemEvent) {
        if (GameData.it != null) {
            if (e.currentItem?.type == Material.COMPASS) {
                (e.whoClicked as Player).compassTarget = GameData.it!!.location
                e.whoClicked.sendMessage("§aYour compass is now pointing to the player who has the flag!")
            }
        }
    }

    @EventHandler
    fun onPickupItem(e: PlayerPickupItemEvent) {
        if (GameData.it != null) {
            if (e.item.itemStack.type == Material.COMPASS) {
                e.player.compassTarget = GameData.it!!.location
                e.player.sendMessage("§aYour compass is now pointing to the player who has the flag!")
            }
        }
    }

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent?) {
        if (GameData.it != null) {
            for (player in Bukkit.getOnlinePlayers()) {
                player.compassTarget = GameData.it!!.location
            }
        }
    }
}