package me.yoursole.ctf.Events;

import me.yoursole.ctf.DataFiles.GameData;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.Objects;

public class ItLocationManager implements Listener {
    private static final String[] arrows = new String[] {
            "←", "↑", "→", "↓", "↖", "↗", "↘", "↙"
    };
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if(GameData.it==null){
            return;
        }
        long diff = (GameData.timerMs - System.currentTimeMillis()) / 1000L;
        int min = (int) (diff / 60);
        int sec = (int) (diff % 60);


        String flagHolder = GameData.it == null ? "Nobody" : GameData.it.getDisplayName();
        String timeString = min > 0 ? min + "m " + sec + "s" : sec + "s";
        if(e.getPlayer().getUniqueId().equals(GameData.it.getUniqueId()) &&e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation()).getType().equals(Material.NETHER_PORTAL)){
            GameData.backUpLoc=GameData.itLoc;
            return;
        }
        if(GameData.it!=null&&e.getPlayer().getUniqueId().equals(GameData.it.getUniqueId()) &&!GameData.inNether){
            GameData.itLoc=e.getPlayer().getLocation();
            return;
        }
        if(!e.getPlayer().getUniqueId().equals(GameData.it.getUniqueId()) &&e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation()).getType().equals(Material.NETHER_PORTAL)){
            GameData.netherBackupLocs.put(e.getPlayer(),e.getPlayer().getLocation());
            return;
        }

        Player player = e.getPlayer();
        String arrow = "|";
        if (GameData.it != null && GameData.it != player) {
            Location itLoc = GameData.itLoc.clone();
            if(GameData.netherHunters.contains(player)&&GameData.inNether){
                itLoc= Objects.requireNonNull(Bukkit.getPlayer(GameData.it.getUniqueId())).getLocation();
            }else if(GameData.netherHunters.contains(player)&&!GameData.inNether){
                itLoc=GameData.netherBackupLocs.get(player);
            }

            double a = (itLoc.getX()-player.getLocation().getX());
            double c = Math.sqrt((a*a)+(Math.pow((itLoc.getZ()-player.getLocation().getZ()),2)));
            double angleBetween = Math.asin(a/c)*180/3.14;
            if(player.getLocation().getZ()<itLoc.getZ()){
                angleBetween=180-Math.abs(angleBetween);
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

            if(angleFacing<-90&&angleBetween<-90){

            }
            double combined = angleFacing-angleBetween;
            if(Math.abs(combined)>180){
                if(combined>0){
                    combined=-1*(360-combined);
                }else{
                    combined=(360-Math.abs(combined));
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
            arrow= ChatColor.RED+arrow+" (NETHER)";
        }else {
            arrow=ChatColor.BLUE+arrow+" (OVERWORLD)";
        }
        if(GameData.arrow.equals(arrow)){
            return;
        }
        TextComponent text = new TextComponent();
        text.setText(ChatColor.GREEN + flagHolder + " has the flag!" + ChatColor.WHITE + ChatColor.BOLD + " " + arrow + " " + ChatColor.AQUA + timeString);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);


    }


    @EventHandler
    public void onPlayerChangeDim(PlayerPortalEvent e){
        if(GameData.it==null){
            return;
        }
        if(e.getPlayer().getWorld().getEnvironment()== World.Environment.NETHER){
            if(e.getPlayer().getUniqueId()==GameData.it.getUniqueId()){
                GameData.inNether=false;
            }else{
                GameData.netherHunters.remove(e.getPlayer());
            }
            //player exits nether (ik its backwards)


        }else if(e.getPlayer().getWorld().getEnvironment()== World.Environment.NORMAL){
            //player enters nether
            if(e.getPlayer().getUniqueId()==GameData.it.getUniqueId()){
                GameData.itLoc=GameData.backUpLoc;

                GameData.inNether=true;
            }else{
                GameData.netherHunters.add(e.getPlayer());
            }

        }
    }

}
