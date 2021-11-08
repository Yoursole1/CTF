package me.yoursole.ctf.Commands;

import me.yoursole.ctf.DataFiles.GameData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomItem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            if (args.length == 0) return false;
            if (sender instanceof Player) {
                Player player = (Player) sender;
                ItemStack item = GameData.customItems.get(args[0]);
                if (item == null) {
                    sender.sendMessage(ChatColor.RED + "Custom item " + args[0] + " not found!");
                    return true;
                }
                player.getInventory().addItem(item);
            }
        } else return false;
        return true;
    }
}
