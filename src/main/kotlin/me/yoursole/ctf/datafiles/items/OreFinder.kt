package me.yoursole.ctf.datafiles.items

import me.yoursole.ctf.CTF
import org.bukkit.*
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftShulker
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Shulker
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.inventivetalent.glow.GlowAPI
import java.util.*
import kotlin.math.pow


object OreFinder : Listener {
    private val axeToOre: HashMap<Material?, List<Material>?> =
        object : HashMap<Material?, List<Material>?>() {
            init {
                put(Material.WOODEN_PICKAXE, listOf(Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE))

                put(Material.STONE_PICKAXE,
                    listOf(Material.IRON_ORE,
                        Material.DEEPSLATE_IRON_ORE,
                        Material.COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE,
                        Material.LAPIS_ORE,
                        Material.DEEPSLATE_LAPIS_ORE))

                put(Material.GOLDEN_PICKAXE, listOf(Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE))

                put(Material.IRON_PICKAXE,
                    listOf(Material.IRON_ORE,
                        Material.DEEPSLATE_IRON_ORE,
                        Material.COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE,
                        Material.LAPIS_ORE,
                        Material.DEEPSLATE_LAPIS_ORE,
                        Material.GOLD_ORE,
                        Material.DEEPSLATE_GOLD_ORE,
                        Material.REDSTONE_ORE,
                        Material.DEEPSLATE_REDSTONE_ORE,
                        Material.DIAMOND_ORE,
                        Material.DEEPSLATE_DIAMOND_ORE,
                        Material.EMERALD_ORE,
                        Material.DEEPSLATE_EMERALD_ORE))

                put(Material.DIAMOND_PICKAXE,
                    listOf(Material.IRON_ORE,
                        Material.DEEPSLATE_IRON_ORE,
                        Material.COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE,
                        Material.LAPIS_ORE,
                        Material.DEEPSLATE_LAPIS_ORE,
                        Material.GOLD_ORE,
                        Material.DEEPSLATE_GOLD_ORE,
                        Material.REDSTONE_ORE,
                        Material.DEEPSLATE_REDSTONE_ORE,
                        Material.DIAMOND_ORE,
                        Material.DEEPSLATE_DIAMOND_ORE,
                        Material.EMERALD_ORE,
                        Material.DEEPSLATE_EMERALD_ORE))
            }
        }

    private val sphereBasisBlocks = buildList {
        //all blocks in radius 10
        for (x in -10..10) {
            for (y in -10..10) {
                for (z in -10..10) {
                    if ((x.toDouble()).pow(2) + (y.toDouble()).pow(2) + (z.toDouble()).pow(2) < 100) {
                        add(org.bukkit.util.Vector(x, y, z))
                    }
                }
            }
        }
    }

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val item = e.material
        if (!this.axeToOre.containsKey(item)) //not a pickaxe
            return

        val ores = this.axeToOre[item]

        val world: World = e.player.world
        val playerLoc = e.player.location.toBlockLocation().toVector()

        for (p in sphereBasisBlocks) {
            val checking = p.add(playerLoc)
            val blockLoc = Location(world, checking.x, checking.y, checking.z)

            val blockAt: Material = world.getBlockAt(blockLoc).type
            if (ores?.contains(blockAt) == true) {
                val c = when (blockAt) {
                    Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE -> GlowAPI.Color.GRAY
                    Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE -> GlowAPI.Color.WHITE
                    Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE -> GlowAPI.Color.GOLD
                    Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE -> GlowAPI.Color.BLUE
                    Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE -> GlowAPI.Color.GREEN
                    Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE -> GlowAPI.Color.AQUA
                    Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE -> GlowAPI.Color.RED

                    //shouldn't be reachable
                    else -> GlowAPI.Color.BLACK
                }

                this.glow(loc = blockLoc, c = c, player = e.player)

                Bukkit.getScheduler().scheduleSyncDelayedTask(CTF.instance, {
                    this.unGlow(player = e.player)
                }, 3000)
            }
        }
    }

    private fun glow(loc: Location, c: GlowAPI.Color, player: Player) {
        player as CraftPlayer
        val shulker = loc.world.spawnEntity(loc, EntityType.SHULKER) as CraftShulker
        shulker.isInvulnerable = true
        shulker.isSilent = true
        shulker.setAI(false)
        shulker.isInvisible = false
        GlowAPI.setGlowing(shulker, c, player)


        if (this.spawnedShukers[player.uniqueId] == null) {
            this.spawnedShukers[player.uniqueId] = arrayListOf(shulker)
        } else {
            this.spawnedShukers[player.uniqueId]?.add(shulker)
        }


    }

    private var spawnedShukers: HashMap<UUID, ArrayList<Shulker>> = HashMap()

    private fun unGlow(player: Player) {
        val shulks = this.spawnedShukers[player.uniqueId] ?: return
        shulks.removeAll {
            it.remove()
            true
        }
    }
}