package me.yoursole.ctf.commands

import com.mojang.brigadier.CommandDispatcher
import me.yoursole.ctf.CTF
import me.yoursole.ctf.datafiles.FlagDropper
import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.Utils.findSafeSpawnOrMake
import me.yoursole.ctf.datafiles.WorldManager
import me.yoursole.ctf.datafiles.WorldManager.getStructureNearSpawn
import me.yoursole.ctf.datafiles.WorldManager.sendToWorld
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.*
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.scoreboard.DisplaySlot
import kotlin.random.Random

object Start : BrigadierCommand {
    var id: Int = -1

    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literal("start").requiresOp().executes { ctx ->
            startGame(ctx.source, false)
        }.then(literal("startnow").executes { ctx ->
            startGame(ctx.source, true)
        }))
    }

    private fun startGame(source: CommandSourceStack, startImmediately: Boolean): Int {
        val sender = source.bukkitSender
        if (GameData.gameRunning) {
            sender.sendMessage("§cGame is already running!")
            return 0
        }
        WorldManager.generateNewWorlds()
        val type = GameData.structureTypes.random()
        val spawn = GameData.world.getStructureNearSpawn(type)!!
            .apply { y = GameData.world!!.getHighestBlockYAt(this).toDouble() }.findSafeSpawnOrMake()
        GameData.gameSpawnPoint = spawn
        val netherSpawn = GameData.world_nether.getStructureNearSpawn(GameData.structureTypesNether.random())!!
            .findSafeSpawnOrMake()
        GameData.netherMainPoint = netherSpawn
        GameData.world_nether!!.worldBorder.size = 100.0
        GameData.world_nether!!.worldBorder.center = GameData.netherMainPoint
        GameData.world!!.worldBorder.center = spawn
        GameData.world!!.worldBorder.size = 200.0
        GameData.scores.clear()
        Bukkit.getScoreboardManager().mainScoreboard.getObjective(DisplaySlot.SIDEBAR)?.unregister()
        for (player in Bukkit.getOnlinePlayers()) {
            player.sendToWorld(GameData.world)
            GameData.scores[player.uniqueId] = 0
            player.isGlowing = false
            player.sendMessage(
                "§bThe selected structure is: ${
                    type?.name?.split('_')?.joinToString(" ") { str -> str.replaceFirstChar { it.titlecaseChar() } }
                }!"
            )
            player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
            player.activePotionEffects.clear()
            player.inventory.clear()
        }
        //set the timer
        GameData.timerMs = System.currentTimeMillis() + 15 * 60 * 1000 //15 minutes

        GameData.gameRunning = true
        id = Bukkit.getScheduler().scheduleSyncDelayedTask(
            CTF.instance,
            {
                FlagDropper.dropFlag(GameData.world!!,
                    GameData.gameSpawnPoint.blockX + Random.nextInt(-100, 100),
                    GameData.gameSpawnPoint.blockZ + Random.nextInt(-100, 100),
                    75)
            }, if (startImmediately) 1L else 15 * 60 * 20 //15 minutes
        )

        //on player death, tp player to world spawn if they are playing the game
        //on player portal, tp to appropriate dimension
        //
        return 1
    }
}