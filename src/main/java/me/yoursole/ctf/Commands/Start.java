package me.yoursole.ctf.Commands;

import me.yoursole.ctf.DataFiles.GameData;
import me.yoursole.ctf.DataFiles.GameLoop;
import me.yoursole.ctf.DataFiles.Items.Flag;
import me.yoursole.ctf.DataFiles.Utils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class Start implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(GameData.it==null){
            if(sender.isOp()){
                //
                Utils.generateNewWorlds();
                StructureType type = GameData.structureTypes.stream().skip((int) (GameData.structureTypes.size() * Math.random())).findFirst().orElse(null);
                Location spawn = Utils.getStructure(GameData.world,type);
                GameData.gameSpawnPoint=spawn;
                spawn.setY(GameData.world.getHighestBlockYAt(spawn));

                StructureType netherType = GameData.structureTypesNether.stream().skip((int) (GameData.structureTypesNether.size() * Math.random())).findFirst().orElse(null);
                GameData.netherMainPoint = Utils.getStructure(GameData.world_nether,netherType);
                GameData.world_nether.getWorldBorder().setSize(100);
                GameData.world_nether.getWorldBorder().setCenter(GameData.netherMainPoint);

                GameData.world.getWorldBorder().setCenter(spawn);
                GameData.world.getWorldBorder().setSize(200);

                for(Player player : Bukkit.getOnlinePlayers()){
                    Utils.sendPlayer(player,GameData.world);
                }

                //on player death, tp player to world spawn if they are playing the game
                //on player portal, tp to appropriate dimension
                //
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
                    playera.sendMessage(ChatColor.AQUA+"The selected structure is: "+ (type != null ? type.getName() : null) +"!");
                    playera.setHealth(playera.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    playera.getActivePotionEffects().clear();

                    //


                    //
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
