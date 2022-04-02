package me.yoursole.ctf.datafiles.items

import me.yoursole.ctf.datafiles.Point3D
import net.minecraft.nbt.NBTTagCompound
import org.apache.commons.lang.math.RandomUtils.nextInt
import org.bukkit.*
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scoreboard.Scoreboard
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.pow
import kotlin.random.Random.Default.nextInt


object OreFinder : Listener{
    private val axeToOre: java.util.HashMap<Material?, List<Material>?> = object : java.util.HashMap<Material?, List<Material>?>() {
        init {
            put(Material.WOODEN_PICKAXE, listOf(Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE))

            put(Material.STONE_PICKAXE, listOf(Material.IRON_ORE, Material.DEEPSLATE_COAL_ORE, Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE
            , Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE))

            put(Material.GOLDEN_PICKAXE, listOf(Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE))

            put(Material.IRON_PICKAXE, listOf(Material.IRON_ORE, Material.DEEPSLATE_COAL_ORE, Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE
                , Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE,
            Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE, Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE))

            put(Material.DIAMOND_PICKAXE, listOf(Material.IRON_ORE, Material.DEEPSLATE_COAL_ORE, Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE
                , Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE,
                Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE, Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE))
        }
    }

    private val sphereBasisBlocks: ArrayList<Point3D> = object : ArrayList<Point3D>(){ //all blocks in radius 10
        init {
            for (x in -10..10){
                for (y in -10..10){
                    for (z in -10..10){
                        if((x.toDouble()).pow(2) + (y.toDouble()).pow(2) + (z.toDouble()).pow(2) < 100){
                            add(Point3D(x, y, z))
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent){
        val item = e.material
        if(!this.axeToOre.containsKey(item)) //not a pickaxe
            return

        val ores = this.axeToOre[item]

        val world: World = e.player.world
        val playerLoc = Point3D(e.player.location.blockX, e.player.location.blockY, e.player.location.blockZ)

        for(p:Point3D in this.sphereBasisBlocks){
            val checking = p.add(playerLoc)
            val blockLoc = Location(world, checking.x.toDouble(), checking.y.toDouble(), checking.z.toDouble())

            val blockAt: Material = world.getBlockAt(blockLoc).type
            if(ores?.contains(blockAt) == true) run {
                var c: ChatColor? = null
                 when (blockAt) {
                    Material.COAL_ORE -> c = ChatColor.GRAY
                    Material.DEEPSLATE_COAL_ORE -> c = ChatColor.GRAY

                    Material.IRON_ORE -> c = ChatColor.WHITE
                    Material.DEEPSLATE_IRON_ORE -> c = ChatColor.WHITE

                    Material.GOLD_ORE -> c = ChatColor.GOLD
                    Material.DEEPSLATE_GOLD_ORE -> c = ChatColor.GOLD

                    Material.LAPIS_ORE -> c = ChatColor.BLUE
                    Material.DEEPSLATE_LAPIS_ORE -> c = ChatColor.BLUE

                    Material.EMERALD_ORE -> c = ChatColor.GREEN
                    Material.DEEPSLATE_EMERALD_ORE -> c = ChatColor.GREEN

                    Material.DIAMOND_ORE -> c = ChatColor.AQUA
                    Material.DEEPSLATE_DIAMOND_ORE -> c = ChatColor.AQUA

                    Material.REDSTONE_ORE -> c = ChatColor.RED
                    Material.DEEPSLATE_REDSTONE_ORE -> c = ChatColor.RED

                    //shouldn't be reachable
                    else -> c = ChatColor.BLACK
                }

                this.glow(loc = blockLoc, c = c, player = e.player)

                Executors.newSingleThreadScheduledExecutor().schedule({
                    this.unGlow(player = e.player)
                }, 3000, TimeUnit.MILLISECONDS)
            }
        }
    }

    private fun glow(loc: Location, c:ChatColor, player: Player){
        var livingS: Entity = loc.world.spawnEntity(loc, EntityType.SHULKER)
        livingS.isGlowing = true
        livingS.isInvulnerable = true
        livingS.isSilent = true

        var e2 = (livingS as LivingEntity)
        e2.setAI(false)
        e2.isInvisible = false


        if(this.spawnedShukers[player.uniqueId]==null){
            this.spawnedShukers[player.uniqueId] = arrayListOf(livingS)
        }else{
            this.spawnedShukers[player.uniqueId]?.add(livingS)
        }


    }
    private var spawnedShukers: HashMap<UUID, ArrayList<Entity>> = HashMap()

    private fun unGlow(player: Player){
        var shulks = this.spawnedShukers[player.uniqueId] ?: return
        for(shulk in shulks){
            (shulk as LivingEntity).isInvulnerable = false
            (shulk as LivingEntity).damage(100000.0)
        }

        this.spawnedShukers[player.uniqueId] = ArrayList()
    }







}