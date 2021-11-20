package me.yoursole.ctf.commands

import me.yoursole.ctf.datafiles.GameData
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object Test : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp) return true
        GameData.dropLoc = (sender as Player).location
        return true
    }
}