package me.yoursole.ctf.events

import me.yoursole.ctf.datafiles.GameData
import org.bukkit.block.Chest
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkLoadEvent
import kotlin.random.Random
import kotlin.random.nextInt

object ChunkLoad : Listener {
    @EventHandler
            /**
             * Adds a random custom item 1/10 times to a chest
             */
    fun onLoadChunk(e: ChunkLoadEvent) {
        val c = e.chunk
        if (c.isLoaded) return
        for (b in c.tileEntities) {
            if (b is Chest) {
                if (Random.nextInt(0..10) == 5) {
                    b.blockInventory.addItem(GameData.customItems.values.random())
                }
            }
        }
    }
}