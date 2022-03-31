package me.yoursole.ctf.datafiles.items

import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.Utils
import me.yoursole.ctf.datafiles.Utils.setCTFId
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import kotlin.random.Random
import kotlin.random.nextInt

object GoblinsSword : Listener {
    @EventHandler
    fun onDeath(event: EntityDeathEvent) {
        val entity = event.entity
        val killer = entity.killer
        if (killer == null || entity is Player) return
        if (Utils.compareBreakable(killer.inventory.itemInMainHand, item)) {
            if (Random.nextInt(0..1) == 0) {
                for (item in event.drops) {
                    item.amount = item.amount * 2
                }
            }
        }
    }

    val item: CraftItemStack by lazy {
        CraftItemStack.asCraftCopy(ItemStack(Material.STONE_SWORD)).apply {
            itemMeta = itemMeta.apply {
                setDisplayName("§6Goblin's Sword")
                lore = listOf(
                    "§6Item Ability: Greed",
                    "§fGrants a 50% chance to double all mob drops."
                )
            }
            setCTFId("goblinssword")
            val key = NamespacedKey.fromString("ctf:goblinssword")!!
            val recipe = ShapedRecipe(key, this)
            recipe.shape(" G ", " I ", " B ")
            recipe.setIngredient('G', Material.GOLD_INGOT)
            recipe.setIngredient('I', Material.IRON_INGOT)
            recipe.setIngredient('B', Material.BONE)
            recipe.group = "ctf:goblinssword"
            Bukkit.addRecipe(recipe)
            GameData.recipeKeys["goblinssword"] = key
        }
    }
}