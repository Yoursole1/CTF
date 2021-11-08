package me.yoursole.ctf.Events;

import me.yoursole.ctf.DataFiles.GameData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        GameData.scores.put(e.getPlayer().getUniqueId(),0);
        e.getPlayer().discoverRecipes(GameData.recipeKeys.values());
    }
}
