package me.yoursole.ctf.Commands;

import me.yoursole.ctf.DataFiles.GameData;
import me.yoursole.ctf.DataFiles.GameLoop;
import me.yoursole.ctf.DataFiles.Items.Flag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;


public class Stop implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if(GameData.it!=null){
                if (sender.isOp()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getInventory().remove(Flag.flag);
                        PotionEffect hasteEffect = GameData.it.getPotionEffect(PotionEffectType.FAST_DIGGING);
                        player.setGlowing(false);
                    }
                    GameData.it.setGlowing(false);
                    GameData.scores.compute(GameData.it.getUniqueId(), (player, score) -> (score == null ? 0 : score) + 60);
                    GameData.it = null;
                    sender.sendMessage("Game Stopped");


                    UUID a = (UUID) GameData.scores.keySet().toArray()[0];
                    for(UUID uuid : GameData.scores.keySet()){
                        if(GameData.scores.get(uuid)>GameData.scores.get(a)){
                            a=uuid;
                        }
                    }
                    for(Player player : Bukkit.getOnlinePlayers()){
                        player.sendMessage(ChatColor.GREEN+"The winner is "+ Bukkit.getPlayer(a).getDisplayName()+"!");
                    }
                    GameData.scores.clear();
                } else {
                    sender.sendMessage("don't u dare ._.");
                }
            }else{
                sender.sendMessage(ChatColor.RED+"The game is not running");
            }

            return true;

        }

    }
