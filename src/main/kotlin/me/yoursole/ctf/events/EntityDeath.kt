package me.yoursole.ctf.events

import me.yoursole.ctf.datafiles.Utils.doTelekinesis
import org.bukkit.Material
import org.bukkit.entity.Evoker
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack

object EntityDeath : Listener {
    @EventHandler
    fun onDeath(event: EntityDeathEvent) {
        val entity = event.entity
        val killer = entity.killer
        if (killer == null || entity is Player) return
        if (entity is Evoker) {
            val amount = event.drops.find { it.type == Material.TOTEM_OF_UNDYING }?.amount ?: 1
            event.drops.clear()
            event.drops.add(ItemStack(Material.DIAMOND, amount))
            killer.sendMessage("§aYou received $amount §bdiamond(s) §ainstead of $amount totem(s)!")
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onDeathTelekinesis(event: EntityDeathEvent) {
        val entity = event.entity
        val killer = entity.killer
        if (killer == null || entity is Player) return
        killer.giveExp(event.droppedExp, true)
        for (drop in event.drops) killer.doTelekinesis(drop)
        event.drops.clear()
        event.droppedExp = 0
    }
}