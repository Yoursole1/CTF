package me.yoursole.ctf.events

import me.yoursole.ctf.commands.ToggleChat
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

object ChatEvent : Listener {
    @EventHandler
    fun onOpChat(e: AsyncPlayerChatEvent) {
        if (e.player.isOp) {
            e.message = "§e${e.message}"
        } else if (ToggleChat.chatMuted) {
            e.isCancelled = true
            e.player.sendMessage("§cChat is currently muted!")
        }
    }
}