package me.yoursole.ctf.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object ToggleChat : CommandExecutor {
    var chatMuted = false
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        chatMuted = !chatMuted
        for (player in Bukkit.getOnlinePlayers()) {
            player.sendMessage("§e${sender.name} ${(if (chatMuted) "muted" else "unmuted")} the chat!")
        }
        return true
    }
}