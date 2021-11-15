package me.yoursole.ctf.commands

import me.yoursole.ctf.datafiles.GameData
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object Scores : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        sender.sendMessage(GameData.scores.entries.joinToString("\n") { (uuid, points) ->
            "§a${Bukkit.getPlayer(uuid)?.displayName}: $points points"
        })
        return true
    }
}