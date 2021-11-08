package me.yoursole.ctf.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Top implements CommandExecutor {
    public HashMap<String, Long> cooldowns = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int cooldownTime = 20;
        if(cooldowns.containsKey(sender.getName())) {
            long secondsLeft = ((cooldowns.get(sender.getName())/1000)+cooldownTime) - (System.currentTimeMillis()/1000);
            if(secondsLeft>0) {
                sender.sendMessage(ChatColor.RED+"You cant use /top for another "+ secondsLeft +((secondsLeft!=1)?" seconds!":"second!"));
                return true;
            }
        }


        cooldowns.put(sender.getName(), System.currentTimeMillis());
        Player player = (Player) sender;
        World world = player.getWorld();
        Location highest = world.getHighestBlockAt(player.getLocation()).getLocation();
        highest.setDirection(player.getLocation().getDirection());
        highest.add(0,1,0);
        player.teleport(highest);





        return true;
    }
}
