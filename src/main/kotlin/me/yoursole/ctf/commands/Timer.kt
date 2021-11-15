package me.yoursole.ctf.commands

import me.yoursole.ctf.datafiles.GameData
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object Timer : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender.isOp) {
            if (args.isEmpty()) return false
            if (args[0].equals("clear", ignoreCase = true)) GameData.timerMs = -1L else GameData.timerMs =
                System.currentTimeMillis() + args[0].toLong()
        } else {
            return false
        }
        return true
    }
}