package me.yoursole.ctf.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.*
import net.minecraft.server.level.ServerPlayer
import java.util.UUID


object Top : BrigadierCommand {
    private val cooldowns = HashMap<UUID, Long>()
    private val cooldownTime = 20

    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literal("top").requires { it.entity is ServerPlayer }.executes { ctx ->
            val player = ctx.source.playerOrException.bukkitEntity
            if (cooldowns.containsKey(player.uniqueId)) {
                val secondsLeft = cooldowns[player.uniqueId]!! / 1000 + cooldownTime - System.currentTimeMillis() / 1000
                if (secondsLeft > 0) {
                    player.sendMessage("§aYou can't use /top for another $secondsLeft ${if (secondsLeft != 1L) "seconds!" else "second!"}")
                    return@executes Command.SINGLE_SUCCESS
                }
            }
            cooldowns[player.uniqueId] = System.currentTimeMillis()
            val world = player.world
            val highest = world.getHighestBlockAt(player.location).location
            highest.direction = player.location.direction
            highest.add(0.0, 1.0, 0.0)
            player.teleport(highest)
            player.sendMessage("§aWhoosh!")
            Command.SINGLE_SUCCESS
        })
    }
}