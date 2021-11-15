package me.yoursole.ctf.datafiles.items

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object CheapApple {
    val item by lazy {
        ItemStack(Material.APPLE).apply {
            itemMeta = itemMeta.apply {
                setDisplayName("§aCheap Apple")
                lore = listOf("§7For when you are feeling broke")
            }
        }
    }
}