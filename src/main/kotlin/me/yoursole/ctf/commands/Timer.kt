package me.yoursole.ctf.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import me.yoursole.ctf.datafiles.GameData
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.*

object Timer : BrigadierCommand {
    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literal("timer").requiresOp().then(argument("time", IntegerArgumentType.integer()).executes { ctx ->
            GameData.timerMs = System.currentTimeMillis() + IntegerArgumentType.getInteger(ctx, "time")
            1
        }).then(literal("clear").executes { ctx ->
            GameData.timerMs = -1L
            1
        }))
    }
}