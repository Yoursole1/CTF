package me.yoursole.ctf.Events;

import me.yoursole.ctf.DataFiles.GameData;
import me.yoursole.ctf.DataFiles.MathUtils;
import org.bukkit.Chunk;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

public class ChunkLoad implements Listener {

    @EventHandler
    /**
     * Adds a random custom item 1/10 times to a chest
     */
    public void onLoadChunk(ChunkLoadEvent e){
        Chunk c = e.getChunk();

        if(c.isLoaded())
            return;


        for(BlockState b : c.getTileEntities()){
            if(b instanceof Chest){
                Chest chest = (Chest) b;
                int rnd = MathUtils.getRandom(1,10);
                if(rnd == 5){
                    ItemStack item = GameData.customItems.values().stream().skip((int) (GameData.customItems.values().size() * Math.random())).findFirst().orElse(null);
                    chest.getBlockInventory().addItem(item);
                }
            }
        }
    }
}
