package me.yoursole.ctf.datafiles

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe

object RecipeManager : Listener {
    init {
        createKelpPaper()
        createSharpnessBook()
        createUnbreakingBook()
    }

    private fun createKelpPaper() {
        val key = NamespacedKey.fromString("ctf:kelppaper")!!
        val recipe = ShapedRecipe(key, ItemStack(Material.PAPER, 3))
        recipe.shape("   ", "KKK", "   ")
        recipe.setIngredient('K', Material.KELP)
        recipe.group = "ctf:kelppaper"
        Bukkit.addRecipe(recipe)
        GameData.recipeKeys["kelppaper"] = key
    }

    private fun createSharpnessBook() {
        val key = NamespacedKey.fromString("ctf:sharpnessbook")!!
        val recipe = ShapelessRecipe(key, ItemStack(Material.ENCHANTED_BOOK).apply { addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1) })
        recipe.addIngredient(Material.DIAMOND)
        recipe.addIngredient(Material.BOOK)
        recipe.group = "ctf:sharpnessbook"
        Bukkit.addRecipe(recipe)
        GameData.recipeKeys["sharpnessbook"] = key
    }

    private fun createUnbreakingBook() {
        val key = NamespacedKey.fromString("ctf:unbreakingbook")!!
        val recipe = ShapelessRecipe(key, ItemStack(Material.ENCHANTED_BOOK).apply { addUnsafeEnchantment(Enchantment.DURABILITY, 1) })
        recipe.addIngredient(Material.IRON_INGOT)
        recipe.addIngredient(Material.BOOK)
        recipe.group = "ctf:unbreakingbook"
        Bukkit.addRecipe(recipe)
        GameData.recipeKeys["unbreakingbook"] = key
    }
}