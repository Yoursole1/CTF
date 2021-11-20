package me.yoursole.ctf.commands

import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.Utils.findSafeSpawnOrMake
import me.yoursole.ctf.datafiles.WorldManager
import me.yoursole.ctf.datafiles.WorldManager.getStructureNearSpawn
import me.yoursole.ctf.datafiles.WorldManager.sendToWorld
import me.yoursole.ctf.datafiles.items.Flag
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.attribute.Attribute
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.scoreboard.DisplaySlot
import java.util.*

object Start : CommandExecutor {
    var scheduler:org.bukkit.scheduler.BukkitScheduler = TODO()
    var id:Int = TODO()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (GameData.it == null) {
            //
            WorldManager.generateNewWorlds()
            val type = GameData.structureTypes.random()
            val spawn = GameData.world.getStructureNearSpawn(type)!!.apply { y = GameData.world!!.getHighestBlockYAt(this).toDouble() }.findSafeSpawnOrMake()
            GameData.gameSpawnPoint = spawn
            val netherSpawn = GameData.world_nether.getStructureNearSpawn(GameData.structureTypesNether.random())!!.findSafeSpawnOrMake()
            GameData.netherMainPoint = netherSpawn
            GameData.world_nether!!.worldBorder.size = 100.0
            GameData.world_nether!!.worldBorder.center = GameData.netherMainPoint!!
            GameData.world!!.worldBorder.center = spawn
            GameData.world!!.worldBorder.size = 200.0
            for(player in Bukkit.getOnlinePlayers()){
                player.sendToWorld(GameData.world)
            }
            //set the timer
            GameData.timerMs = System.currentTimeMillis() + 24000L //15 minutes


            scheduler = Bukkit.getServer().scheduler
            id = scheduler.scheduleSyncDelayedTask(
                Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("CapturetheFlag"))!!,
                {
                    GameData.scores.clear()
                    Bukkit.getScoreboardManager().mainScoreboard.getObjective(DisplaySlot.SIDEBAR)?.unregister()
                    val chosen = Bukkit.getOnlinePlayers().filter { it.gameMode == GameMode.SURVIVAL }.random()
                    chosen.inventory.addItem(Flag.flag)
                    chosen.isGlowing = true
                    GameData.itLoc = chosen.location
                    GameData.it = chosen
                    chosen.sendMessage("§aYou got the flag to start!")
                    for (player in Bukkit.getOnlinePlayers()) {
                        GameData.scores[player.uniqueId] = 0
                        if (player.displayName != player.displayName) {
                            player.sendMessage("§a${player.displayName} has received the flag")
                            player.isGlowing = false
                        }
                        player.sendMessage(
                            "§bThe selected structure is: ${
                                type?.name?.split('_')?.joinToString(" ") { str -> str.replaceFirstChar { it.titlecaseChar() } }
                            }!"
                        )
                        player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
                        player.activePotionEffects.clear()
                    }
                }, 24000L//15 minutes
            )

            //on player death, tp player to world spawn if they are playing the game
            //on player portal, tp to appropriate dimension
            //


        } else {
            sender.sendMessage("§cThe game is already running")
        }
        return true
    }
}