package me.yoursole.ctf

import me.yoursole.ctf.commands.*
import me.yoursole.ctf.datafiles.*
import me.yoursole.ctf.datafiles.items.*
import me.yoursole.ctf.events.*
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class CTF : JavaPlugin() {

    companion object {
        lateinit var instance: CTF
    }

    init {
        instance = this
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(WorldManager, this)
        server.pluginManager.registerEvents(MoveItemEvent, this)
        server.pluginManager.registerEvents(DropItemEvent, this)
        server.pluginManager.registerEvents(PlayerDeath, this)
        server.pluginManager.registerEvents(GetCompass, this)
        server.pluginManager.registerEvents(PlaceFlagEvent, this)
        server.pluginManager.registerEvents(Join, this)
        server.pluginManager.registerEvents(Leave, this)
        server.pluginManager.registerEvents(ChatEvent, this)
        server.pluginManager.registerEvents(BlockBreak, this)
        server.pluginManager.registerEvents(HermesBoots, this)
        server.pluginManager.registerEvents(TunnelersPickaxe, this)
        server.pluginManager.registerEvents(GoblinsSword, this)
        server.pluginManager.registerEvents(ThorsAxe, this)
        server.pluginManager.registerEvents(BuildLimit, this)
        server.pluginManager.registerEvents(ItDamagePlayer, this)
        server.pluginManager.registerEvents(CraftEvent, this)
        server.pluginManager.registerEvents(ItLocationManager, this)
        server.pluginManager.registerEvents(RespawnEvent, this)
        server.pluginManager.registerEvents(ChunkLoad, this)
        server.pluginManager.registerEvents(EntityDeath, this)
        server.pluginManager.registerEvents(FlagDropper, this)
        server.pluginManager.registerEvents(PyromancersCharm, this)
        server.pluginManager.registerEvents(RecipeManager, this)
        server.pluginManager.registerEvents(MachineGunBow, this)
        server.pluginManager.registerEvents(OreFinder, this)

        getCommand("start")!!.setExecutor(Start)
        getCommand("stop")!!.setExecutor(Stop)
        getCommand("scores")!!.setExecutor(Scores)
        getCommand("timer")!!.setExecutor(Timer)
        getCommand("togglechat")!!.setExecutor(ToggleChat)
        getCommand("customitem")!!.setExecutor(CustomItem)
        getCommand("top")!!.setExecutor(Top)
        getCommand("test")!!.setExecutor(Test)
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