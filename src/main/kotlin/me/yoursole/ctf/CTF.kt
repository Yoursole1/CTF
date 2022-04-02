package me.yoursole.ctf

import me.yoursole.ctf.commands.*
import me.yoursole.ctf.datafiles.*
import me.yoursole.ctf.datafiles.items.*
import me.yoursole.ctf.events.*
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_18_R2.CraftServer
import org.bukkit.plugin.java.JavaPlugin

class CTF : JavaPlugin() {

    companion object {
        lateinit var instance: CTF
    }

    init {
        instance = this
    }

    override fun onEnable() {
        listOf(
            WorldManager,
            MoveItemEvent,
            DropItemEvent,
            PlayerDeath,
            GetCompass,
            PlaceFlagEvent,
            Join,
            Leave,
            ChatEvent,
            BlockBreak,
            HermesBoots,
            TunnelersPickaxe,
            GoblinsSword,
            ThorsAxe,
            BuildLimit,
            ItDamagePlayer,
            CraftEvent,
            ItLocationManager,
            RespawnEvent,
            ChunkLoad,
            EntityDeath,
            FlagDropper,
            PyromancersCharm,
            RecipeManager,
            MachineGunBow,
            WebShooter,
            OreFinder
        ).forEach {
            server.pluginManager.registerEvents(it, this)
        }
        val dispatcher = (server as CraftServer).server.vanillaCommandDispatcher.dispatcher
        listOf(
            ToggleChat,
            Top,
            CustomItem,
            Timer,
            Scores,
            Start,
            Stop
        ).forEach { it.register(dispatcher) }
        GameLoop()

        for (player in Bukkit.getOnlinePlayers()) {
            GameData.scores[player.uniqueId] = 0
            player.discoverRecipes(GameData.recipeKeys.values)
        }
    }

    override fun onDisable() {
        for (key in GameData.recipeKeys.values) {
            Bukkit.removeRecipe(key)
        }
        Bukkit.getScheduler().cancelTask(GameLoop.taskId)
        WorldManager.deleteWorlds()
        for (player in Bukkit.getOnlinePlayers()) {
            player.isGlowing = false
        }
    }
}