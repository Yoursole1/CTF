package me.yoursole.ctf.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import me.yoursole.ctf.datafiles.GameData
import net.kyori.adventure.text.TextComponent
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.server.commands.GiveCommand
import net.minecraft.server.level.ServerPlayer
import kotlin.math.min

object CustomItem : BrigadierCommand {
    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literal("customitem").requiresOp()
            .then(argument("targets", EntityArgument.players())
                .then(argument("item",
            StringArgumentType.word()).suggests { ctx, builder ->
                SharedSuggestionProvider.suggest(GameData.customItems.keys, builder)
            }.executes { ctx ->
            giveCustomItem(ctx.source, EntityArgument.getPlayers(ctx, "targets"), StringArgumentType.getString(ctx, "item"), 1)
        }.then(argument("count",
            IntegerArgumentType.integer(1)).executes { ctx ->
            giveCustomItem(ctx.source, EntityArgument.getPlayers(ctx, "targets"), StringArgumentType.getString(ctx, "item"), IntegerArgumentType.getInteger(ctx, "count"))
        }))))
    }

    private fun giveCustomItem(source: CommandSourceStack, targets: Collection<ServerPlayer>, item: String, count: Int): Int {
        val customItem = GameData.customItems[item] ?: return 0
        val maxStackAmount = customItem.maxStackSize
        val maxAmount = maxStackAmount * GiveCommand.MAX_ALLOWED_ITEMSTACKS

        if (count > maxAmount) {
            source.sendFailure(TranslatableComponent("commands.give.failed.toomanyitems",
                maxAmount, (customItem.displayName() as TextComponent).content()))
            return 0
        }
        targets.forEach {
            val player = it.bukkitEntity
            var remaining = count
            while (remaining > 0) {
                val c = min(remaining, maxStackAmount)
                remaining -= c
                val stack = customItem.asQuantity(count)
                val items = player.inventory.addItem(stack)
                if (items.isNotEmpty()) {
                    for (i in items.values) {
                        val drop = player.world.dropItemNaturally(player.location, i)
                        drop.pickupDelay = 0
                        drop.owner = player.uniqueId
                        drop.thrower = player.uniqueId
                    }
                }
            }
        }
        return 1
    }

}