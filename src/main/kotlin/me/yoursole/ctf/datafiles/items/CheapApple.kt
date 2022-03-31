package me.yoursole.ctf.datafiles.items

import me.yoursole.ctf.datafiles.Utils.setCTFId
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object CheapApple {
    val item: CraftItemStack by lazy {
        CraftItemStack.asCraftCopy(ItemStack(Material.APPLE)).apply {
            itemMeta = itemMeta.apply {
                setDisplayName("§aCheap Apple")
                lore = listOf("§7For when you are feeling broke")
                addEnchant(Enchantment.THORNS, 10, true)
            }
            setCTFId("cheapapple")
        }
    }
}