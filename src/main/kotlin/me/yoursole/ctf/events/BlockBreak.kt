package me.yoursole.ctf.events

import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.Utils.doTelekinesis
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt

object BlockBreak : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (GameData.dropLoc != null && event.block.location.distanceSquared(GameData.dropLoc!!) <= 9) return
        if (event.isCancelled || !event.isDropItems || event.player.gameMode == GameMode.CREATIVE) return
        val drops = event.block.getDrops(event.player.inventory.itemInMainHand)
        if (drops.isEmpty()) return
        for (d in drops) {
            var drop = d
            val smelted = smeltable[drop.type]
            if (smelted != null) {
                val stack = ItemStack(smelted)
                if (smelted == Material.COPPER_INGOT) {
                    stack.amount = Random.nextInt(2..3)
                }
                stack.amount = drop.amount
                event.player.giveExp((experience.getOrDefault(smelted, 0f) * stack.amount).roundToInt(), true)
                drop = stack
            }
            event.player.doTelekinesis(drop)
        }
        event.player.giveExp(event.expToDrop, true)
        event.isDropItems = false
        event.expToDrop = 0
    }

    private val smeltable = hashMapOf(
        Material.RAW_IRON to Material.IRON_INGOT,
        Material.RAW_GOLD to Material.GOLD_INGOT,
        Material.RAW_COPPER to Material.COPPER_INGOT

    )
    private val experience = hashMapOf(
        Material.IRON_INGOT to 0.7f,
        Material.GOLD_INGOT to 1f,
        Material.COPPER_INGOT to 0.7f
    )
}