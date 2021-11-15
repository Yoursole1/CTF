package me.yoursole.ctf.commands

import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.Utils
import me.yoursole.ctf.datafiles.Utils.getStructureNearSpawn
import me.yoursole.ctf.datafiles.Utils.sendToWorld
import me.yoursole.ctf.datafiles.items.Flag
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.attribute.Attribute
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.scoreboard.DisplaySlot

object Start : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (GameData.it == null) {
            if (sender.isOp) {
                //
                Utils.generateNewWorlds()
                val type = GameData.structureTypes.random()
                val spawn = GameData.world.getStructureNearSpawn(type)
                GameData.gameSpawnPoint = spawn
                spawn!!.y = GameData.world!!.getHighestBlockYAt(spawn).toDouble()
                GameData.netherMainPoint =
                    GameData.world_nether.getStructureNearSpawn(GameData.structureTypesNether.random())
                GameData.world_nether!!.worldBorder.size = 100.0
                GameData.world_nether!!.worldBorder.center = GameData.netherMainPoint!!
                GameData.world!!.worldBorder.center = spawn
                GameData.world!!.worldBorder.size = 200.0
                for (player in Bukkit.getOnlinePlayers()) {
                    player sendToWorld GameData.world
                }

                //on player death, tp player to world spawn if they are playing the game
                //on player portal, tp to appropriate dimension
                //
                GameData.scores.clear()
                val sb = Bukkit.getScoreboardManager()
                val s = sb.mainScoreboard
                val o = s.getObjective(DisplaySlot.SIDEBAR)
                o?.unregister()
                val player = Bukkit.getOnlinePlayers().filter { it.gameMode == GameMode.SURVIVAL }.random()
                player.inventory.addItem(Flag.flag)
                player.isGlowing = true
                GameData.itLoc = player.location
                GameData.it = player
                player.sendMessage("§aYou got the flag to start!")
                for (playera in Bukkit.getOnlinePlayers()) {
                    GameData.scores[playera.uniqueId] = 0
                    if (player.displayName != playera.displayName) {
                        playera.sendMessage("§a${player.displayName} has received the flag")
                        playera.isGlowing = false
                    }
                    playera.sendMessage("§bThe selected structure is: ${type?.name}!")
                    playera.health = playera.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
                    playera.activePotionEffects.clear()
                }
            } else {
                sender.sendMessage("don't u dare ._.")
            }
        } else {
            sender.sendMessage("§cThe game is already running")
        }
        return true
    }
}