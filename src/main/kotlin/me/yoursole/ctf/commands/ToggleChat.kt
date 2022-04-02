package me.yoursole.ctf.commands

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.Util
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.*
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.ChatType
import net.minecraft.network.chat.TextComponent

object ToggleChat : BrigadierCommand {
    var chatMuted = false

    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            literal("togglechat").requiresOp().executes { ctx ->
                chatMuted = !chatMuted
                EntityArgument.getPlayers(ctx, "players").onEach {
                    it.sendMessage(TextComponent("§e${ctx.source.displayName.contents} ${(if (chatMuted) "muted" else "unmuted")} the chat!"), ChatType.SYSTEM, Util.NIL_UUID)
                }.size
            }
        )
    }
}