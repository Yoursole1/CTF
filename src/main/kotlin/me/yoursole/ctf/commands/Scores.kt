package me.yoursole.ctf.commands

import com.mojang.brigadier.CommandDispatcher
import me.yoursole.ctf.datafiles.GameData
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.*
import org.bukkit.Bukkit

object Scores : BrigadierCommand {
    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literal("scores").executes { ctx ->
            ctx.source.bukkitSender.sendMessage(GameData.scores.entries.joinToString("\n") { (uuid, points) ->
                "§a${Bukkit.getPlayer(uuid)?.displayName}: $points points"
            })
            1
        })
    }
}