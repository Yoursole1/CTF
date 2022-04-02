package me.yoursole.ctf.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.commands.CommandSourceStack

interface BrigadierCommand {
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>)
}

fun LiteralArgumentBuilder<CommandSourceStack>.requiresOp() = requires {
    it.hasPermission(it.server.operatorUserPermissionLevel)
}