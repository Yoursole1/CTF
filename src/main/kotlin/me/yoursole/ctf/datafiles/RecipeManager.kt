package me.yoursole.ctf.datafiles

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Tag
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.Listener
import org.bukkit.inventory.*
import org.bukkit.material.MaterialData

object RecipeManager : Listener {
    init {
        createKelpPaper()
        createSharpnessBook()
        createUnbreakingBook()
        createSaddle()
        smeltableLeather()
        woolToString()
    }

    private fun woolToString() {
        val key = NamespacedKey.fromString("ctf:wooltostring")!!
        val recipe = ShapelessRecipe(key, ItemStack(Material.STRING, 4))
        recipe.addIngredient(RecipeChoice.MaterialChoice(Tag.WOOL))
        recipe.group = "ctf:wooltostring"
        Bukkit.addRecipe(recipe)
        GameData.recipeKeys["wooltostring"] = key
    }

    private fun smeltableLeather() {
        val key = NamespacedKey.fromString("ctf:rottenleather")!!
        val recipe = FurnaceRecipe(key, ItemStack(Material.LEATHER), Material.ROTTEN_FLESH, 0.1f, 200)
        Bukkit.addRecipe(recipe)
        GameData.recipeKeys["rottenleather"] = key
    }

    private fun createSaddle() {
        val key = NamespacedKey.fromString("ctf:saddle")!!
        val recipe = ShapedRecipe(key, ItemStack(Material.SADDLE))
        recipe.shape("   ", "SLS", "LIL")
        recipe.setIngredient('S', Material.STRING)
        recipe.setIngredient('L', Material.LEATHER)
        recipe.setIngredient('I', Material.IRON_INGOT)
        recipe.group = "ctf:saddle"
        Bukkit.addRecipe(recipe)
        GameData.recipeKeys["saddle"] = key
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