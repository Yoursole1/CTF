package me.yoursole.ctf.DataFiles;

import me.yoursole.ctf.DataFiles.Items.HermesBoots;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GameLoop {
    public static int taskId = -1;
    private static final String[] arrows = new String[] {
            "←", "↑", "→", "↓", "↖", "↗", "↘", "↙"
    };

    public GameLoop() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("CapturetheFlag")), () -> {
            if (GameData.it != null) {
                GameData.scores.compute(GameData.it.getUniqueId(), (player, score) -> (score == null ? 0 : score) + 1);
                GameData.it.addPotionEffect(GameData.slowness);
                GameData.it.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40.0);
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (GameData.it == null) {
                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
                    player.addPotionEffect(GameData.saturation);
                } else {
                    if (player.getUniqueId() != GameData.it.getUniqueId()) {
                        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
                        player.addPotionEffect(GameData.haste);
                        if (GameData.it.isInsideVehicle()) {
                            player.addPotionEffect(GameData.dolphin);
                        }
                    }
                }
                player.addPotionEffect(GameData.nightVision);
                boolean canFly = player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE;
                for (ItemStack armor : player.getInventory().getArmorContents()) {
                    if (armor == null) continue;
                    if (Utils.compareBreakable(armor, HermesBoots.item)) {
                        canFly = true;
                    }
                }
                player.setAllowFlight(canFly);
                if (!canFly) player.setFlying(false);
            }
            if (GameData.timerMs != -1L) {
                long diff = (GameData.timerMs - System.currentTimeMillis()) / 1000L;
                int min = (int) (diff / 60);
                int sec = (int) (diff % 60);


                String flagHolder = GameData.it == null ? "Nobody" : GameData.it.getDisplayName();
                String timeString = min > 0 ? min + "m " + sec + "s" : sec + "s";

                for (Player player : Bukkit.getOnlinePlayers()) {
                    String arrow = "|";
                    if (GameData.it != null && GameData.it != player) {
                        Location itLoc = GameData.itLoc;
                        if(GameData.netherHunters.contains(player)&&GameData.inNether){
                            itLoc= Objects.requireNonNull(Bukkit.getPlayer(GameData.it.getUniqueId())).getLocation();
                        }else if(GameData.netherHunters.contains(player)&&!GameData.inNether){
                            itLoc=GameData.netherBackupLocs.get(player);
                        }

                        double a = (itLoc.getX()-player.getLocation().getX());
                        double c = Math.sqrt((a*a)+(Math.pow((itLoc.getZ()-player.getLocation().getZ()),2)));
                        double angleBetween = Math.asin(a/c)*180/3.14;
                        if(player.getLocation().getZ()<itLoc.getZ()){
                            angleBetween=180- Math.abs(angleBetween);
                            if(player.getLocation().getX()>itLoc.getX()){
                                angleBetween*=-1;
                            }
                        }



                        double angle = Math.toRadians(player.getLocation().getYaw());
                        double x = Math.cos(angle);
                        double angleFacing = 180-(Math.acos(x)*180/3.14);
                        if(player.getLocation().getDirection().getX()<0){
                            angleFacing*=-1;
                        }
                        /*
                        if(angleFacing<-90&&angleBetween<-90){

                        }
                        */
                        double combined = angleFacing-angleBetween;
                        if(Math.abs(combined)>180){
                            if(combined>0){
                                combined=-1*(360-combined);
                            }else{
                                combined=(360- Math.abs(combined));
                            }
                        }


                        if(combined>-22.5&&combined<22.5){
                            //forward
                            arrow = arrows[1];
                        }else if(combined>=22.5&&combined<67.5){
                            //diag left forward
                            arrow = arrows[4];
                        }else if(combined>=67.5&&combined<112.5){
                            //left
                            arrow = arrows[0];
                        }else if(combined>=112.5&&combined<157.5){
                            //diag left backword
                            arrow = arrows[7];
                        }else if((combined>=157&&combined<=180)||(combined>=-180&&combined<-157.5)){
                            //backwards
                            arrow = arrows[3];
                        }else if(combined>=-157.5&&combined<-112.5) {
                            // backward right
                            arrow = arrows[6];
                        } else if (combined>=-112.5&&combined<-67.5) {
                            // right
                            arrow = arrows[2];
                        } else if (combined>=-67.5&&combined<-22.5) {
                            // forward right
                            arrow = arrows[5];
                        }
                    }

                    if(GameData.inNether){
                        arrow=ChatColor.RED+arrow+" (NETHER)";
                    }else {
                        arrow=ChatColor.BLUE+arrow+" (OVERWORLD)";
                    }
                    TextComponent text = new TextComponent();
                    text.setText(ChatColor.GREEN + flagHolder + " has the flag!" + ChatColor.WHITE + ChatColor.BOLD + " " + arrow + " " + ChatColor.AQUA + timeString);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
                    GameData.arrow=arrow;
                }
            }
            if (!GameData.scores.isEmpty()) {
                ScoreboardManager sb = Bukkit.getScoreboardManager();
                Scoreboard s = sb.getMainScoreboard();
                Objective o = s.getObjective(DisplaySlot.SIDEBAR);
                if (o == null) {
                    o = s.registerNewObjective("Score", "dummy", "Scores");
                    o.setDisplaySlot(DisplaySlot.SIDEBAR);
                }
                for (Map.Entry<UUID, Integer> e : GameData.scores.entrySet()) {
                    if (e.getValue() > 0) {
                        Player p = Bukkit.getPlayer(e.getKey());
                        Score sc = o.getScore(p.getDisplayName());
                        sc.setScore(e.getValue());
                    }
                }
            }
        }, 0L, 20L);
    }

}
