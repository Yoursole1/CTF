package me.yoursole.ctf.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object Top : CommandExecutor {
    private val cooldowns = HashMap<String, Long>()
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) return false
        val cooldownTime = 20
        if (cooldowns.containsKey(sender.name)) {
            val secondsLeft = cooldowns[sender.name]!! / 1000 + cooldownTime - System.currentTimeMillis() / 1000
            if (secondsLeft > 0) {
                sender.sendMessage("§aYou cant use /top for another $secondsLeft ${if (secondsLeft != 1L) "seconds!" else "second!"}")
                return true
            }
        }
        cooldowns[sender.name] = System.currentTimeMillis()
        val world = sender.world
        val highest = world.getHighestBlockAt(sender.location).location
        highest.direction = sender.location.direction
        highest.add(0.0, 1.0, 0.0)
        sender.teleport(highest)
        return true
    }
}