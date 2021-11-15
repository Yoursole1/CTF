package me.yoursole.ctf.datafiles.items

import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object Flag {
    val flag by lazy {
        ItemStack(Material.BLUE_BANNER).apply {
            itemMeta = itemMeta.apply {
                setDisplayName("§9The Flag")
                lore = listOf(
                    "§6Keep me safe",
                    "When I am in your inventory you get points"
                )
                isUnbreakable = true
                setCustomModelData(1)
                addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE)
            }
        }
    }
}