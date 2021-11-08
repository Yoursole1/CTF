package me.yoursole.ctf.Events;

import me.yoursole.ctf.DataFiles.GameData;
import me.yoursole.ctf.DataFiles.Items.Flag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Objects;

public class PlayerDeath implements Listener {
    private static Player a= null;
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        if(GameData.it==null)
            return;
        if(Objects.equals(Objects.requireNonNull(e.getEntity().getPlayer()).getUniqueId(), GameData.it.getUniqueId())){
            e.getEntity().getInventory().remove(Flag.flag);
            e.getEntity().getPlayer().setGlowing(false);

            e.getDrops().clear();
            boolean found = false;
            for(Player player : Bukkit.getOnlinePlayers()){
                if (player != e.getEntity().getPlayer()) {
                    if(Objects.requireNonNull(e.getDeathMessage()).contains(player.getDisplayName())){
                        found = true;
                        player.getInventory().addItem(Flag.flag);
                        player.sendMessage(ChatColor.GREEN+"You received the flag through murder");
                        player.setAbsorptionAmount(player.getAbsorptionAmount() + 5);
                        player.addPotionEffect(GameData.regen);
                        player.setGlowing(true);
                        for(Player playera : Bukkit.getOnlinePlayers()){
                            if(!player.getDisplayName().equals(playera.getDisplayName())){
                                playera.sendMessage(ChatColor.GREEN+player.getDisplayName()+" has received the flag");
                            }
                        }
                        GameData.it=player;
                        break;
                    }
                }
            }
            if(!found){
                if(Bukkit.getOnlinePlayers().size()>1){
                    Player player = Bukkit.getOnlinePlayers().stream().skip((int) (Bukkit.getOnlinePlayers().size() * Math.random())).findFirst().orElse(null);
                    while(Objects.equals(player, e.getEntity().getPlayer())){
                        player = Bukkit.getOnlinePlayers().stream().skip((int) (Bukkit.getOnlinePlayers().size() * Math.random())).findFirst().orElse(null);
                    }
                    assert player != null;
                    player.getInventory().addItem(Flag.flag);
                    player.setAbsorptionAmount(player.getAbsorptionAmount() + 5);
                    player.addPotionEffect(GameData.regen);
                    player.sendMessage(ChatColor.GREEN+"You got lucky and received the flag");
                    player.setGlowing(true);
                    if(GameData.inNether&&!GameData.netherHunters.contains(player)){
                        GameData.inNether=false;
                    }
                    for(Player playera : Bukkit.getOnlinePlayers()){
                        if(!player.getDisplayName().equals(playera.getDisplayName())){
                            playera.sendMessage(ChatColor.GREEN+player.getDisplayName()+" has received the flag");
                        }
                    }
                    GameData.it=player;
                }else{
                    a=e.getEntity().getPlayer();
                }



            }
        }else{
            GameData.netherHunters.remove(e.getEntity().getPlayer());
        }


    }
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        if(e.getPlayer().equals(a)){
            Objects.requireNonNull(e.getPlayer()).getInventory().addItem(Flag.flag);
            e.getPlayer().setGlowing(true);
            e.getPlayer().setAbsorptionAmount(5);
            e.getPlayer().sendMessage(ChatColor.GREEN+"You got lucky...you are the only person online so you get to keep the flag");
            a=null;
        }
    }
}
