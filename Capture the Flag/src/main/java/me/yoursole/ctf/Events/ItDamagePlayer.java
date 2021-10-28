package me.yoursole.ctf.Events;

import me.yoursole.ctf.DataFiles.GameData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ItDamagePlayer implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e){
        if(GameData.it!=null){
            if(e.getDamager().getType()== EntityType.PLAYER&& e.getDamager().getUniqueId()== GameData.it.getUniqueId()){
                double absorption = ((Player) e.getDamager()).getAbsorptionAmount();
                if(absorption<5){
                    ((Player) e.getDamager()).setAbsorptionAmount(((Player) e.getDamager()).getAbsorptionAmount()+0.5);
                }

            }
        }

    }
}
