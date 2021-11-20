package me.yoursole.ctf.commands

import me.yoursole.ctf.CTF
import me.yoursole.ctf.datafiles.FlagDropper
import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.Utils.findSafeSpawnOrMake
import me.yoursole.ctf.datafiles.WorldManager
import me.yoursole.ctf.datafiles.WorldManager.getStructureNearSpawn
import me.yoursole.ctf.datafiles.WorldManager.sendToWorld
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.scoreboard.DisplaySlot

object Start : CommandExecutor {
    var id: Int = -1

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!GameData.gameRunning) {
            //
            WorldManager.generateNewWorlds()
            val type = GameData.structureTypes.random()
            val spawn = GameData.world.getStructureNearSpawn(type)!!
                .apply { y = GameData.world!!.getHighestBlockYAt(this).toDouble() }.findSafeSpawnOrMake()
            GameData.gameSpawnPoint = spawn
            val netherSpawn = GameData.world_nether.getStructureNearSpawn(GameData.structureTypesNether.random())!!
                .findSafeSpawnOrMake()
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

            GameData.gameRunning = true
            id = Bukkit.getScheduler().scheduleSyncDelayedTask(
                CTF.instance,
                {
                    GameData.scores.clear()
                    Bukkit.getScoreboardManager().mainScoreboard.getObjective(DisplaySlot.SIDEBAR)?.unregister()
                    FlagDropper.dropFlag()
                    for (player in Bukkit.getOnlinePlayers()) {
                        GameData.scores[player.uniqueId] = 0
                        player.isGlowing = false
                        player.sendMessage(
                            "§bThe selected structure is: ${
                                type?.name?.split('_')?.joinToString(" ") { str -> str.replaceFirstChar { it.titlecaseChar() } }
                            }!"
                        )
                        player.sendMessage(
                            "§bThe flag is dropping to (${GameData.dropLoc?.x}, ${GameData.dropLoc?.z})")
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