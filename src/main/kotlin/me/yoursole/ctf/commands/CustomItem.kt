package me.yoursole.ctf.commands

import me.yoursole.ctf.datafiles.GameData
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object CustomItem : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) return false
        if (sender !is Player) return false
        val item = GameData.customItems[args[0]]
        val amount = args.getOrNull(1)?.toIntOrNull() ?: 1
        if (item == null) {
            sender.sendMessage("§cCustom item ${args[0]} not found!")
            return true
        }
        sender.inventory.addItem(item.asQuantity(amount))
        return true
    }
}