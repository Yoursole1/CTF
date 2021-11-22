package me.yoursole.ctf.datafiles

import me.yoursole.ctf.datafiles.items.Flag
import org.bukkit.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.util.BoundingBox

object FlagDropper : Listener {
    fun dropFlag(world: World, x: Int, z: Int, height: Int = 100) {
        val drop = Location(world, x.toDouble(), 0.toDouble(), z.toDouble()).apply {
            y = world.getHighestBlockYAt(this) + 2.0
        }
        val dropping = drop.clone().apply { y = (y + height).coerceAtMost(255.0) }
        drop.block.type = Material.BEACON
        drop.clone().add(-1.0, -1.0, -1.0).block.type = Material.NETHERITE_BLOCK
        drop.clone().add(-1.0, -1.0, 0.0).block.type = Material.NETHERITE_BLOCK
        drop.clone().add(-1.0, -1.0, 1.0).block.type = Material.NETHERITE_BLOCK
        drop.clone().add(0.0, -1.0, -1.0).block.type = Material.NETHERITE_BLOCK
        drop.clone().add(0.0, -1.0, 0.0).block.type = Material.NETHERITE_BLOCK
        drop.clone().add(0.0, -1.0, 1.0).block.type = Material.NETHERITE_BLOCK
        drop.clone().add(1.0, -1.0, -1.0).block.type = Material.NETHERITE_BLOCK
        drop.clone().add(1.0, -1.0, 0.0).block.type = Material.NETHERITE_BLOCK
        drop.clone().add(1.0, -1.0, 1.0).block.type = Material.NETHERITE_BLOCK

        dropping.block.type = Material.END_GATEWAY
        GameData.dropLoc = drop.apply { y += 1 }
        GameData.droppingPos = dropping
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun blockBreak(event: BlockBreakEvent) {
        if (GameData.dropLoc == null) return
        if (event.block.location.distanceSquared(GameData.dropLoc!!) <= 9) {
            event.player.sendMessage("§cYou cannot break blocks here!")
            event.isCancelled = true
            event.isDropItems = false
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun entityExplode(event: EntityExplodeEvent) {
        if (GameData.dropLoc == null) return
        event.blockList().removeAll { it.location.distanceSquared(GameData.dropLoc!!) < 9 }
    }

    @EventHandler
    fun enterPortal(event: PlayerMoveEvent) {
        if (GameData.droppingPos == null || GameData.it != null || event.player.gameMode != GameMode.SURVIVAL) return
        if (GameData.droppingPos!!.distanceSquared(GameData.dropLoc!!) <= 9 && event.player.boundingBox.overlaps(BoundingBox.of(GameData.droppingPos!!.block))) {
            GameData.droppingPos!!.block.type = Material.AIR
            GameData.droppingPos = null
            val drop = GameData.dropLoc!!.clone().apply { y -= 1 }
            GameData.dropLoc = null
            GameData.it = event.player
            GameData.itLoc = event.player.location
            event.player.absorptionAmount += 5
            event.player.addPotionEffect(GameData.regen)
            event.player.inventory.addItem(Flag.flag)
            event.player.isGlowing = true
            event.player.sendMessage("§aYou have picked up the flag!")
            if (GameData.inNether && !GameData.netherHunters.contains(event.player)) {
                GameData.inNether = false
            }
            for (player in Bukkit.getOnlinePlayers()) {
                if (player == event.player) continue
                player.sendMessage("§a${event.player.name} has picked up the flag!")
            }
            drop.block.type = Material.AIR
            drop.clone().add(-1.0, -1.0, -1.0).block.type = Material.AIR
            drop.clone().add(-1.0, -1.0, 0.0).block.type = Material.AIR
            drop.clone().add(-1.0, -1.0, 1.0).block.type = Material.AIR
            drop.clone().add(0.0, -1.0, -1.0).block.type = Material.AIR
            drop.clone().add(0.0, -1.0, 0.0).block.type = Material.AIR
            drop.clone().add(0.0, -1.0, 1.0).block.type = Material.AIR
            drop.clone().add(1.0, -1.0, -1.0).block.type = Material.AIR
            drop.clone().add(1.0, -1.0, 0.0).block.type = Material.AIR
            drop.clone().add(1.0, -1.0, 1.0).block.type = Material.AIR
        }
    }
}