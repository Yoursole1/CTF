package me.yoursole.ctf.Events;

import me.yoursole.ctf.DataFiles.GameData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Leave implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        GameData.scores.remove(e.getPlayer());
    }
}
