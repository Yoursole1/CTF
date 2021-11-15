package me.yoursole.ctf.events

import me.yoursole.ctf.datafiles.GameData
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object Join : Listener {
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        GameData.scores[e.player.uniqueId] = 0
        e.player.discoverRecipes(GameData.recipeKeys.values)
    }
}