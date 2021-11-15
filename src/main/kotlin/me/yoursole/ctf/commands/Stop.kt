package me.yoursole.ctf.commands

import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.Utils
import me.yoursole.ctf.datafiles.Utils.sendToWorld
import me.yoursole.ctf.datafiles.items.Flag
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.*

object Stop : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (GameData.it != null) {
            for (player in Bukkit.getOnlinePlayers()) {
                player.inventory.remove(Flag.flag)
                player.isGlowing = false
                player sendToWorld Bukkit.getWorld("world")
            }
            Utils.deleteWorlds()
            GameData.it!!.isGlowing = false
            GameData.scores.compute(GameData.it!!.uniqueId) { _: UUID?, score: Int? -> (score ?: 0) + 60 }
            GameData.it = null
            sender.sendMessage("Game Stopped")
            val a = GameData.scores.entries.maxByOrNull { it.value }!!.key
            for (player in Bukkit.getOnlinePlayers()) {
                player.sendMessage("§aThe winner is ${Bukkit.getPlayer(a)!!.displayName}!")
            }
            GameData.scores.clear()
        } else {
            sender.sendMessage("§cThe game is not running")
        }
        return true
    }
}