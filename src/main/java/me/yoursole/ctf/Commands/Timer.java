package me.yoursole.ctf.Commands;

import me.yoursole.ctf.DataFiles.GameData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Timer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            if (args.length == 0) return false;
            if (args[0].equalsIgnoreCase("clear")) GameData.timerMs = -1L;
            else GameData.timerMs = System.currentTimeMillis() + Long.parseLong(args[0]);
        } else {
            return false;
        }
        return true;

    }
}
