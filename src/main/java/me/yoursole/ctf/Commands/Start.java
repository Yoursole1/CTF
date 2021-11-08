package me.yoursole.ctf.Commands;

import me.yoursole.ctf.DataFiles.GameData;
import me.yoursole.ctf.DataFiles.GameLoop;
import me.yoursole.ctf.DataFiles.Items.Flag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Map;
import java.util.UUID;

public class Start implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(GameData.it==null){
            if(sender.isOp()){
                GameData.scores.clear();
                ScoreboardManager sb = Bukkit.getScoreboardManager();
                Scoreboard s = sb.getMainScoreboard();
                Objective o = s.getObjective(DisplaySlot.SIDEBAR);
                if (o != null) o.unregister();

                Player player = Bukkit.getOnlinePlayers().stream().skip((int) (Bukkit.getOnlinePlayers().size() * Math.random())).findFirst().orElse(null);
                player.getInventory().addItem(Flag.flag);
                player.setGlowing(true);
                GameData.itLoc=player.getLocation();
                GameData.it=player;
                player.sendMessage(ChatColor.GREEN+"You got the flag to start!");

                for(Player playera : Bukkit.getOnlinePlayers()){
                    GameData.scores.put(playera.getUniqueId(), 0);
                    if(!player.getDisplayName().equals(playera.getDisplayName())){
                        playera.sendMessage(ChatColor.GREEN + player.getDisplayName()+ " has received the flag");
                        playera.setGlowing(false);
                    }
                    playera.setHealth(playera.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    playera.getActivePotionEffects().clear();
                }
            }else{
                sender.sendMessage("don't u dare ._.");
            }
        }else{
            sender.sendMessage(ChatColor.RED+"The game is already running");
        }

        return true;

    }
}
