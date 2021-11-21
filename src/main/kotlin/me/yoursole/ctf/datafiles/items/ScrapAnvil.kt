package me.yoursole.ctf.datafiles.items

import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.Utils.setCTFId
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

object ScrapAnvil {
    val item: CraftItemStack by lazy {
        CraftItemStack.asCraftCopy(ItemStack(Material.CHIPPED_ANVIL)).apply {
            itemMeta = itemMeta.apply {
                setDisplayName("§7Scrap Anvil")
                lore = listOf("§eUSE WITH CAUTION!")
            }
            setCTFId("scrapanvil")
            val key = NamespacedKey.fromString("ctf:scrapanvil")!!
            val recipe = ShapedRecipe(key, this)
            recipe.shape("IBI", " I ", " I ")
            recipe.setIngredient('B', Material.IRON_BLOCK)
            recipe.setIngredient('I', Material.IRON_INGOT)
            recipe.group = "ctf:scrapanvil"
            Bukkit.addRecipe(recipe)
            GameData.recipeKeys["scrapanvil"] = key
        }
    }
}