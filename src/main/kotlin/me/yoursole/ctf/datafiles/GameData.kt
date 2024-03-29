package me.yoursole.ctf.datafiles

import me.yoursole.ctf.datafiles.items.*
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import java.util.*

object GameData {
    var it: Player? = null
    var scores = HashMap<UUID, Int>()
    var timerMs: Long = -1
    val customItems = HashMap<String, ItemStack>()
    val recipeKeys = HashMap<String, NamespacedKey>()
    val haste = PotionEffectType.FAST_DIGGING.createEffect(60, 2).withParticles(false)
    val saturation = PotionEffectType.SATURATION.createEffect(60, 0).withParticles(false).withIcon(false)
    val dolphin = PotionEffectType.DOLPHINS_GRACE.createEffect(60, 3).withParticles(false)
    val nightVision = PotionEffectType.NIGHT_VISION.createEffect(2000, 68).withParticles(false).withIcon(false)
    val slowness = PotionEffectType.SLOW.createEffect(60, 1).withParticles(false)
    val regen = PotionEffectType.REGENERATION.createEffect(60, 1).withParticles(false)
    var itLoc: Location? = null
    var inNether = false
    var backUpLoc: Location? = null
    var dropLoc: Location? = null
    var droppingPos: Location? = null
    val netherHunters = ArrayList<Player>()
    val netherBackupLocs = HashMap<Player, Location>()
    var arrow = "|"
    val overworldName by lazy {
        NamespacedKey.fromString("ctf:world_game")!!
    }
    val netherName by lazy {
        NamespacedKey.fromString("ctf:world_nether_game")!!
    }
    var world: World? = null
    var world_nether: World? = null
    val structureTypes = setOf(
        StructureType.VILLAGE,
        StructureType.DESERT_PYRAMID,
        StructureType.JUNGLE_PYRAMID,
        StructureType.MINESHAFT,
        StructureType.PILLAGER_OUTPOST,
        //StructureType.WOODLAND_MANSION,
        //StructureType.RUINED_PORTAL
        StructureType.OCEAN_MONUMENT
    )
    val structureTypesNether = setOf(StructureType.NETHER_FORTRESS, StructureType.BASTION_REMNANT)
    var gameSpawnPoint: Location = Location(Bukkit.getWorld("world_game"), 0.0, 0.0, 0.0)
    var netherMainPoint: Location = Location(Bukkit.getWorld("world_nether_game"), 0.0, 0.0, 0.0)

    var gameRunning: Boolean = false


    init {
        try {
            customItems["flag"] = Flag.flag
            customItems["hermes"] = HermesBoots.item
            customItems["tunnelerspickaxe"] = TunnelersPickaxe.item
            customItems["goblinssword"] = GoblinsSword.item
            customItems["thorsaxe"] = ThorsAxe.item
            customItems["cheapapple"] = CheapApple.item
            customItems["scrapanvil"] = ScrapAnvil.item
            customItems["pyromancerscharm"] = PyromancersCharm.item
            customItems["machinegunbow"] = MachineGunBow.item
            customItems["webshooter"] = WebShooter.item
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}