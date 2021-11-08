package me.yoursole.ctf.Commands;

import me.yoursole.ctf.DataFiles.GameData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class Scores implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        for(Map.Entry<UUID, Integer> e : GameData.scores.entrySet()){
            sender.sendMessage(ChatColor.GREEN+ Bukkit.getPlayer(e.getKey()).getDisplayName()+": "+e.getValue()+" points");
        }

        return true;
    }
}
