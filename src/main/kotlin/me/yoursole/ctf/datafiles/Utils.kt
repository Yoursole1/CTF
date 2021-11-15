package me.yoursole.ctf.datafiles

import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import java.io.File
import kotlin.random.Random
import kotlin.random.nextLong

object Utils {
    fun compareBreakable(first: ItemStack?, second: ItemStack?): Boolean {
        var similar = false
        if (first == null || second == null) {
            return similar
        }
        val sameTypeId = first.type == second.type
        val sameHasItemMeta = first.hasItemMeta() == second.hasItemMeta()
        val sameEnchantments = first.enchantments == second.enchantments
        var sameItemMeta = true
        if (sameHasItemMeta) {
            val m1 = first.itemMeta
            val m2 = second.itemMeta
            if ((m1 is Damageable) xor (m2 is Damageable)) return false
            if (m1 is Damageable && m2 is Damageable) {
                m1.damage = 0
                m2.damage = 0
                sameItemMeta = Bukkit.getItemFactory().equals(m1, m2)
            }
        }
        if (sameTypeId && sameHasItemMeta && sameEnchantments && sameItemMeta) {
            similar = true
        }
        return similar
    }

    fun generateNewWorlds() {
        val overworldName = GameData.overworldName
        val netherName = GameData.netherName
        var f = File(Bukkit.getServer().worldContainer.absolutePath + "/" + overworldName)
        deleteWorld(f)
        f = File(Bukkit.getServer().worldContainer.absolutePath + "/" + netherName)
        deleteWorld(f)
        //ensures the worlds that are generated are deleted
        var wc = WorldCreator(overworldName)
        wc.environment(World.Environment.NORMAL)
        wc.type(WorldType.NORMAL)
        wc.generatorSettings("2;0;1;")
        wc.seed(Random.nextLong(1L..100000L))
        GameData.world = wc.createWorld()
        //alternative Bukkit.createWorld(wc)
        wc = WorldCreator(netherName)
        wc.environment(World.Environment.NETHER)
        wc.type(WorldType.NORMAL)
        wc.generatorSettings("2;0;1;")
        wc.seed(Random.nextLong(1L..100000L))
        GameData.world_nether = wc.createWorld()

        //worlds should be generated
    }

    fun deleteWorlds() {
        val overworldName = GameData.overworldName
        val netherName = GameData.netherName
        Bukkit.unloadWorld(overworldName, false)
        Bukkit.unloadWorld(netherName, false)
        var f = File(Bukkit.getServer().worldContainer.absolutePath + "/" + overworldName)
        deleteWorld(f)
        f = File(Bukkit.getServer().worldContainer.absolutePath + "/" + netherName)
        deleteWorld(f)
    }

    fun deleteWorld(path: File) {
        if (path.exists()) {
            for (file in path.listFiles()!!) {
                if (file.isDirectory) {
                    deleteWorld(file)
                } else {
                    file.delete()
                }
            }
        }
        path.delete()
    }

    /*
    Only teleports the player if they are not in the world at all
    Does not have an effect if the player is already in the world
     */
    infix fun Player.sendToWorld(world: World?) {
        if (world != null && location.world != world) {
            teleport(world.spawnLocation)
        }
    }

    fun World?.getStructureNearSpawn(type: StructureType?): Location? {
        if (this == null || type == null) return null
        return locateNearestStructure(spawnLocation, type, 10000, false)
    }

    fun Player.doTelekinesis(vararg stack: ItemStack) {
        val items = inventory.addItem(*stack)
        if (items.isNotEmpty()) {
            for (item in items.values) {
                val drop = world.dropItemNaturally(location, item)
                drop.pickupDelay = 5
                drop.owner = uniqueId
                drop.thrower = uniqueId
            }
        }
    }
}