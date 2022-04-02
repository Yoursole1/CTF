package me.yoursole.ctf.datafiles.items

import me.yoursole.ctf.datafiles.Point3D
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Shulker
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
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

    private val sphereBasisBlocks: ArrayList<Point3D> = object : ArrayList<Point3D>() { //all blocks in radius 10
        init {
            for (x in -10..10) {
                for (y in -10..10) {
                    for (z in -10..10) {
                        if ((x.toDouble()).pow(2) + (y.toDouble()).pow(2) + (z.toDouble()).pow(2) < 100) {
                            add(Point3D(x, y, z))
                        }
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
        val playerLoc = Point3D(e.player.location.blockX, e.player.location.blockY, e.player.location.blockZ)

        for (p: Point3D in this.sphereBasisBlocks) {
            val checking = p.add(playerLoc)
            val blockLoc = Location(world, checking.x.toDouble(), checking.y.toDouble(), checking.z.toDouble())

            val blockAt: Material = world.getBlockAt(blockLoc).type
            if (ores?.contains(blockAt) == true) {
                val c = when (blockAt) {
                    Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE -> ChatColor.GRAY
                    Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE -> ChatColor.WHITE
                    Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE -> ChatColor.GOLD
                    Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE -> ChatColor.BLUE
                    Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE -> ChatColor.GREEN
                    Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE -> ChatColor.AQUA
                    Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE -> ChatColor.RED

                    //shouldn't be reachable
                    else -> ChatColor.BLACK
                }

                this.glow(loc = blockLoc, c = c, player = e.player)

                Executors.newSingleThreadScheduledExecutor().schedule({
                    this.unGlow(player = e.player)
                }, 3000, TimeUnit.MILLISECONDS)
            }
        }
    }

    private fun glow(loc: Location, c: ChatColor, player: Player) {
        val livingS = loc.world.spawnEntity(loc, EntityType.SHULKER) as Shulker
        livingS.isGlowing = true
        livingS.isInvulnerable = true
        livingS.isSilent = true
        livingS.setAI(false)
        livingS.isInvisible = false


        if (this.spawnedShukers[player.uniqueId] == null) {
            this.spawnedShukers[player.uniqueId] = arrayListOf(livingS)
        } else {
            this.spawnedShukers[player.uniqueId]?.add(livingS)
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