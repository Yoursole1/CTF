package me.yoursole.ctf.Commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleChat implements CommandExecutor {

    public static boolean chatMuted = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) return false;
        chatMuted = !chatMuted;

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.YELLOW + sender.getName() + (chatMuted ? " muted" : " unmuted") + " the chat!");
        }

        return true;
    }
}
