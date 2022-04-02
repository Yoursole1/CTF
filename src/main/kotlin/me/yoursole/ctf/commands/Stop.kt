package me.yoursole.ctf.commands

import com.mojang.brigadier.CommandDispatcher
import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.WorldManager
import me.yoursole.ctf.datafiles.items.Flag
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.literal
import net.minecraft.network.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.*

object Stop : BrigadierCommand {
    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literal("stop").requiresOp().executes { ctx ->
            if (!GameData.gameRunning) {
                ctx.source.sendFailure(TextComponent("Game is not running!"))
                return@executes 0
            }
            for (player in Bukkit.getOnlinePlayers()) {
                player.inventory.remove(Flag.flag)
                player.isGlowing = false
            }

            if (Bukkit.getScheduler().isQueued(Start.id))
                Bukkit.getScheduler().cancelTask(Start.id)

            WorldManager.deleteWorlds()
            if (GameData.it != null) {
                GameData.it!!.isGlowing = false
                GameData.scores.compute(GameData.it!!.uniqueId) { _: UUID?, score: Int? -> (score ?: 0) + 60 }
            }
            GameData.it = null
            GameData.droppingPos = null
            GameData.dropLoc = null
            ctx.source.sendSuccess(TextComponent("Game Stopped!"), true)
            val a = GameData.scores.entries.maxByOrNull { it.value }?.key
            if (a != null) {
                for (player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage("§aThe winner is ${Bukkit.getPlayer(a)?.displayName}!")
                }
            }
            GameData.scores.clear()
            GameData.gameRunning = false
            1
        })
    }
}