package me.yoursole.ctf.datafiles.items

import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.Utils
import me.yoursole.ctf.datafiles.Utils.setCTFId
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import java.util.*

object PyromancersCharm : Listener {
    val item: CraftItemStack by lazy {
        CraftItemStack.asCraftCopy(ItemStack(Material.LAVA_BUCKET)).apply {
            itemMeta = itemMeta.apply {
                setDisplayName("§6Pyromancer's Charm")
                lore = listOf(
                    "§eCaution! §c§lHOT!",
                    "§fWhile in your inventory, each hit",
                    "§fwill gain §cBlessing of the Pyromancer§r,",
                    "§flighting your enemies on fire!"
                )
                addEnchant(Enchantment.FIRE_ASPECT, 1, true)
                addItemFlags(ItemFlag.HIDE_ENCHANTS)
            }
            setCTFId("pyromancerscharm")
            val key = NamespacedKey.fromString("ctf:pyromancerscharm")!!
            val recipe = ShapedRecipe(key, this)
            recipe.shape("PBP", "BGB", "LLL")
            recipe.setIngredient('B', Material.BLAZE_ROD)
            recipe.setIngredient('P', Material.BLAZE_POWDER)
            recipe.setIngredient('G', RecipeChoice.MaterialChoice(Material.GOLD_BLOCK, Material.GHAST_TEAR))
            recipe.setIngredient('L', Material.LAVA_BUCKET)
            recipe.group = "ctf:pyromancerscharm"
            Bukkit.addRecipe(recipe)
            GameData.recipeKeys["pyromancerscharm"] = key
        }
    }

    @EventHandler
    fun onHit(event: EntityDamageByEntityEvent) {
        if (event.damager is Player && event.entity is LivingEntity && event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            val damager = event.damager as Player
            val target = event.entity as LivingEntity
            if (damager.inventory.any { it.isSimilar(item) }) {
                target.fireTicks = 300
            }
        }
    }

    @EventHandler
    fun onBucketPlace(event: PlayerBucketEmptyEvent) {
        val held = event.player.inventory.getItem(event.hand)
        if (item.isSimilar(held)) event.isCancelled = true
    }
}