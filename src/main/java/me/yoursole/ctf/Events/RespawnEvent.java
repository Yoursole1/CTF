package me.yoursole.ctf.Events;

import me.yoursole.ctf.DataFiles.GameData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnEvent implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e){
        if(GameData.it==null)
            return;
        e.setRespawnLocation(GameData.gameSpawnPoint);
    }
}
