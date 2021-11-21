package me.yoursole.ctf.datafiles

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldInitEvent
import org.bukkit.event.world.WorldUnloadEvent
import kotlin.random.Random
import kotlin.random.nextLong

object WorldManager : Listener {
    @EventHandler
    fun onWorldInit(event: WorldInitEvent) {
        if (event.world.key.namespace == "ctf") {
            event.world.isAutoSave = false
            event.world.keepSpawnInMemory = false
        }
    }

    @EventHandler
    fun onWorldUnload(event: WorldUnloadEvent) {
        event.world.evacuatePlayers()
    }

    fun generateNewWorlds() {
        val overworldName = GameData.overworldName
        val netherName = GameData.netherName
        //ensures the worlds that are generated are deleted
        deleteWorlds()
        GameData.world = WorldCreator(overworldName).apply {
            environment(World.Environment.NORMAL)
            type(WorldType.NORMAL)
            generatorSettings("2;0;1;")
            seed(Random.nextLong(1L..100000L))
        }.createWorld()
        GameData.world_nether = WorldCreator(netherName).apply {
            environment(World.Environment.NETHER)
            type(WorldType.NORMAL)
            generatorSettings("2;0;1;")
            seed(Random.nextLong(1L..100000L))
        }.createWorld()
        GameData.world!!.setGameRule(GameRule.KEEP_INVENTORY, true)
        GameData.world_nether!!.setGameRule(GameRule.KEEP_INVENTORY, true)
        //worlds should be generated
    }

    fun deleteWorlds() {
        val overworld = Bukkit.getWorld(GameData.overworldName)
        val nether = Bukkit.getWorld(GameData.netherName)
        if (overworld != null) {
            overworld.evacuatePlayers()
            Bukkit.unloadWorld(overworld, false)
            overworld.worldFolder.deleteRecursively()
        }
        if (nether != null) {
            nether.evacuatePlayers()
            Bukkit.unloadWorld(nether, false)
            nether.worldFolder.deleteRecursively()
        }
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

    fun World.evacuatePlayers() {
        val mainWorld = Bukkit.getWorlds().find { it.key.namespace != "ctf" }
        for (player in this.players) {
            player.sendMessage(
                Component.text("The previous world you were on has closed!").color(NamedTextColor.YELLOW)
            )
            player sendToWorld mainWorld
        }
    }
}